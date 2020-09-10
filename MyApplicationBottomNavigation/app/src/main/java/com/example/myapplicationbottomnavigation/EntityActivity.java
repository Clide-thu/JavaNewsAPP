package com.example.myapplicationbottomnavigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import Backend.APPEntity;
import Backend.APPEntityRelation;
import Backend.APPEvent;

public class EntityActivity extends AppCompatActivity {
    private APPEntity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entity_show);
        try {
            entity = new APPEntity(new JSONObject(getIntent().getStringExtra("entityJson")));
            ((TextView)findViewById(R.id.labelView)).setText(entity.getLabel());
            if(entity.getImgPath() != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = getImageBitmap(entity.getImgPath());
                        if(bitmap != null){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView)findViewById(R.id.imageView)).setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                }).start();
            }
            ((TextView)findViewById(R.id.introView)).setText(entity.getIntroduce());
            LinearLayout linearLayoutP = (LinearLayout)findViewById(R.id.propertyLayout);
            for(Map.Entry<String,String> tmpEntry : entity.getProperties().entrySet()){
                TextView tmpView = (TextView) LayoutInflater.from(this).inflate(R.layout.property_show,null);
                SpannableString tmpSpan = new SpannableString(tmpEntry.getKey()+" "+tmpEntry.getValue());
                tmpSpan.setSpan(new BackgroundColorSpan(0xffff8888), 0 , tmpEntry.getKey().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tmpView.setText(tmpSpan);
                tmpView.setTextColor(0xff000000);
                tmpView.setTextSize(20);
                linearLayoutP.addView(tmpView);
            }
            LinearLayout linearLayoutR = (LinearLayout)findViewById(R.id.relationLayout);
            for(APPEntityRelation relation:entity.getRelations()){
                LinearLayout tmpView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.relation_show,null);
                ((TextView)tmpView.findViewById(R.id.relationView)).setText(relation.getRelation());
                if(relation.getForward()){
                    ((TextView)tmpView.findViewById(R.id.forwardView)).setText("-->");
                }else{
                    ((TextView)tmpView.findViewById(R.id.forwardView)).setText("<--");
                }
                SpannableString tmpSpan = new SpannableString(relation.getLabel());
                tmpSpan.setSpan(new BackgroundColorSpan(0xffff8866), 0 , relation.getLabel().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ((TextView)tmpView.findViewById(R.id.labelView)).setText(tmpSpan);
                linearLayoutR.addView(tmpView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //please run in thread
    private Bitmap getImageBitmap(String url){
        Bitmap bitmap =null ;
        try {
            URL imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return bitmap;
    }
}
