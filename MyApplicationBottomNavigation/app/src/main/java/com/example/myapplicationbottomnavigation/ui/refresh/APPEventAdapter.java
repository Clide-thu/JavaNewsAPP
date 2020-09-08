package com.example.myapplicationbottomnavigation.ui.refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplicationbottomnavigation.R;

import org.w3c.dom.Text;

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
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv1.setText(event.getTitle());
        if(event.getWatched()){
            //System.out.println("sssss");
            viewHolder.tv1.setTextColor(0xff888888);
//            viewHolder.tv1.setBackgroundColor(android.R.color.black);
        } else{
            //System.out.println("AAAAAAA");
            viewHolder.tv1.setTextColor(0xff000000);
//            viewHolder.tv1.setBackgroundColor(android.R.color.darker_gray);

        }
        return view;
    }

//辅助类
    class ViewHolder{
        TextView tv1;
    }
}
