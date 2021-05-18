package com.cent.testchart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

//
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.Viewport;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//import com.anychart.AnyChartView;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.*;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.enums.Anchor;
import com.anychart.enums.HAlign;

public class StatisticsFragment extends Fragment {

//    private LineGraphSeries<DataPoint> series;
//    private int lastX = 0;
//    private Viewport viewport ;
//    private GraphView graph;
    private DrawerLayout dLayout;
    private View container;
//    private Button btn;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.statistic_layout, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.container = view;
        init_();

    }
    com.anychart.charts.CircularGauge circularGauge;
//    CircularGauge circularGauge2;
    double currentValue =  0;
    private void init_() {
//        CustomChart();
        Any_Chart();

/*
        graph = (GraphView) container.findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.addSeries(series);
        viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);
        graph.setTitle("CPU Graph test");
        viewport.setBorderColor(Color.DKGRAY);
 */

//        btn  = container.findViewById(R.id.btn);

//        setClickListener();
    }

    private void Any_Chart() {
        AnyChartView anyChartView = container.findViewById(R.id.any_chart_view);
        View hidden_v = container.findViewById(R.id.hidden_v);
        CardView card = container.findViewById(R.id.card_chart);
        hidden_v.getLayoutParams().height = (int) ((0.1) * card.getLayoutParams().height);


//        hidden_v.getLayoutParams().height = 200;
//        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

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
                .maximum(140);

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

        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + currentValue + "</span>")
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER));
        circularGauge.label(1)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(-100)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");

        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 25,\n" +
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
                        "    from: 80,\n" +
                        "    to: 140,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        anyChartView.setChart(circularGauge);

    }

    private void CustomChart() {
//        Any anyChartView = container.findViewById(R.id.any_chart_view);
/*
        circularGauge = AnyChart.circularGauge();

        circularGauge.credits(true);

        circularGauge.fill("#36e364")
                .stroke(null)
                .padding(0, 0, 0, 0)
                .margin(30, 30, 30, 30);
        circularGauge.startAngle(0)
                .sweepAngle(360);


        circularGauge.axis(0)
                .startAngle(-150)
                .radius(80)
                .sweepAngle(300)
                .width(3)
                .ticks("{ type: 'line', length: 4, position: 'outside' }");

        circularGauge.axis(0).labels().position("outside");

        circularGauge.axis(0).scale()
                .minimum(0)
                .maximum(140);

        circularGauge.axis(0).scale()
                .ticks("{interval: 10}")
                .minorTicks("{interval: 10}");

        circularGauge.needle(0)
                .stroke(null)
                .startRadius("6%")
                .endRadius("38%")
                .startWidth("2%")
                .endWidth(0);

        circularGauge.cap()
                .radius("4%")
                .enabled(true)
                .stroke(null);

        circularGauge.label(0)
                .text("<span style=\"font-size: 25\">Wind Speed</span>")
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER));
        circularGauge.label(0)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(100)
                .padding(15, 20, 0, 0);

        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + currentValue + "</span>")
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER));
        circularGauge.label(1)
                .anchor(Anchor.CENTER_TOP)
                .offsetY(-100)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");

        circularGauge.range(0,
                "{\n" +
                        "    from: 0,\n" +
                        "    to: 25,\n" +
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
                        "    from: 80,\n" +
                        "    to: 140,\n" +
                        "    position: 'inside',\n" +
                        "    fill: 'red 0.5',\n" +
                        "    stroke: '1 #000',\n" +
                        "    startSize: 6,\n" +
                        "    endSize: 6,\n" +
                        "    radius: 80,\n" +
                        "    zIndex: 1\n" +
                        "  }");

        circularGauge.draw(true);


        anyChartView.setChart(circularGauge);
        */

    }
//
//    private void setClickListener() {
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(container.getContext(), "In Statistic fragment", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

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
        changeGraph();




    }

    public void changeGraph() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                while (true) {
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            addEntry();
//                        }
//                    });
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
    // add random data to graph
    private void addEntry() {
        double currentValue = readUsage() * 100;
        circularGauge.data((new com.anychart.chart.common.dataentry.SingleValueDataSet(new Double[] { currentValue })));
        circularGauge.label(1)
                .text("<span style=\"font-size: 20\">" + (int)(currentValue) + "</span>")
                .useHtml(true)
                .hAlign(String.valueOf(HAlign.CENTER));
        circularGauge.label(1)
                .anchor(String.valueOf(Anchor.CENTER_TOP))
                .offsetY(-100)
                .padding(5, 10, 0, 0)
                .background("{fill: 'none', stroke: '#c1c1c1', corners: 3, cornerType: 'ROUND'}");
//        notify();
        /*
        series.appendData(new DataPoint(lastX++, readUsage() * 100f), false, 20);
        synchronized (viewport){
            viewport.notify();
            viewport.scrollToEnd();
        }
        synchronized (series){
            series.notify();
            series.notifyAll();
        }
         */
    }
    public void click(View view) {
        Toast.makeText(container.getContext() , "Test Graph", Toast.LENGTH_LONG).show();
    }
}
