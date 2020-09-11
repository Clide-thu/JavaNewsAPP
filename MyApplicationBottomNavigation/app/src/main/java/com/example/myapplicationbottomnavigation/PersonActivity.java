package com.example.myapplicationbottomnavigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
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
import java.net.URL;
import java.util.Map;

import Backend.APPEntity;
import Backend.APPEntityRelation;
import Backend.APPPerson;

public class PersonActivity extends AppCompatActivity {
    APPPerson person;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_page);
        System.out.println("hh");
        try {
            person = new APPPerson(new JSONObject(getIntent().getStringExtra("person")));
            if(person.getName_zh().equals("")){
                ((TextView)findViewById(R.id.person_Name)).setText(person.getName());
            }else {
                ((TextView)findViewById(R.id.person_Name)).setText(person.getName_zh());
            }
            ((TextView)findViewById(R.id.person_Affiliation)).setText(person.getAffiliation());
            ((TextView)findViewById(R.id.person_Position)).setText(person.getPosition());
            if(!person.getPhotoPath().equals("")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = getImageBitmap(person.getPhotoPath());
                        if(bitmap != null){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView)findViewById(R.id.person_Photo)).setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                }).start();
            }
            ((TextView)findViewById(R.id.person_Intro)).setText("       "+person.getBio());
            ((TextView)findViewById(R.id.person_edu)).setText(person.getEdu());
            ((TextView)findViewById(R.id.person_work)).setText(person.getWork());

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
