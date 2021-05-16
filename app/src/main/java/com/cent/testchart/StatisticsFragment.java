package com.cent.testchart;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
//
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.Viewport;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.RandomAccessFile;

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
    private void init_() {
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
//        changeGraph();

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
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }


                }
            }

        }).start();
    }
    // add random data to graph
    private void addEntry() {
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
