package com.java.caisijie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LabelActivity extends AppCompatActivity {
    private FlowLayout flowLayout;
    private FlowLayout unflowLayout;
    Intent intent=getIntent();
    final int[] flag = {1,1};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.label_edit);
        List<String> list=new ArrayList<>();
        List<String> list2=new ArrayList<>();
        list2.add("news");list2.add("paper");
        flowLayout = findViewById(R.id.flow);
        unflowLayout = findViewById(R.id.unflow);
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        list.clear();
        String label = sharedPreferences.getString("label","news paper");
        Scanner scanner = new Scanner(label);
        int i = 0;
        while (scanner.hasNext()){
            flag[i++]=0;
            String tmpLabel = scanner.next();
            list.add(tmpLabel);
            list2.remove(tmpLabel);
        }
        list.addAll(list2);

//往容器内添加TextView数据
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (flowLayout != null) {
            flowLayout.removeAllViews();
        }
        if (unflowLayout != null) {
            unflowLayout.removeAllViews();
        }
        TextView tv = new TextView(this);
        layoutParams.setMargins(50, 50, 50, 50);
        tv.setText(list.get(0));
        tv.setTextSize(30);
        tv.setSingleLine();
        tv.setTextColor(Color.BLACK);
        tv.setBackground(getResources().getDrawable(R.drawable.textview));
        tv.setLayoutParams(layoutParams);
        if(flag[0] ==0)
            flowLayout.addView(tv, layoutParams);
        else
            unflowLayout.addView(tv);

        final TextView sv = new TextView(this);
        layoutParams.setMargins(50, 50, 50, 50);
        sv.setText(list.get(1));
        sv.setTextSize(30);
        sv.setSingleLine();
        sv.setTextColor(Color.BLACK);
        sv.setBackground(getResources().getDrawable(R.drawable.textview));

        sv.setLayoutParams(layoutParams);
        if(flag[1] ==0)
            flowLayout.addView(sv, layoutParams);
        else
            unflowLayout.addView(sv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag[0] ==0) {
                    flowLayout.removeView(v);
                    unflowLayout.addView(v);
                }
                else{
                    unflowLayout.removeView(v);
                    flowLayout.addView(v);
                }
                flag[0] =1- flag[0];

            }
        });
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag[1] ==0) {
                    flowLayout.removeView(v);
                    unflowLayout.addView(v);
                }
                else{
                    unflowLayout.removeView(v);
                    flowLayout.addView(v);
                }
                flag[1] =1- flag[1];
                //unflowLayout
            }

        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String label = "";
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                int tmpCount = flowLayout.getChildCount();
                for(int i = 0; i < tmpCount; i++){
                    label += ((TextView)flowLayout.getChildAt(i)).getText()+" ";
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("label",label);
                editor.commit();
                setResult(100);
                finish();
            }
        });
    }

}
