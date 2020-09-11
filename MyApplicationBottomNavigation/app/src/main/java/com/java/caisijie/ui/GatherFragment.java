package com.java.caisijie.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.java.caisijie.R;

import java.util.ArrayList;

import Backend.APPEvent;

public class GatherFragment extends Fragment {
    private ArrayList<APPEvent> events;
    private LinearLayout linearLayout;
    TextView title;
    TextView date;

    public GatherFragment(ArrayList<APPEvent> events){
        super();
        this.events = events;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_people,null);
        linearLayout = (LinearLayout)view.findViewById(R.id.peopleLayout);
        for(APPEvent event : events){
            View tmpView = LayoutInflater.from(getContext()).inflate(R.layout.event_show,null);
            title = (TextView)tmpView.findViewById(R.id.event_title);
            title.setText(event.getTitle());
            title.setTextColor(0xff000000);
            date = (TextView)tmpView.findViewById(R.id.date);
            date.setText(event.getDate());
            date.setTextColor(0xff000000);
            linearLayout.addView(tmpView);
        }
        return view;
    }
}
