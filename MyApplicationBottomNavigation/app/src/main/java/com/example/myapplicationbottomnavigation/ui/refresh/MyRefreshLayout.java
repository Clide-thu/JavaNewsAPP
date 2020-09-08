package com.example.myapplicationbottomnavigation.ui.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplicationbottomnavigation.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import Backend.APPEvent;
import Backend.APPNetEvents;

public class MyRefreshLayout extends SwipeRefreshLayout implements ListView.OnScrollListener {
    private String type;
    private ArrayList<APPEvent> events = new ArrayList<>();
    private ListView listView;
    private APPNetEvents eventService = new APPNetEvents(getContext());
    private APPEventAdapter adapter;

    public MyRefreshLayout(@NonNull Context context, String type) {
        super(context);
        this.type = type;
        adapter = new APPEventAdapter(getContext(), R.layout.event_show, events);
        inflate(context, R.layout.myrefresh_layout, this);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                APPEvent tmpEvent = events.get(i);
                tmpEvent.setWatched();
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(),tmpEvent.get_id(),Toast.LENGTH_SHORT).show();
            }
        });
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                synchronized (MyRefreshLayout.this){
                    Toast.makeText(getContext(),"refresh",Toast.LENGTH_SHORT).show();
                    eventService = new APPNetEvents(getContext());
                    events.clear();
                    loadMore();
                    setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if(listView.getLastVisiblePosition()==listView.getAdapter().getCount()-1){
            Toast.makeText(getContext(),"load more",Toast.LENGTH_SHORT).show();
            loadMore();
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        setEnabled(listView.getFirstVisiblePosition()==0);
    }

    private void loadMore(){
        synchronized(this){
            new Thread(){
                @Override
                public void run() {
                    final ArrayList<APPEvent> tmpList = eventService.getMore(type);
                    if(tmpList == null || tmpList.size()==0){
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"network error",Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    synchronized (events){
                        events.addAll(tmpList);
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            System.out.println("start");
//                        adapter.addAll(tmpList);
                            adapter.notifyDataSetChanged();
                            System.out.println("end");

                        }
                    });

                }
            }.start();
        }

    }


}
