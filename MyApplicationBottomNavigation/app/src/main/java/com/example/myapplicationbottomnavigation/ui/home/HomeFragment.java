package com.example.myapplicationbottomnavigation.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplicationbottomnavigation.R;
import com.example.myapplicationbottomnavigation.ui.refresh.MyRefreshLayout;
import com.example.myapplicationbottomnavigation.ui.refresh.RefreshFragment;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HomeFragment extends Fragment {

    private ArrayList<String> types = new ArrayList<>();
    private HashMap<String, RefreshFragment> myFragments = new HashMap<>();
    private ArrayList<RefreshFragment> showFragments = new ArrayList<>();
    private FragmentPagerAdapter adapter;
    private TabLayout tabLayout;

    public HomeFragment() {
        super();
        init();
    }

    public HomeFragment(int contentLayoutId) {
        super(contentLayoutId);
        init();
    }

    private void init(){
        types.add("news");
        types.add("paper");
        myFragments.put("news",new RefreshFragment("news"));
        myFragments.put("paper",new RefreshFragment("paper"));
    }

    public class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return showFragments.get(position);
        }

        @Override
        public int getCount() {
            return showFragments.size();
        }
    };

    public void refreshLabel(){
        System.out.println("refreshLabel");
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        showFragments.clear();
        tabLayout.removeAllTabs();
        for(String tmpLabel:types){
            if(sharedPreferences.getBoolean(tmpLabel,true)){
                showFragments.add(myFragments.get(tmpLabel));
                tabLayout.addTab(tabLayout.newTab().setText(tmpLabel));
            }
        }
        adapter.notifyDataSetChanged();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,null);
        tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        adapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager,false);
        refreshLabel();
        return view;
    }
}