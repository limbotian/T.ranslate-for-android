package com.tian.translate;

import android.app.Application;
import android.content.Context;

import com.tian.translate.utils.SharedPreferencesUtil;

import org.litepal.LitePal;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
        SharedPreferencesUtil.getInstance(context,"data");
    }

    public static Context getContext(){
        return context;
    }
}
