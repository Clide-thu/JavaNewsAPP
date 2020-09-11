package com.example.myapplicationbottomnavigation;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplicationbottomnavigation.R;
import com.example.myapplicationbottomnavigation.ui.PeopleFragment;
import com.example.myapplicationbottomnavigation.ui.refresh.RefreshFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import Backend.APPNetPerson;
import Backend.APPPerson;

public class PeopleActivity extends AppCompatActivity {
    APPNetPerson personService;
    ArrayList<String> types;
    ArrayList<Fragment> fragments;
    ArrayList<APPPerson> alive;
    ArrayList<APPPerson> dead;
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.people);
        personService = new APPNetPerson();
        types = new ArrayList<>();
        fragments = new ArrayList<>();
        alive = new ArrayList<>();
        dead = new ArrayList<>();
        tabLayout = (TabLayout)findViewById(R.id.tabPeople);
        viewPager = (ViewPager)findViewById(R.id.peoplePager);
        adapter = new MyAdapter(getSupportFragmentManager(), types, fragments);
        Toast.makeText(this,"waiting...",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<APPPerson> tmpPeople = personService.getInfo();
                if(tmpPeople == null){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PeopleActivity.this,"network error",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
                types.add("高关注学者"); types.add("追忆学者");
                for(APPPerson tmpPerson:tmpPeople){
                    if(tmpPerson.getIs_passedaway()){
                        dead.add(tmpPerson);
                    }else{
                        alive.add(tmpPerson);
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        fragments.add(new PeopleFragment(alive));
                        fragments.add(new PeopleFragment(dead));
                        System.out.println(dead.size());
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager,true);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public class MyAdapter extends FragmentPagerAdapter{
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
