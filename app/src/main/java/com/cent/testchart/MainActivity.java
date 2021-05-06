package com.cent.testchart;

import android.app.Fragment;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Fragment mNavigationDrawerFragment;

    Toolbar mToolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init_();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init_() {

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        frameLayout = findViewById(R.id.frame);
        mToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, 0, 0);
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view); // initiate a Navigation View
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.nav_menu_statistic){
                    Toast.makeText(MainActivity.this, "Fragmant changing.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new StatisticsFragment());
                    ft.commit();
                }

                else if(menuItem.getItemId() == R.id.menu_exit){
                    System.exit(0);
                }
                return true;
            }
        });

        default_();

    }

    private void default_() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, new StatisticsFragment());
        ft.commit();
    }
/*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }
*/
}