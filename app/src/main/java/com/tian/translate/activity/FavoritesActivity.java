package com.tian.translate.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;

import com.tian.translate.MyApplication;
import com.tian.translate.R;
import com.tian.translate.adapter.TextItemAdapter;
import com.tian.translate.model.TextItem;
import com.tian.translate.myInterface.ChangeItemStatus;
import com.tian.translate.utils.SpacesItemDecoration;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritesActivity extends BaseActivity {
    private Boolean dataChanged = false;
    private List<TextItem> listViews = new ArrayList<>();
    private TextItemAdapter myListViewAdapter;
    private ChangeItemStatus changeItemStatus = new ChangeItemStatus() {
        @Override
        public void chooseItem(TextItem textItem) {

        }
        @Override
        public void ItemFavorites(TextItem textItem) {
            initListItems();
            dataChanged = true;
        }

        @Override
        public void deleteItem(TextItem textItem) {

        }

        @Override
        public void toDetail(TextItem textItem) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        addActivity(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout =  findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.CENTER);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("dataChanged",dataChanged);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        initListItems();
        RecyclerView recyclerView = findViewById(R.id.favorites);
        recyclerView.addItemDecoration(new SpacesItemDecoration(5));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        myListViewAdapter = new TextItemAdapter(listViews,changeItemStatus);
        recyclerView.setAdapter(myListViewAdapter);

    }

    public static void startFavoritesActivity(Context context) {
        context.startActivity(new Intent(context, FavoritesActivity.class));
    }


    private void initListItems() {
        listViews.clear();
        listViews.addAll(LitePal.where("isFavorites = ?","1").find(TextItem.class));
        Collections.reverse(listViews);
        if (myListViewAdapter!=null){
            myListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("dataChanged",dataChanged);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}
