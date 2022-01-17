package com.ourygo.ygolua;

import android.app.Application;
import android.content.Context;

/**
 * TODO
 *
 * @author 千年纹 1799426163@qq.com
 * @version 1.0.0
 */
public class LuaApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getContext() {
        return context;
    }
}
