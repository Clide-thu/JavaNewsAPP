package com.java.caisijie.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.java.caisijie.EntitySearchActivity;
import com.java.caisijie.GatherActivity;
import com.java.caisijie.HistoryActivity;
import com.java.caisijie.PeopleActivity;
import com.java.caisijie.R;

public class DashboardFragment extends Fragment {

    private CardView history;
    private CardView entitySearch;
    private CardView peopleShow;
    private CardView gather;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        history = view.findViewById(R.id.history);
        entitySearch = view.findViewById(R.id.entitySearch);
        peopleShow = view.findViewById(R.id.peopleShow);
        gather = view.findViewById(R.id.gather);
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
        gather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GatherActivity.class));
            }
        });
        return view;
    }
}