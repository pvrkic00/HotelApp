package com.example.pvrki.bazaprobav2.gost;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.pvrki.bazaprobav2.MyHandler;
import com.example.pvrki.bazaprobav2.NotificationSettings;
import com.example.pvrki.bazaprobav2.R;
import com.microsoft.windowsazure.notifications.NotificationsManager;

public class GostIzbornik extends AppCompatActivity {


    Intent i;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_gost_izbornik);

        i=getIntent();
        createNotificationChannel ();
        NotificationsManager.handleNotifications (this , NotificationSettings.SenderId , MyHandler.class);
        username=i.getStringExtra ("username");
        password=i.getStringExtra ("pass");

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        moveTaskToBack(true);
    }
    public  void clickKategorijaRestoran(View view)
    {
            new Thread (new Runnable () {
                public void run() {
                    // a potentially  time consuming task
                    i = new Intent (GostIzbornik.this , Restoran.class);
                    i.putExtra ("username" , username);
                    i.putExtra ("pass" , password);
                    startActivity (i);
                }
            }).start ();
    }
    protected void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "APP chanell";
            String description ="vjezba";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}


