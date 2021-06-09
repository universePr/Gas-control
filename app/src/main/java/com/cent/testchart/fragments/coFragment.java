package com.cent.testchart.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.enums.Anchor;
import com.anychart.enums.HAlign;
import com.cent.testchart.App;
import com.cent.testchart.R;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

public class coFragment extends Fragment {

    private View container;

    public coFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.container = view;
        init_();
    }
    com.anychart.charts.CircularGauge circularGauge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_co, container, false);
    }
    private void init_() {

        Any_Chart_co2();

    }

    View hidden_v;
    CardView card;
    AnyChartView anyChartView;


    private void Any_Chart_co2() {
        anyChartView =  (AnyChartView) container.findViewById(R.id.any_chart_view_co2);
        hidden_v = container.findViewById(R.id.hidden_v);
        card = container.findViewById(R.id.card_chart_co2);
//        int height =  (int) (anyChartView.getLayoutParams().height) - 100;
//        hidden_v.getLayoutParams().height = height;
//        Toast.makeText(container.getContext(),  container.getLayoutParams().height + "" , Toast.LENGTH_LONG).show();
        circularGauge = AnyChart.circular();
        settingUpCircularGauge();
        anyChartView.setChart(circularGauge);
        anyChartView.setVisibility(View.VISIBLE);
        changeGraph();

    }


    private float readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void changeGraph() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                while (true) {
                    addEntry();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }


                }
            }

        }).start();

    }


    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private void addEntry() {
        double currentValue = App.amount_co;

        circularGauge.data((new com.anychart.chart.common.dataentry.SingleValueDataSet(new Double[] { currentValue })));
        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + df2.format(currentValue) + "</span>")
                .useHtml(true)
                .hAlign(com.anychart.graphics.vector.text.HAlign.CENTER);
        circularGauge.label(1)
                .anchor(String.valueOf(Anchor.CENTER_TOP))
                .offsetY(-150)
                .offsetX(8)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#fff', corners: 3, cornerType: 'ROUND'}");
    }

    private void settingUpCircularGauge(){
        circularGauge = AnyChart.circular();
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0, 0, 0, 0)
                .margin(20,0,0,0);
        circularGauge.startAngle(0)
                .sweepAngle(360);

        double currentValue = 13.8D;
        circularGauge.data(new SingleValueDataSet(new Double[] { currentValue }));

        circularGauge.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{ type: 'line', length: 4, position: 'outside' }");

        circularGauge.axis(0).labels().position("outside");

        circularGauge.axis(0).scale()
                .minimum(0)
                .maximum(300);

        circularGauge.axis(0).scale()
                .ticks("{interval: 10}")
                .minorTicks("{interval: 10}");

        circularGauge.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("50%")
                .startWidth("2%")
                .endWidth(0);

        circularGauge.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        circularGauge.label(0)
                .text("<span style=\"font-size: "+ 0 + "\">~</span>")
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER));
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);


        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 100,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'green 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        circularGauge.range(1,
                "{\n" +
                        "    from: 200,\n" +
                        "    to: 300,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

    }
}