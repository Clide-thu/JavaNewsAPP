package com.java.caisijie.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.java.caisijie.LabelActivity;
import com.java.caisijie.R;
import com.java.caisijie.ui.refresh.RefreshFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeFragment extends Fragment {

    private ArrayList<String> types = new ArrayList<>();
    private ArrayList<RefreshFragment> showFragments = new ArrayList<>();
    private FragmentPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == 100){
            System.out.println("homerefresh");
            refreshLabel();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public class MyAdapter extends FragmentPagerAdapter{
        List<String> list;
        List<RefreshFragment> fragments;
        public MyAdapter(@NonNull FragmentManager fm,List<String> list, List<RefreshFragment> fragments) {
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

    public synchronized void refreshLabel(){
//        System.out.println("refreshLabel");
        SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        showFragments.clear();
        types.clear();
        String label = sharedPreferences.getString("label","news paper");
        Scanner scanner = new Scanner(label);
        while (scanner.hasNext()){
            String tmpLabel = scanner.next();
            showFragments.add(new RefreshFragment(tmpLabel));
            types.add(tmpLabel);
        }
        adapter.notifyDataSetChanged();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,null);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        adapter = new MyAdapter(getChildFragmentManager(), types, showFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager,true);
        view.findViewById(R.id.tabButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), LabelActivity.class),100);
            }
        });
        refreshLabel();
        return view;
    }
}