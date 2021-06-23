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
import androidx.core.app.NotificationCompat;

import com.cent.testchart.App;
import com.cent.testchart.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import static com.cent.testchart.constants.Constants.MyPREFERENCES;
import static com.cent.testchart.constants.Constants.Phone;
import static com.cent.testchart.constants.Constants.defPhone;
import static com.cent.testchart.constants.Constants.WARNING_CO_PPM;
import static com.cent.testchart.constants.Constants.WARNING_LPG_PPM;
import static com.cent.testchart.constants.Constants.WARNING_SMOKE_PPM;

public class BluetoothService extends Service {

    private final String DEVICE_ADDRESS="78:D8:5D:10:1B:C7";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    boolean deviceConnected=false;
    byte buffer[];

    public BluetoothService(){
        if(BTinit())
        {
            if(BTconnect())
            {
                App.deviceConnected = true;
                deviceConnected=true;
            }

        }
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
        String NOTIFICATION_CHANNEL_ID = "com.safe_home.bluetooth";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("")
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startBluetoothStream();
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


    public void startBluetoothStream() {

        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

                String co;
                String lpg;
                String smoke;

                if(App.deviceConnected) {
                    lpg = ListenForData();
                    co  = ListenForData();
                    smoke = ListenForData();

                    Log.i("statistics--", lpg + "//" + co + "//" + smoke);
                    App.amount_lpg = Integer.parseInt(lpg);
                    App.amount_co = Integer.parseInt(co);
                    App.amount_smoke = Integer.parseInt(smoke);

                    if(App.amount_co > WARNING_CO_PPM){ //Warning 250 ppm
                        sendSMS(Constants.carbon_monoxide);
                    }
                    if(App.amount_co > WARNING_LPG_PPM){ //Warning 250 ppm
                        sendSMS(Constants.lpg);
                    }
                    if(App.amount_co > WARNING_SMOKE_PPM){ //Warning 250 ppm
                        sendSMS(Constants.smoke);
                    }


                }else{
                    Log.i("statistics--", "Not connected from bluetooth service");
                }


            }
        };
    }

    private void sendSMS(String gas){
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                    sharedPreferences.getString(Phone, defPhone),
                    null,
                    "Warning for the amount of "+gas+" gas in the air!",
                    null,null);
            Log.i("SEND SMS", "Send");

        } catch (Exception e) {
            Log.i("SEND SMS", "Error");
            e.printStackTrace();
        }
        Log.i("SEND SMS", sharedPreferences.getString(Phone, defPhone));

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

    public boolean BTinit() {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            App.main_app.startActivityForResult(enableAdapter, 1);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(App.app_context, "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                if(iterator.getAddress().equals(DEVICE_ADDRESS))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect() {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }

    private String ListenForData() {
        buffer = new byte[1024];
        String string = "0";
        int byteCount = 0;
        boolean isExist = false;
        while(!isExist) {
            try {
                byteCount = inputStream.available();
                if (byteCount > 0) {

                    byte[] rawBytes = new byte[byteCount];
                    try {
                        inputStream.read(rawBytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        string = new String(rawBytes,"UTF-8");
                        if(Integer.parseInt(string.trim()) >= 0 )
                            isExist = true;
                        else
                            string = "0";

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return string;
    }

}