package com.example.myapplicationbottomnavigation.ui.notifications;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplicationbottomnavigation.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Backend.APPNetEpidemicData;

public class NotificationsFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private LineChart lineChart; //折线图控件
    private Spinner mProSpinner = null;
    private Spinner mBookSpinner = null;
    ArrayAdapter<String> adapter2=null;
    ArrayList<String> arr1 = new ArrayList<>();
    ArrayList<String> arr2 = new ArrayList<>();
    static int pos1=0;
    static int pos2=0;
    String region="China|Beijing";
    private APPNetEpidemicData dataService;
    View view;

    private void getData(){
        dataService = new APPNetEpidemicData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (arr1){
                    arr1.addAll(dataService.getDataOfChina().keySet());
                }
                synchronized (arr2){
                    arr2.addAll(dataService.getDataOfWorld().keySet());
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        adapter2.notifyDataSetChanged();
                        if(arr1.size()>0){
                            initLineChart(arr1.get(0));
                        }
                    }
                });
            }
        }).start();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mProSpinner = (Spinner) view.findViewById(R.id.spin_one);
        mBookSpinner = (Spinner) view.findViewById(R.id.spin_two);
        String[] cho1={"国内","国外"};

        // 创建ArrayAdapter对象
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, cho1);
        adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, arr1);

        // 为Spinner设置Adapter
        mBookSpinner.setAdapter(adapter2);
        mBookSpinner.setSelection(0,true);
        mProSpinner.setAdapter(adapter1);
        mProSpinner.setSelection(0,true);
        lineChart = view.findViewById(R.id.lc);
        getData();

        mProSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                pos1 = position;
                //System.out.println(pos1);
                if(pos1==0)
                    adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arr1);
                else
                    adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arr2);
                // 设置二级下拉列表的选项内容适配器
                mBookSpinner.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });

        mBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos2 = i;
                if(pos1==0) {
                    region=arr1.get(pos2);
                    initLineChart(region);
                }
                else if(pos1==1) {
                    region=arr2.get(pos2);
                    initLineChart(region);
                }

            }

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值

            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
        //初始化控件
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /**
     * 初始化图表数据
     */
    private void initLineChart(String region){
        lineChart.animateXY(2000, 2000); // 呈现动画
        Description description = new Description();
        System.out.println(region);
        description.setText(region); //自定义描述
        description.setTextColor(Color.BLACK);
        description.setTextSize(30);
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        //legend.setTextColor(Color.WHITE);
        setYAxis();
        setXAxis();
        setData(region);
    }

    /**
     * 设置Y轴数据
     */
    private void setYAxis(){
        YAxis yAxisLeft = lineChart.getAxisLeft();// 左边Y轴
        yAxisLeft.setDrawAxisLine(true); // 绘制Y轴
        yAxisLeft.setDrawLabels(true); // 绘制标签
        yAxisLeft.setGranularity(3f); // 设置间隔尺寸
        //yAxisLeft.setTextColor(Color.WHITE); //设置颜色
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return new Integer((int)value).toString();
            }
        });
        // 右侧Y轴
        lineChart.getAxisRight().setEnabled(false); // 不启用
    }

    /**
     * 设置X轴数据
     */
    private void setXAxis(){
        // X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawAxisLine(false); // 不绘制X轴
        xAxis.setDrawGridLines(false); // 不绘制网格线
        // 模拟X轴标签数据
        final String[] weekStrs = new String[15];
        for(int i= 14; i >= 0; i--){
            weekStrs[i] = new Integer(15-i).toString();
        }
        xAxis.setLabelCount(weekStrs.length); // 设置标签数量
        xAxis.setTextSize(15); // 文本大小为18dp
        xAxis.setGranularity(1); // 设置间隔尺寸
        // 使图表左右留出点空位
        xAxis.setAxisMinimum(0); // 设置X轴最小值
        //设置颜色
        xAxis.setTextColor(Color.BLACK);
        // 设置标签的显示格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weekStrs[(int) value];
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 在底部显示
    }

    /**
     * 填充数据
     */
    private void setData(final String region){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] ys1;
                int[] ys2;
                int[] ys3;
                if(pos1 == 0){
                    ys1 = dataService.getDataOfChina().get(region).getConfirmed();
                    ys2 = dataService.getDataOfChina().get(region).getDead();
                    ys3 = dataService.getDataOfChina().get(region).getCured();
                }else{
                    ys1 = dataService.getDataOfWorld().get(region).getConfirmed();
                    ys2 = dataService.getDataOfWorld().get(region).getDead();
                    ys3 = dataService.getDataOfWorld().get(region).getCured();
                }
                List<Entry> yVals1 = new ArrayList<>();
                List<Entry> yVals2 = new ArrayList<>();
                List<Entry> yVals3 = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    yVals1.add(new Entry(i, ys1[i]));
                    yVals2.add(new Entry(i, ys2[i]));
                    yVals3.add(new Entry(i, ys3[i]));
                }
                // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
                LineDataSet lineDataSet1 = new LineDataSet(yVals1, "确诊人数");
                LineDataSet lineDataSet2 = new LineDataSet(yVals2, "死亡人数");
                LineDataSet lineDataSet3 = new LineDataSet(yVals3, "治愈人数");
                lineDataSet2.setCircleColor(Color.RED); //设置点圆的颜色
                lineDataSet3.setCircleColor(Color.GREEN);//设置点圆的颜色
                lineDataSet1.setCircleRadius(2); //设置点圆的半径
                lineDataSet2.setCircleRadius(2); //设置点圆的半径
                lineDataSet3.setCircleRadius(2); //设置点圆的半径
                lineDataSet1.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
                lineDataSet2.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
                lineDataSet3.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆
                lineDataSet2.setColor(Color.RED); // 设置为红色
                lineDataSet3.setColor(Color.GREEN); // 设置为黑色
                // 值的字体大小为12dp
                lineDataSet1.setValueTextSize(0);
                lineDataSet2.setValueTextSize(0);
                lineDataSet3.setValueTextSize(0);
                //将每一组折线数据集添加到折线数据中
                final LineData lineData = new LineData(lineDataSet1,lineDataSet2,lineDataSet3);
                //设置颜色
                lineData.setValueTextColor(Color.BLACK);
                final int confirmed = ys1[14];
                final int dead = ys2[14];
                final int cured = ys3[14];
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //将折线数据设置给图表
                        lineChart.setData(lineData);
                        ((TextView)view.findViewById(R.id.confirmed)).setText(new Integer(confirmed).toString());
                        ((TextView)view.findViewById(R.id.dead)).setText(new Integer(dead).toString());
                        ((TextView)view.findViewById(R.id.cured)).setText(new Integer(cured).toString());
                    }
                });
            }
        }).start();
    }
}