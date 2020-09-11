package com.example.myapplicationbottomnavigation.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplicationbottomnavigation.EntitySearchActivity;
import com.example.myapplicationbottomnavigation.HistoryActivity;
import com.example.myapplicationbottomnavigation.PeopleActivity;
import com.example.myapplicationbottomnavigation.R;
import com.example.myapplicationbottomnavigation.ui.refresh.MyRefreshLayout;

public class DashboardFragment extends Fragment {

    private CardView history;
    private CardView entitySearch;
    private CardView peopleShow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        history = view.findViewById(R.id.history);
        entitySearch = view.findViewById(R.id.entitySearch);
        peopleShow = view.findViewById(R.id.peopleShow);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HistoryActivity.class));
            }
        });
        entitySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EntitySearchActivity.class));
            }
        });
        peopleShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PeopleActivity.class));
            }
        });
        return view;
    }
}