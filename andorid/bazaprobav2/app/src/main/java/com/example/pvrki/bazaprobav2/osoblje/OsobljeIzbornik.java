package com.example.pvrki.bazaprobav2.osoblje;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.pvrki.bazaprobav2.NotificationSettings;
import com.example.pvrki.bazaprobav2.OsobljeNotificationHandler;
import com.example.pvrki.bazaprobav2.R;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.sql.Statement;

import static com.example.pvrki.bazaprobav2.MainActivity.mainActivity;

public class OsobljeIzbornik extends AppCompatActivity {
    Intent i;
    String username;
    String password;
    Statement stmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_osoblje_izbornik);
        i=getIntent();
        username=i.getStringExtra ("username");
        password=i.getStringExtra ("pass");
        mainActivity.createNotificationChannel ();
        NotificationsManager.handleNotifications (this , NotificationSettings.SenderId , OsobljeNotificationHandler.class);

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        moveTaskToBack(true);
    }


    public void ClickPopisDostupnihZadataka(View view)
    {
        new Thread (new Runnable () {
        public void run() {
            // a potentially  time consuming task
            i = new Intent (OsobljeIzbornik.this , PopisDostupnihZadataka.class);
            i.putExtra ("username" , username);
            i.putExtra ("pass" , password);
            startActivity (i);
        }
    }).start ();

    }


}
