package com.java.caisijie.ui.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.java.caisijie.R;

import java.util.List;

import Backend.APPEvent;

public class APPEventAdapter extends ArrayAdapter<APPEvent> {



    public APPEventAdapter(@NonNull Context context, int resource, @NonNull List<APPEvent> objects) {
        super(context, resource, objects);
    }


    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        APPEvent event = getItem(position);
        ViewHolder viewHolder;
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_show,null);
            viewHolder = new ViewHolder();
            viewHolder.tv1 = view.findViewById(R.id.event_title);
            viewHolder.tv2 = view.findViewById(R.id.source_author);
            viewHolder.tv3 = view.findViewById(R.id.date);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(event.getTitle());
        viewHolder.tv2.setText(event.getSourceAuthor());
        viewHolder.tv3.setText(event.getDate());
        if(event.getWatched()){
            //System.out.println("sssss");
            viewHolder.tv1.setTextColor(0xff888888);
            viewHolder.tv2.setTextColor(0xff888888);
            viewHolder.tv3.setTextColor(0xff888888);
//            viewHolder.tv1.setBackgroundColor(android.R.color.black);
        } else{
            //System.out.println("AAAAAAA");
            viewHolder.tv1.setTextColor(0xff000000);
            viewHolder.tv2.setTextColor(0xff000000);
            viewHolder.tv3.setTextColor(0xff000000);
//            viewHolder.tv1.setBackgroundColor(android.R.color.darker_gray);

        }
        return view;
    }

//辅助类
    class ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }
}
