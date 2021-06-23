package com.cent.testchart.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.enums.Anchor;
import com.anychart.enums.HAlign;
import com.cent.testchart.App;
import com.cent.testchart.R;

import java.io.File;
import java.text.DecimalFormat;

import static java.lang.Thread.sleep;

public class smokeFragment extends Fragment {

    private View container;

    public smokeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.container = view;
        init_();
    }
    com.anychart.charts.CircularGauge cg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_smoke, container, false);
    }
    private void init_() {

        Any_Chart_co2();

    }

    View hidden_v;
    CardView card;
    AnyChartView anyChartView;


    private void Any_Chart_co2() {
        anyChartView =  (AnyChartView) container.findViewById(R.id.any_chart_view_smoke);
        hidden_v = container.findViewById(R.id.hidden_v);
        card = container.findViewById(R.id.card_chart_smoke);
//        int height =  (int) (anyChartView.getLayoutParams().height) - 100;
//        hidden_v.getLayoutParams().height = height;
//        Toast.makeText(container.getContext(),  container.getLayoutParams().height + "" , Toast.LENGTH_LONG).show();
        cg = AnyChart.circular();
        cg.removeAllPointers();
        anyChartView.refreshDrawableState();
        settingUpCircularGauge();
        anyChartView.setChart(cg);
        anyChartView.setVisibility(View.VISIBLE);
        changeGraph();

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
                    addEntry();


                }
            }

        }).start();

    }


    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private void addEntry() {
        double currentValue = App.amount_smoke;
       
            cg.data((new com.anychart.chart.common.dataentry.SingleValueDataSet(new Double[]{currentValue})));
            cg.label(1)
                    .text("<span style=\"font-size: 20\">" + df2.format(currentValue) + "</span>")
                    .useHtml(true)
                    .hAlign(com.anychart.graphics.vector.text.HAlign.CENTER);
            cg.label(1)
                    .anchor(String.valueOf(Anchor.CENTER_TOP))
                    .offsetY(-150)
                    .offsetX(8)
                    .padding(5, 10, 0, 0)
                    .background("{fill: 'none', stroke: '#fff', corners: 3, cornerType: 'ROUND'}");

    }

    private void settingUpCircularGauge(){
        cg = AnyChart.circular();
        cg.fill("#fff")
                .stroke(null)
                .padding(0, 0, 0, 0)
                .margin(20,0,0,0);
        cg.startAngle(0)
                .sweepAngle(360);

        double currentValue = 13.8D;
        cg.data(new SingleValueDataSet(new Double[] { currentValue }));

        cg.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{ type: 'line', length: 4, position: 'outside' }");

        cg.axis(0).labels().position("outside");

        cg.axis(0).scale()
                .minimum(0)
                .maximum(300);

        cg.axis(0).scale()
                .ticks("{interval: 10}")
                .minorTicks("{interval: 10}");

        cg.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("50%")
                .startWidth("2%")
                .endWidth(0);

        cg.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        cg.label(0)
                .text("<span style=\"font-size: "+ 0 + "\">~</span>")
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER));
        cg.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);


        cg.range(0,
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

        cg.range(1,
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