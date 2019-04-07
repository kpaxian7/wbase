package com.bnb.wbasemodule.http;

import com.bnb.wbasemodule.utils.JLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static java.net.HttpURLConnection.HTTP_NOT_MODIFIED;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static okhttp3.internal.http.StatusLine.HTTP_CONTINUE;


public final class LoggerInterceptor implements Interceptor {

  public static final String TAG = "LoggerInterceptor";
  private static final Charset UTF8 = Charset.forName("UTF-8");
  private boolean mLogBody = true;
  private boolean mLogHeaders = true;

  @Override
  public Response intercept(Chain chain) throws IOException {
    StringBuilder requestSb = new StringBuilder();
    Request request = chain.request();

    RequestBody requestBody = request.body();
    boolean hasRequestBody = requestBody != null;
    Connection connection = chain.connection();
    Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
    String requestStartMessage =
        "--> " + request.method() + " Sending request " + request.url().toString() + "\n" + protocol(protocol) + "\n";
    if (!mLogHeaders && hasRequestBody) {
      requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
    }
    requestSb.append(requestStartMessage);


    if (mLogHeaders) {
      if (hasRequestBody) {
        // Request body headers are only present when installed as a network interceptor. Force
        // them to be included (when available) so there values are known.
        if (requestBody.contentType() != null) {
          requestSb.append("Content-Type: " + requestBody.contentType() + "\n");
        }
        if (requestBody.contentLength() != -1) {
          requestSb.append("Content-Length: " + requestBody.contentLength() + "\n");
        }
      }
      Headers headers = request.headers();
      if (headers.size() > 0) {
        requestSb.append("Header-Params: {\n");
        Set<String> names = headers.names();
        for (String name : names) {
          requestSb.append("\t" + name + "   :" + headers.get(name) + "\n");
        }
        requestSb.append("}\n");
      }

      if (!mLogBody || !hasRequestBody) {
        requestSb.append("--> END " + request.method() + "\n");
      } else if (bodyEncoded(headers)) {
        requestSb.append("--> END " + request.method() + " (encoded body omitted)");
      } else {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        String paramStr = buffer.readString(charset);
        StringBuilder sb = new StringBuilder();
        if (request.method().equals("POST")) {
          sb.append(JLog.formatJson(paramStr));
        } else {
          sb.append("{\n");
          String[] params = paramStr.split("&");
          for (String param : params) {
            sb.append("  " + param + ",\n");
          }
          sb.append("}");
        }
        requestSb.append("Request params:" + sb.toString() + "\n");
        requestSb.append("--> END " + request.method()
            + " (" + requestBody.contentLength() + "-byte body)" + "\n");
      }
    }

    long startNs = System.nanoTime();
    Response response = chain.proceed(request);
    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

    ResponseBody responseBody = response.body();
    long contentLength = responseBody.contentLength();
    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
    requestSb.append("<-- " + response.code() + ' ' + response.message() + ' '
        + "Received response for " + response.request().url() + " (" + tookMs + "ms" + (!mLogHeaders ? ", "
        + bodySize + " body" : "") + ')' + "\n");


    StringBuilder responeSb = new StringBuilder();
    if (mLogHeaders) {
      Headers headers = response.headers();
      for (int i = 0, count = headers.size(); i < count; i++) {
        requestSb.append(headers.name(i) + ": " + headers.value(i) + "\n");
      }

      if (!mLogBody || !hasBody(response)) {
        requestSb.append("<-- END HTTP \n");

      } else if (bodyEncoded(response.headers())) {
        requestSb.append("<-- END HTTP (encoded body omitted)\n");
      } else {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        if (contentLength != 0) {

          responeSb.append(buffer.clone().readString(charset));

        }
        requestSb.append("<-- END HTTP (" + buffer.size() + "-byte body)\n");
      }
    }
    JLog.d(TAG, requestSb.toString());
    JLog.json(TAG, responeSb.toString());
    return response;
  }


  public static boolean hasBody(Response response) {
    // HEAD requests never yield a body regardless of the response headers.
    if (response.request().method().equals("HEAD")) {
      return false;
    }

    int responseCode = response.code();
    if ((responseCode < HTTP_CONTINUE || responseCode >= 200)
        && responseCode != HTTP_NO_CONTENT
        && responseCode != HTTP_NOT_MODIFIED) {
      return true;
    }
    if (stringToLong(response.headers().get("Content-Length")) != -1
        || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
      return true;
    }
    return false;
  }

  private static long stringToLong(String s) {
    if (s == null) return -1;
    try {
      return Long.parseLong(s);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  private boolean bodyEncoded(Headers headers) {
    String contentEncoding = headers.get("Content-Encoding");
    return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
  }

  private static String protocol(Protocol protocol) {
    return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
  }


}
