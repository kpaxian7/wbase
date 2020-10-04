package com.bnb.wbasemodule.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils {

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static List<String> filterNullStrList(List<String> strList) {
        List<String> list = new ArrayList<>();
        for (String pic : strList) {
            if (!TextUtils.isEmpty(pic)) {
                list.add(pic);
            }
        }
        return list;

    }

    public static List<Integer> convertString2IntegerList(List<String> list) {
        if (isEmpty(list)) {
            return null;
        }
        List<Integer> integers = new ArrayList<>();
        for (String s : list) {
            try {
                integers.add(Integer.valueOf(s));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return integers;
    }

    public static boolean exit(Collection<Integer> list, String str) {
        if (ListUtils.isEmpty(list) || android.text.TextUtils.isEmpty(str)) {
            return false;
        }
        for (Integer integer : list) {
            if (str.equals(String.valueOf(integer))) {
                return true;
            }
        }
        return false;
    }


}
