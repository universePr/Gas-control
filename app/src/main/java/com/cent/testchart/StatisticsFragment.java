package com.cent.testchart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class StatisticsFragment extends Fragment {

    private View container;


    public StatisticsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.statistic_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.container = view;
        init_();

    }

    private void init_() {
        BottomNavigationView bottomNavigationMenu = container.findViewById(R.id.bottom_navigation);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.co2){
                    Toast.makeText(container.getContext(), "Fragmant changing.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.frame_pages_gas, new CarbonFragment());
                    ft.commit();

                }

                return false;
            }
        });
        bottomNavigationMenu.setSelectedItemId(R.id.co2);
    }

}
