package com.example.myapplicationbottomnavigation.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplicationbottomnavigation.PersonActivity;
import com.example.myapplicationbottomnavigation.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Backend.APPPerson;

public class PeopleFragment extends Fragment {
    private ArrayList<APPPerson> people;
    private LinearLayout linearLayout;

    public PeopleFragment(ArrayList<APPPerson> people){
        super();
        this.people = people;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_people,null);
        linearLayout = (LinearLayout)view.findViewById(R.id.peopleLayout);
        for(final APPPerson person : people){
            final View tmpView = LayoutInflater.from(getContext()).inflate(R.layout.personcard,null);
            if(person.getName_zh().equals("")){
                ((TextView)tmpView.findViewById(R.id.personName)).setText(person.getName());
            }else {
                ((TextView)tmpView.findViewById(R.id.personName)).setText(person.getName_zh());
            }
            ((TextView)tmpView.findViewById(R.id.personAffiliation)).setText(person.getAffiliation());
            ((TextView)tmpView.findViewById(R.id.personPosition)).setText(person.getPosition());
            if(!person.getPhotoPath().equals("")){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bitmap = getImageBitmap(person.getPhotoPath());
                        if(bitmap != null){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView)tmpView.findViewById(R.id.personPhoto)).setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                }).start();
            }
            linearLayout.addView(tmpView);
            tmpView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    intent.putExtra("person",person.getJson());
                    startActivity(intent);
                }
            });
        }
        return view;
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
