package com.example.myapplicationbottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Backend.APPEvent;
import Backend.APPNetEvents;

public class NewsActivity extends AppCompatActivity {
    private APPNetEvents eventService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newspage);
        eventService = new APPNetEvents(getApplication());
        Intent intent = getIntent();
        final String _id = intent.getStringExtra("_id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final APPEvent event = eventService.getEventBy_id(_id);
                if(event == null){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(NewsActivity.this,"network error",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    return;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.titleView)).setText(event.getTitle());
                        ((TextView)findViewById(R.id.source_authorView)).setText(event.getSourceAuthor());
                        ((TextView)findViewById(R.id.timeView)).setText(event.getDate());
                        ((TextView)findViewById(R.id.contentView)).setText("        "+event.getContent());
                    }
                });
            }
        }).start();
    }
}
