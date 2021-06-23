package com.cent.testchart;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.cent.testchart.database.Commit2DB;
import com.cent.testchart.fragments.LiveStatisticsFragment;
import com.cent.testchart.fragments.SmsFragment;
import com.cent.testchart.fragments.StaticsFragment;
import com.cent.testchart.services.BluetoothService;
import com.cent.testchart.services.Recorder;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.io.File;

public class App extends AppCompatActivity {

    public static Context app_context;

    public static int amount_co = 0;
    public static int amount_lpg = 0;
    public static int amount_smoke = 0;
    public static boolean deviceConnected = false;
    public static Activity main_app;


    private Toolbar mToolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private Commit2DB commit2DB;

    Intent recorder_intent;
    Intent bluetooth_intent;
    private Recorder recorder;
    private BluetoothService bluetoothService;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        app_context = this;
        main_app = this;
        init_permissions();
        init_();
        init_db();




    }




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
                if(menuItem.getItemId() == R.id.menu_live_statistic){

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new LiveStatisticsFragment());
                    ft.commit();
                    drawerLayout.closeDrawers();
                }
                if(menuItem.getItemId() == R.id.menu_past_statistics){
//                    Toast.makeText(App.this, "During the last 30 days.", Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new StaticsFragment());
                    ft.commit();
                    drawerLayout.closeDrawers();
                }
                if(menuItem.getItemId() == R.id.menu_connect){
                    init_service();
                }
                if(menuItem.getItemId() == R.id.menu_sms){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame, new SmsFragment());
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


    private void init_service() {
        bluetoothService = new BluetoothService();
        recorder = new Recorder();
        recorder_intent = new Intent(this, recorder.getClass());
        bluetooth_intent = new Intent(this, bluetoothService.getClass());
        if (!isMyServiceRunning(recorder.getClass()) || !isMyServiceRunning(bluetoothService.getClass())) {
            startService(recorder_intent);
            startService(bluetooth_intent);
        }
    }

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

    private void init_permissions() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) ==
                PackageManager.PERMISSION_GRANTED) {


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale("android.permission.SEND_SMS")) {


            } else {

                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.SEND_SMS}, 1);

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission granted to send sms", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(this, "Permission denied to send sms", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


    private void default_() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, new LiveStatisticsFragment());
        ft.commit();

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
}
