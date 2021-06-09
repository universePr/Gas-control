package com.cent.testchart.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cent.testchart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class LiveStatisticsFragment extends Fragment {

    private View container;


    public LiveStatisticsFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.live_static_layout, container, false);
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
                if(menuItem.getItemId() == R.id.co){
                    //Toast.makeText(container.getContext(), "Fragmant changing.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.frame_gas, new coFragment());
                    ft.commit();

                }
                if(menuItem.getItemId() == R.id.lpg){
                    //Toast.makeText(container.getContext(), "Fragmant changing.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.frame_gas, new lpgFragment());
                    ft.commit();

                }
                if(menuItem.getItemId() == R.id.Smoke){
                    //Toast.makeText(container.getContext(), "Fragmant changing.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.frame_gas, new smokeFragment());
                    ft.commit();

                }

                return false;
            }
        });
        bottomNavigationMenu.setSelectedItemId(R.id.co);

    }

}
