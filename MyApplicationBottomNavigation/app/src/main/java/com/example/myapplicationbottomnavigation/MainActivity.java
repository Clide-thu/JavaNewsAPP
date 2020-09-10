package com.example.myapplicationbottomnavigation;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.w3c.dom.Text;

import java.util.ArrayList;

import Backend.APPEvent;
import Backend.APPNetEvents;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> searchHistory;
    private SearchView searchView;
    private ArrayAdapter<String> adapter;
    private APPNetEvents eventsService;
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        initSearch();
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
                adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.searchhistory_show,searchHistory);
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

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                System.out.println(("change"));
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
//                searchHistoryView.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
                searchHistory.clear();
                adapter.notifyDataSetChanged();
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

}