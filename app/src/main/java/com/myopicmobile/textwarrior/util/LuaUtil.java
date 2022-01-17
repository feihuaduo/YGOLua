package com.myopicmobile.textwarrior.util;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.ourygo.ygolua.LuaApplication;

/**
 * Create By feihua  On 2022/1/16
 */
public class LuaUtil {
    public static int c(int color){
        return ContextCompat.getColor(LuaApplication.getContext(),color);
    }
}
