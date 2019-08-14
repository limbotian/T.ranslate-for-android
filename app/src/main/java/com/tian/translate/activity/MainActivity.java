package com.tian.translate.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tian.translate.MyApplication;
import com.tian.translate.R;
import com.tian.translate.adapter.LanguageChooseItemAdapter;
import com.tian.translate.adapter.TextItemAdapter;
import com.tian.translate.model.Language;
import com.tian.translate.model.TextItem;
import com.tian.translate.model.TranslateHttpSendParm;
import com.tian.translate.myInterface.ChangeItemStatus;
import com.tian.translate.myInterface.RefreshLanguage;
import com.tian.translate.utils.DataUtils;
import com.tian.translate.utils.GetTranslateResult;
import com.tian.translate.utils.SharedPreferencesUtil;
import com.tian.translate.utils.SpacesItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<TextItem> listViews = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private DrawerLayout drawer;
    private TextItemAdapter myListViewAdapter;
    private LanguageChooseItemAdapter languageChooseFromItemAdapter;
    private LanguageChooseItemAdapter languageChooseToItemAdapter;
    private EditText contentText;
    private TextItem translate;
    private List<Language> languageFromList = new ArrayList<>();
    private List<Language> languageToList = new ArrayList<>();
    private Language currentFromLanguage;
    private Language currentToLanguage;
    private RefreshLanguage refreshLanguage = new RefreshLanguage() {
        @Override
        public void refreshActivity(Language language) {
            if (language.getType() == 1) {
                currentFromLanguage = language;
                Button button = findViewById(R.id.language_from);
                button.setText(language.getName());
                SharedPreferencesUtil.putData("currentFromLanguage", currentFromLanguage.getCode());
            } else {
                currentToLanguage = language;
                Button button = findViewById(R.id.language_to);
                button.setText(language.getName());
                SharedPreferencesUtil.putData("currentToLanguage", currentToLanguage.getCode());
            }
            languageChooseDialog.dismiss();
        }
    };

    private ChangeItemStatus changeItemStatus = new ChangeItemStatus() {
        @Override
        public void chooseItem(TextItem textItem) {

        }

        @Override
        public void ItemFavorites(TextItem textItem) {
            initListItems();
            if (vibrator != null){
                vibrator.vibrate(30);
                initListItems();
            }
        }

        @Override
        public void deleteItem(TextItem textItem) {
            textItem.delete();
            if (vibrator != null){
                vibrator.vibrate(50);
                initListItems();
            }
        }

        @Override
        public void toDetail(TextItem textItem) {
            DetailActivity.startDetailActivity(MainActivity.this,textItem);
        }
    };
    private AlertDialog languageChooseDialog;
    private Vibrator vibrator;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addActivity(this);
        requestPermissions();
        //检查权限
        if (hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            initListItems();
            initLanguage();
            initSharedPreferences();
        }
        if (hasPermission(this, Manifest.permission.VIBRATE)) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.CENTER);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    getTranslate();
                }
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        myListViewAdapter = new TextItemAdapter(listViews, changeItemStatus);
        recyclerView.setAdapter(myListViewAdapter);
        contentText = (EditText) findViewById(R.id.content_text);
        Button languageFrom = findViewById(R.id.language_from);
        languageFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.language_choose_layout, null);
                languageChooseDialog = new AlertDialog.Builder(view.getContext()).setView(dialogView).create();
                languageChooseDialog.setCancelable(true);
                RecyclerView recyclerView = dialogView.findViewById(R.id.language_choose_list);
                recyclerView.addItemDecoration(new SpacesItemDecoration(5));
                LinearLayoutManager manager = new LinearLayoutManager(languageChooseDialog.getContext());
                recyclerView.setLayoutManager(manager);
                languageChooseFromItemAdapter = new LanguageChooseItemAdapter(languageFromList, refreshLanguage);
                recyclerView.setAdapter(languageChooseFromItemAdapter);
                languageChooseDialog.show();
            }
        });

        Button languageTo = findViewById(R.id.language_to);
        languageTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.language_choose_layout, null);
                languageChooseDialog = new AlertDialog.Builder(view.getContext()).setView(dialogView).create();
                languageChooseDialog.setCancelable(true);
                RecyclerView recyclerView = dialogView.findViewById(R.id.language_choose_list);
                recyclerView.addItemDecoration(new SpacesItemDecoration(5));
                LinearLayoutManager manager = new LinearLayoutManager(languageChooseDialog.getContext());
                recyclerView.setLayoutManager(manager);
                languageChooseFromItemAdapter = new LanguageChooseItemAdapter(languageToList, refreshLanguage);
                recyclerView.setAdapter(languageChooseFromItemAdapter);
                languageChooseDialog.show();
            }
        });
    }

    private void initSharedPreferences() {
        if ("none".equals(SharedPreferencesUtil.getData("currentFromLanguage", "none"))) {
            SharedPreferencesUtil.putData("currentFromLanguage", "auto");
            List<Language> auto = LitePal.where("code = ? and type = ?", "auto", "1").find(Language.class);
            currentFromLanguage = auto.get(0);
            Button button = findViewById(R.id.language_from);
            button.setText(currentFromLanguage.getName());
        } else {
            List<Language> auto = LitePal.where("code = ? and type = ?", (String) SharedPreferencesUtil.getData("currentFromLanguage", "auto"), "1").find(Language.class);
            currentFromLanguage = auto.get(0);
            Button button = findViewById(R.id.language_from);
            button.setText(currentFromLanguage.getName());
        }
        if ("none".equals(SharedPreferencesUtil.getData("currentToLanguage", "none"))) {
            SharedPreferencesUtil.putData("currentToLanguage", "en");
            List<Language> auto = LitePal.where("code = ? and type = ?", "en", "1").find(Language.class);
            currentToLanguage = auto.get(0);
            Button button = findViewById(R.id.language_to);
            button.setText(currentToLanguage.getName());
        } else {
            List<Language> en = LitePal.where("code = ? and type = ?", (String) SharedPreferencesUtil.getData("currentToLanguage", "en"), "2").find(Language.class);
            currentToLanguage = en.get(0);
            Button button = findViewById(R.id.language_to);
            button.setText(currentToLanguage.getName());
        }
    }

    private void initLanguage() {
        languageFromList.addAll(LitePal.where("type = ?", "1").find(Language.class));
        languageToList.addAll(LitePal.where("type = ?", "2").find(Language.class));
        if (languageFromList.size() == 0 || languageToList.size() == 0) {
            DataUtils.initializeData();
            languageFromList.addAll(LitePal.where("type = ?", "1").find(Language.class));
            languageToList.addAll(LitePal.where("type = ?", "2").find(Language.class));
        }
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initListItems();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (escFlag == 0) {
                showShortToast(getString(R.string.pass_again_back_to_main_activity));
                escFlag++;
                reBackFlag();
            } else {
                finishAll();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawers();
        if (id == R.id.nav_home) {
            startMainActivity(this);
        } else if (id == R.id.nav_star) {
            startActivityForResult(new Intent(MainActivity.this, FavoritesActivity.class), 1);
        } else if (id == R.id.nav_setting) {
            SettingActivity.startSettingActivity(this);
        } else if (id == R.id.nav_share) {
            showShortToast("KKK");
        } else if (id == R.id.nav_about) {
            AboutActivity.startAboutActivity(this);
        }

        return true;
    }

    private void initListItems() {
        listViews.clear();
        listViews.addAll(LitePal.findAll(TextItem.class));
        Collections.reverse(listViews);
        if (myListViewAdapter!=null){
            myListViewAdapter.notifyDataSetChanged();
        }
    }

    private void getTranslate() {
        if (isNetworkConnected(MainActivity.this)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    TranslateHttpSendParm parm = new TranslateHttpSendParm(contentText.getText().toString(), currentFromLanguage == null ? (String) SharedPreferencesUtil.getData("currentFromLanguage", "auto") : currentFromLanguage.getCode(), currentToLanguage == null ? (String) SharedPreferencesUtil.getData("currentToLanguage", "en") : currentToLanguage.getCode());
                    translate = GetTranslateResult.translate(parm);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (translate.isError()) {
                                showLongToast(translate.getErrorMsg());
                            } else {
                                translate.setCreateDate(new Date());
                                translate.save();
                                initListItems();
                            }
                        }
                    });
                }
            }).start();
        }else{
            showLongToast(getText(R.string.no_network).toString());
        }
    }

    public static void startMainActivity(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }


    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        rxPermission
                .requestEach(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.INTERNET

                )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d("checkPermission", permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            Log.d("checkPermission", permission.name + " is denied. More info should be provided.");
                            finishAll();
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                            Log.d("checkPermission", permission.name + " is denied.");
                            finishAll();
                        }
                    }
                });
    }

    public static boolean hasPermission(Context context, String permission) {
        int perm = context.checkCallingOrSelfPermission(permission);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    initListItems();
                }
                break;
            default:
        }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
