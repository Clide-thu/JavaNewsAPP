package com.java.caisijie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.java.caisijie.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mob.MobSDK;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

import Backend.APPNetEvents;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> searchHistory;
    private SearchView searchView;
    private APPNetEvents eventsService;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobSDK.submitPolicyGrantResult(true,null);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        initSearch();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            System.out.println("mainrefresh");
            navController.navigate(R.id.navigation_home);
        }
    }

    private void initSearch(){
        searchHistory = new ArrayList<>();
        searchView = findViewById(R.id.searchView);
        eventsService = new APPNetEvents(this);
        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.searchHistory);
        searchView.setOnSearchClickListener(new SearchView.OnClickListener(){
            @Override
            public void onClick(View view) {
//                searchHistoryView.setTextFilterEnabled(true);
//                searchHistoryView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                scrollView.bringToFront();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (searchHistory){
                            searchHistory.clear();
                            searchHistory.addAll(eventsService.getSearchHistory());
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                linearLayout.removeAllViews();
                                for(String tmpString: searchHistory){
                                    final TextView tmpText = (TextView) getLayoutInflater().inflate(R.layout.searchhistory_show,null);
                                    tmpText.setText(tmpString);
                                    tmpText.setBackground(getDrawable(R.drawable.underline));
                                    linearLayout.addView(tmpText);
                                    tmpText.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            searchView.setQuery(tmpText.getText(),true);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }).start();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                searchHistoryView.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                searchHistory.clear();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            // 当点击搜索按钮时触发该方法


            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s)){
                    System.out.println("request");
                    searchView.setQuery("",false);
                    searchView.setFocusable(false);
                    searchView.clearFocus();

                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("keyword",s);
                    startActivity(intent);
                }

                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText)){
//                    searchHistoryView.setFilterText(newText);
//                }else{
//                    searchHistoryView.clearTextFilter();
//                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    private void refresh(){
        HomeFragment homeFragment = (HomeFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_home);
        if(homeFragment != null){
            homeFragment.refreshLabel();
            System.out.println("refreshLabel");
        }
    }
}