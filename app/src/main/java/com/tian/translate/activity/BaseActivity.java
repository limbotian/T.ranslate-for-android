package com.tian.translate.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends AppCompatActivity {
    public static List<Activity> activities = new ArrayList<>();
    public static int escFlag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void showShortToast(String mag) {
        Toast.makeText(this, mag, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String mag) {
        Toast.makeText(this, mag, Toast.LENGTH_LONG).show();
    }

    public static void reBackFlag() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                escFlag = 0;
            }
        }, 2000);
    }



    public void makeSnackbar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
