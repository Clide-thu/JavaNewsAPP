package com.example.myapplicationbottomnavigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import Backend.APPEvent;
import Backend.APPNetEvents;

public class SearchActivity extends Activity {
    private String keyword;
    private ScrollView scrollView;
    private ArrayList<SpannableString> titleList;
    private ArrayList<APPEvent> events;
    private APPNetEvents eventService;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_show);
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        scrollView = findViewById(R.id.search_show);
        titleList = new ArrayList<>();
        events = new ArrayList<>();
        eventService = new APPNetEvents(this);
        linearLayout = findViewById(R.id.linearLayout);
        getSearch();
    }

    private void getSearch(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            ArrayList<APPEvent> tmpList = eventService.search(keyword);
            if(tmpList == null){
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"network error",Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            synchronized (events){
                events.clear();
                titleList.clear();
                events.addAll(tmpList);
                for(APPEvent event:events){
                    int t = 0;
                    String tmp = event.getTitle();
                    System.out.println(tmp);
                    SpannableString modified = new SpannableString(tmp);
                    while (tmp.contains(keyword)){
                        int position = tmp.indexOf(keyword);
                        modified.setSpan(new ForegroundColorSpan(Color.RED),t+position,t+position+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tmp = tmp.substring(position+keyword.length());
                        t+=position+keyword.length();
                    }
                    titleList.add(modified);
                }
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    linearLayout.removeAllViews();
                    int tmpLen = titleList.size();
                    for(int i = 0; i < tmpLen; i++){
                        SpannableString tmpString = titleList.get(i);
                        TextView tmpView = (TextView) SearchActivity.this.getLayoutInflater().inflate(R.layout.searchhistory_show,null);
                        tmpView.setText(tmpString);
                        linearLayout.addView(tmpView);
                        tmpView.setTag(events.get(i));
                            tmpView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    APPEvent tmpEvent = (APPEvent) view.getTag();
                                    Intent intent = new Intent(SearchActivity.this, NewsActivity.class);
                                    intent.putExtra("_id",tmpEvent.get_id());
                                    startActivity(intent);
                                }
                            });
                    }
                    if(linearLayout.getChildCount()==0){
                        TextView tmpView = (TextView) SearchActivity.this.getLayoutInflater().inflate(R.layout.searchhistory_show,null);
                        tmpView.setText("No data!");
                        linearLayout.addView(tmpView);
                    }
                }
            });

            }
        }).start();

    }

}
