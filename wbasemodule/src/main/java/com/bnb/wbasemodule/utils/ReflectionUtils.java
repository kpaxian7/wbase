package com.bnb.wbasemodule.utils;

import android.support.v4.app.FragmentActivity;

import java.lang.reflect.Field;

public class ReflectionUtils {

    private static String sFragmentActivityFragmentsTag = null;

    /**
     * 获取FragmentActivity onSaveInstanceState Bundle key
     */
    public static String getFragmentsTag() {
        if (sFragmentActivityFragmentsTag == null) {
            try {
                Class<?> threadClazz = Class.forName("android.support.v4.app.FragmentActivity");
                Field field = threadClazz.getDeclaredField("FRAGMENTS_TAG");
                field.setAccessible(true);
                sFragmentActivityFragmentsTag = (String) field.get(FragmentActivity.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sFragmentActivityFragmentsTag;
    }

}
