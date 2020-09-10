package com.example.myapplicationbottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
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
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsActivity extends AppCompatActivity {
    private APPNetEvents eventService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newspage);
        eventService = new APPNetEvents(getApplication());
        Intent intent = getIntent();
        final String _id = intent.getStringExtra("_id");
        findViewById(R.id.ret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.share).setVisibility(View.GONE);
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnekeyShare handler = new OnekeyShare();
                handler.setTitle("疫情新闻分享");
                handler.setText(((TextView)findViewById(R.id.titleView)).getText().toString()
                        +"\n       "+((TextView)findViewById(R.id.contentView)).getText().toString());
                handler.show(NewsActivity.this);
            }
        });
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
                        findViewById(R.id.share).setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
}
