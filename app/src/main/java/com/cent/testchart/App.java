package com.cent.testchart;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.cent.testchart.database.Commit2DB;
import com.cent.testchart.fragments.LiveStatisticsFragment;
import com.cent.testchart.fragments.StatiticsFragment;
import com.cent.testchart.services.Recorder;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class App extends AppCompatActivity {

    public static Context app_context;

    public static int amount_co = 0; //TODO: For share bluetooth data to service


    private Toolbar mToolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private Commit2DB commit2DB;

    Intent mServiceIntent;
    private Recorder mYourService;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        app_context = this;

        init_();
        init_db();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            init_service();
        }

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                amount_co++;
                handler.postDelayed(this, 2000);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init_() {

        drawerLayout = findViewById(R.id.drawer_layout);
        frameLayout = findViewById(R.id.frame);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setHomeButtonEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, 0, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerToggle.getDrawerArrowDrawable().setColor(getColor(R.color.vector));
        }else{
            drawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.vector));
        }
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view); // initiate a Navigation View
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //TODO: Disable one state and two click
                if(menuItem.getItemId() == R.id.menu_live_statistic){

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new LiveStatisticsFragment());
                    ft.commit();
                    drawerLayout.closeDrawers();
                }
                if(menuItem.getItemId() == R.id.menu_past_statistics){
//                    Toast.makeText(App.this, "During the last 30 days.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new StatiticsFragment());
                    ft.commit();
                    drawerLayout.closeDrawers();
                }

                else if(menuItem.getItemId() == R.id.menu_exit){
                    onBackPressed();
                }
                return true;
            }
        });
        Menu menu = navigationView.getMenu();

        MenuItem Statistics= menu.findItem(R.id.statistics);
        SpannableString s = new SpannableString(Statistics.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance1), 0, s.length(), 0);
        Statistics.setTitle(s);

        default_();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init_service() {
        mYourService = new Recorder();
        mServiceIntent = new Intent(this, mYourService.getClass());
        if (!isMyServiceRunning(mYourService.getClass())) {
            startService(mServiceIntent);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isMyServiceRunning(Class serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    private void init_db() {
        commit2DB = new Commit2DB(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)


    private void default_() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, new LiveStatisticsFragment());
        ft.commit();

    }

}