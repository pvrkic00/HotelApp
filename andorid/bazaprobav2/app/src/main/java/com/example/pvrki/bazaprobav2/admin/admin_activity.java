package com.example.pvrki.bazaprobav2.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.pvrki.bazaprobav2.R;

public class admin_activity extends AppCompatActivity {
    Intent i;
    String username;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_admin_activity);
        i=getIntent();

        username=i.getStringExtra ("username");
        password=i.getStringExtra ("pass");



    }


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        moveTaskToBack(true);
    }

    public  void clickDodajKorisnika(View view)
    {
        new Thread (new Runnable () {
            public void run() {
                // a potentially  time consuming task
                i = new Intent (admin_activity.this , DodajKorisnika.class);
                i.putExtra ("username" , username);
                i.putExtra ("pass" , password);
                startActivity (i);
            }
        }).start ();
    }
    public  void clickDodajOsoblje(View view)
    {
        new Thread (new Runnable () {
            public void run() {
                // a potentially  time consuming task
                i = new Intent (admin_activity.this , DodajOsoblje.class);
                i.putExtra ("username" , username);
                i.putExtra ("pass" , password);
                startActivity (i);
            }
        }).start ();
    }
}
