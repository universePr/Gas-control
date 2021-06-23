package com.cent.testchart.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;
import androidx.core.app.NotificationCompat;

import com.cent.testchart.App;
import com.cent.testchart.constants.Constants;
import com.cent.testchart.data.Data;
import com.cent.testchart.database.Commit2DB;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import static com.cent.testchart.constants.Constants.MyPREFERENCES;
import static com.cent.testchart.constants.Constants.Phone;
import static com.cent.testchart.constants.Constants.carbon_monoxide;
import static com.cent.testchart.constants.Constants.defPhone;
import static com.cent.testchart.constants.Constants.WARNING_CO_PPM;
import static com.cent.testchart.constants.Constants.WARNING_LPG_PPM;
import static com.cent.testchart.constants.Constants.WARNING_SMOKE_PPM;
import static com.cent.testchart.constants.Constants.tag_co;
import static com.cent.testchart.constants.Constants.tag_lpg;
import static com.cent.testchart.constants.Constants.tag_smoke;

public class Recorder extends Service {

    private com.cent.testchart.database.Commit2DB commit2DB;

    /***/
    public Recorder(){
        this.commit2DB = new Commit2DB(App.app_context);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "com.safe_home";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Safe Home in process...")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startRecorde();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRecordetask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("RestartService");
        broadcastIntent.setClass(this, RestartService.class);
        this.sendBroadcast(broadcastIntent);
    }
    private Timer timer;
    private TimerTask timerTask;
    private Calendar calendar;

    public void startRecorde() {

        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                calendar = Calendar.getInstance();
                String date = calendar.getTime().toString();
                if(App.deviceConnected) {


                    commit2DB.insertData(new Data(App.amount_co, date , Constants.tag_co ));
                    commit2DB.insertData(new Data(App.amount_lpg, date, Constants.tag_lpg ));
                    commit2DB.insertData(new Data(App.amount_smoke , date, Constants.tag_smoke ));


                }else{
                    Log.i("statistics--", "In database not connected");
                }
            }
        };
        timer.schedule(timerTask, 1000, 2 * 60 * 1000);

    }


    public void stopRecordetask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}