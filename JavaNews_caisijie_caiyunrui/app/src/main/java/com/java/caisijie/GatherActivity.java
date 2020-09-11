package com.java.caisijie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.java.caisijie.ui.GatherFragment;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import Backend.APPEvent;

public class GatherActivity extends AppCompatActivity {
    JSONArray gather;
    ArrayList<String> types;
    ArrayList<Fragment> fragments;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPagerAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);
        System.out.println("begin");
        InputStream inputStream = getResources().openRawResource(R.raw.tmp);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        try {
            gather = new JSONArray(in.readLine());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("read in");
        types = new ArrayList<>();
        fragments = new ArrayList<>();
        tabLayout = (TabLayout)findViewById(R.id.tabPeople);
        viewPager = (ViewPager)findViewById(R.id.peoplePager);
        if(gather != null){
            int len = gather.length();
            for(int i = 0; i < len; i++){
                System.out.println("i: "+i);
                try {
                    JSONObject group = gather.getJSONObject(i);
                    types.add(group.getString("word"));
                    JSONArray events = group.getJSONArray("group");
                    int tmpLen = events.length();
                    ArrayList<APPEvent> tmp = new ArrayList<>();
                    for(int j = 0; j < tmpLen; j++){
                        tmp.add(new APPEvent(events.getJSONObject(j)));
                    }
                    fragments.add(new GatherFragment(tmp));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        adapter = new GatherActivity.MyAdapter(getSupportFragmentManager(), types, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager,true);
        adapter.notifyDataSetChanged();
    }

    public class MyAdapter extends FragmentPagerAdapter {
        List<String> list;
        List<Fragment> fragments;
        public MyAdapter(@NonNull FragmentManager fm, List<String> list, List<Fragment> fragments) {
            super(fm);
            this.list = list;
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return fragments != null ? fragments.size() : 0;
        }
    };
}
