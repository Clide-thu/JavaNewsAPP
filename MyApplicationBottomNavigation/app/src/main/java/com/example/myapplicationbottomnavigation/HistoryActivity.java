package com.example.myapplicationbottomnavigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Backend.APPEvent;
import Backend.APPNetEvents;

public class HistoryActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private ArrayList<String> titleList;
    private ArrayList<APPEvent> events;
    private APPNetEvents eventService;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_show);
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
                ArrayList<APPEvent> tmpList = eventService.getEventRecord();
                if(tmpList == null){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(HistoryActivity.this,"database error",Toast.LENGTH_SHORT).show();
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
                        titleList.add(tmp);
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.removeAllViews();
                        int tmpLen = titleList.size();
                        for(int i = 0; i < tmpLen; i++){
                            String tmpString = titleList.get(i);
                            TextView tmpView = (TextView) HistoryActivity.this.getLayoutInflater().inflate(R.layout.searchhistory_show,null);
                            tmpView.setText(tmpString);
                            linearLayout.addView(tmpView);
                            tmpView.setTag(events.get(i));
                            tmpView.setBackground(getDrawable(R.drawable.underline));
                            tmpView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    APPEvent tmpEvent = (APPEvent) view.getTag();
                                    Intent intent = new Intent(HistoryActivity.this, NewsActivity.class);
                                    intent.putExtra("_id",tmpEvent.get_id());
                                    startActivity(intent);
                                }
                            });
                        }
                        if(linearLayout.getChildCount()==0){
                            TextView tmpView = (TextView) HistoryActivity.this.getLayoutInflater().inflate(R.layout.searchhistory_show,null);
                            tmpView.setText("No data!");
                            linearLayout.addView(tmpView);
                        }
                    }
                });

            }
        }).start();

    }
}
