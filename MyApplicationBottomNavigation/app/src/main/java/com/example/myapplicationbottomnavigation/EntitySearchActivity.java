package com.example.myapplicationbottomnavigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

import Backend.APPEntity;
import Backend.APPNetEntity;

public class EntitySearchActivity extends Activity {
    private ArrayList<APPEntity> searchResult;
    private SearchView searchView;
    private APPNetEntity entityService;
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entity_search);
        initSearch();
    }

    private void initSearch(){
        searchResult = new ArrayList<>();
        searchView = findViewById(R.id.searchView);
        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.searchResult);
        searchResult = new ArrayList<>();
//        searchView.setOnSearchClickListener(new SearchView.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                adapter = new ArrayAdapter<String>(EntitySearchActivity.this,R.layout.searchhistory_show,searchHistory);
////                searchHistoryView.setTextFilterEnabled(true);
////                searchHistoryView.setVisibility(View.VISIBLE);
//                scrollView.setVisibility(View.VISIBLE);
//                scrollView.bringToFront();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (searchHistory){
//                            searchHistory.clear();
//                            searchHistory.addAll(eventsService.getSearchHistory());
//                        }
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                linearLayout.removeAllViews();
//                                for(String tmpString: searchHistory){
//                                    final TextView tmpText = (TextView) getLayoutInflater().inflate(R.layout.searchhistory_show,null);
//                                    tmpText.setText(tmpString);
//                                    linearLayout.addView(tmpText);
//                                    tmpText.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            searchView.setQuery(tmpText.getText(),true);
//                                        }
//                                    });
//                                }
//                            }
//                        });
//                    }
//                }).start();
//            }
//        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 当点击搜索按钮时触发该方法


            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s)){
                    System.out.println("request");
                    searchView.setQuery("",false);
                    searchView.setFocusable(false);
                    searchView.clearFocus();
                    entityService = new APPNetEntity(s);
                    search(s);
                }
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)){
//                    searchHistoryView.setFilterText(newText);
//                }else{
//                    searchHistoryView.clearTextFilter();
//                }
                return false;
            }
        });
    }

    private void search(String s){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (searchResult){
                    searchResult.clear();
                    searchResult.addAll(entityService.getResult());
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                    linearLayout.removeAllViews();
                    for(APPEntity tmpEntity:searchResult){
                        final TextView tmpText = (TextView) getLayoutInflater().inflate(R.layout.searchhistory_show,null);
                        tmpText.setText(tmpEntity.getLabel());
                        tmpText.setBackground(getDrawable(R.drawable.underline));
                        tmpText.setTag(tmpEntity);
                        linearLayout.addView(tmpText);
                        tmpText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                APPEntity tmp = (APPEntity) view.getTag();
                                Intent intent = new Intent(EntitySearchActivity.this, EntityActivity.class);
                                intent.putExtra("entityJson",tmp.getJson());
                                startActivity(intent);
                            }
                        });
                    }
                    }
                });
            }
        }).start();
    }
}
