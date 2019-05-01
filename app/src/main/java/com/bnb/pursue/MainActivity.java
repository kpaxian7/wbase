package com.bnb.pursue;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.tv_test).setOnClickListener(v -> {
            MyUpdateHelper helper = new MyUpdateHelper(MainActivity.this);
            helper.startUpdate();
        });
    }

    private void init() {
        String printTxtPath = getApplicationContext().getPackageResourcePath() + "/files/";
        File filesDir = getFilesDir();
        File dataDirectory = Environment.getDataDirectory();

        Log.e("asd", "path -> " + dataDirectory.getAbsolutePath());
    }
}
