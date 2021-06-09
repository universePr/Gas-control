package com.cent.testchart.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import com.cent.testchart.Intro;
import com.cent.testchart.R;
import com.cent.testchart.constants.Constants;
import com.cent.testchart.data.Data;
import com.cent.testchart.database.Commit2DB;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cent.testchart.constants.Constants.MyPREFERENCES;
import static com.cent.testchart.constants.Constants.Phone;
import static com.cent.testchart.constants.Constants.tag_co;
import static com.cent.testchart.constants.Constants.tag_lpg;
import static com.cent.testchart.constants.Constants.tag_smoke;

public class SmsFragment extends Fragment {


    private View container;
    EditText edt;
    Button submit;

    public SmsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_sms, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
      container = view;
      init_();
    }

    private void init_() {
      edt = container.findViewById(R.id.edt_phonenumber);
      submit = container.findViewById(R.id.submit);

      submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Log.i("Update--",  "in changing");
              String phone_number = edt.getText().toString();

              if (        phone_number.equals("98")
                      ||  !phone_number.contains("98")
                      ||  phone_number.length() != 12
                      || containsIllegals(phone_number)
              ) {
                  Toast.makeText(container.getContext(), "Invalid call number " , Toast.LENGTH_SHORT).show();
                  Log.i("Update--",  "Not");
              } else {
                  insertPhoneNumberToSharedPref("+"+phone_number);
                  Toast.makeText(container.getContext(), "Update call number : Done " , Toast.LENGTH_SHORT).show();
                  Log.i("Update--",  "OK");
              }
          }
      });
    }

    private void insertPhoneNumberToSharedPref(String phone_number) {
        SharedPreferences sharedPreferences = App.app_context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Phone, phone_number);
        editor.apply();
    }


    public boolean containsIllegals(String toExamine) {
        Pattern pattern = Pattern.compile("[+$&()_!`/~#@*%{}<>\\[\\]|\"\\_^{a-z}{A-Z}]");
        Matcher matcher = pattern.matcher(toExamine);
        return matcher.find();
    }
}