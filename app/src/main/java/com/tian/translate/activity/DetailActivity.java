package com.tian.translate.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.tian.translate.R;
import com.tian.translate.model.TextItem;
import com.tian.translate.utils.DataUtils;

import org.litepal.LitePal;

import java.util.Collections;

public class DetailActivity extends BaseActivity {
    private Boolean dataChanged = false;
    private FloatingActionButton fab;
    private TextItem currentTextItem;
    private Vibrator vibrator;
    private TextView languageFrom;
    private TextView languageTo;
    private TextView srcText;
    private TextView dstText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        currentTextItem = (TextItem) getIntent().getSerializableExtra(MainActivity.ObjectKey);
        languageFrom = findViewById(R.id.language_from);
        languageTo = findViewById(R.id.language_to);
        dstText = findViewById(R.id.dst_text);
        srcText = findViewById(R.id.src_text);
        if (hasPermission(this, Manifest.permission.VIBRATE)) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("dataChanged", dataChanged);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!currentTextItem.getIsFavorites()) {
                    currentTextItem.setIsFavorites(true);
                    currentTextItem.update(currentTextItem.getId());
                } else {
                    currentTextItem.setIsFavorites(false);
                    currentTextItem.update(currentTextItem.getId());
                }
                dataChanged = true;
                initItem();
                vibrator.vibrate(50);
            }
        });
        initDetailData();
    }

    private void initDetailData() {
        if (currentTextItem != null) {
            if (!currentTextItem.getIsFavorites()) {
                fab.setImageResource(R.drawable.ic_star_border_dark);
            } else {
                fab.setImageResource(R.drawable.ic_yellow_star);
            }
            languageFrom.setText(DataUtils.codeToString(DetailActivity.this, currentTextItem.getLanguageFrom()));
            languageTo.setText(DataUtils.codeToString(DetailActivity.this, currentTextItem.getLanguageTo()));
            srcText.setText(currentTextItem.getSrcText());
            dstText.setText(currentTextItem.getContentText());
        }
    }

    public static void startDetailActivity(Context context, TextItem textItem) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(MainActivity.ObjectKey, textItem);
        context.startActivity(intent);
    }

    public static boolean hasPermission(Context context, String permission) {
        int perm = context.checkCallingOrSelfPermission(permission);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private void initItem() {
        currentTextItem = LitePal.find(TextItem.class, currentTextItem.getId());
        initDetailData();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("dataChanged", dataChanged);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
