package com.cent.testchart.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.cent.testchart.App;
import com.cent.testchart.R;
import com.cent.testchart.constants.Constants;
import com.cent.testchart.data.Data;
import com.cent.testchart.database.Commit2DB;

import java.util.ArrayList;
import java.util.List;

public class StatiticsFragment extends Fragment {

    private View container;
    private Commit2DB commit2DB;
    private ArrayList<com.cent.testchart.data.Data> list_data = new ArrayList<Data>();
    private ArrayList<com.cent.testchart.data.Data> list_carbon_monoxide = new ArrayList<Data>();
    private ArrayList<com.cent.testchart.data.Data> list_lpg = new ArrayList<Data>();
    private ArrayList<com.cent.testchart.data.Data> list_smoke = new ArrayList<Data>();




    public StatiticsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_statitics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.container = view;
        commit2DB = new Commit2DB(App.app_context);
        init_();

    }

    private void init_() {
        AnyChartView anyChartView = container.findViewById(R.id.chart_past_statistics);

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("During the last 30 days.");

        cartesian.yAxis(0).title("The amount of gas released.");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        init_arrays();

        List<DataEntry> seriesData = new ArrayList<>();
        /*
        for(int i = 0 ; i < 0 ; i++ ){

            seriesData.add(new CustomDataEntry(i +"",
                    list_carbon_monoxide.get(i).getCount(),
                    list_lpg.get(i).getCount(),
                    list_smoke.get(i).getCount()));
        }
        */


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name(Constants.carbon_monoxide);
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name(Constants.lpg);
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series3 = cartesian.line(series3Mapping);
        series3.name(Constants.smoke);
        series3.hovered().markers().enabled(true);
        series3.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series3.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
        anyChartView.setVisibility(View.VISIBLE);
        anyChartView.findFocus();
        anyChartView.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value2, Number value3) {
            super(x, value);
            setValue("value2", value2);
            setValue("value3", value3);
        }

    }


    private void init_arrays() {
        list_data = commit2DB.getLatest30Days();
        for (int i = 0 ; i < list_data.size(); i+=3) {
                list_carbon_monoxide.add(list_data.get(i));
                list_lpg.add(list_data.get(i+1));
                list_smoke.add(list_data.get(i+2));

        }
    }

}