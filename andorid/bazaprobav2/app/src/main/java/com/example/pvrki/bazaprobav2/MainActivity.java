package com.example.pvrki.bazaprobav2;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pvrki.bazaprobav2.admin.admin_activity;
import com.example.pvrki.bazaprobav2.gost.GostIzbornik;
import com.example.pvrki.bazaprobav2.osoblje.OsobljeIzbornik;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.microsoft.windowsazure.notifications.NotificationsManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

    /*
        This source code could be used for academic purposes only. Posting on other websites or blogs is only allowed with a dofollow link to the orignal content.
    */

public class MainActivity extends AppCompatActivity {
    // Declaring layout button, edit texts
    Intent i;
    Button login;
    TextInputEditText username, password;
    ProgressBar progressBar;
    String korisnik="";
    Integer id_korisnika;
    String uloga;
    public static MainActivity mainActivity;
    public static Boolean isVisible = false;
    private GoogleCloudMessaging gcm;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public Toast Toast;
    private static final String TAG = "MainActivity";

    Connection con;
    String usernam;
    String passwordd;
    MyHandler myHandler;
    Statement stmt;
    ResultSet resultSet=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        // Getting values from button, texts and progress bar
        login = (Button) findViewById (R.id.button);
        username = (TextInputEditText) findViewById (R.id.editText);
        password = (TextInputEditText) findViewById (R.id.editText2);
        progressBar = (ProgressBar) findViewById (R.id.progressBar);
        // End Getting values from button, texts and progress bar

        mainActivity = this;
        createNotificationChannel ();




        // Setting up the function when button login is clicked
        login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                CheckLogin checkLogin = new CheckLogin ();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute ("");
            }
        });
        //End Setting up the function when button login is clicked
    }

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "Prijava uspješna";
        Boolean isSuccess = false;

        String usernam = username.getText ().toString ();
        String passwordd = password.getText ().toString ();

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility (View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility (View.GONE);

            //Toast.makeText (MainActivity.this , r , Toast.LENGTH_SHORT).show ();
            if (isSuccess==true) {
                Toast.makeText (MainActivity.this , z , Toast.LENGTH_LONG).show ();

                ConnectionHelper ch = new ConnectionHelper (usernam , passwordd);

                String query ="SELECT Uloga.naziv,Korisnik.id_korisnik FROM Korisnik inner join Uloga ON korisnik.id_uloga=uloga.id_uloga Where korisnicko_ime='"+usernam+"'";
                try  {
                    Connection connection = ch.connections ();
                    stmt = connection.createStatement ();

                    resultSet = stmt.executeQuery(query);

                    ResultSetMetaData rsmd=resultSet.getMetaData ();

                    while(resultSet.next ())
                    {
                        korisnik= (resultSet.getString (1));
                        id_korisnika=resultSet.getInt (2);
                    }

                    connection.close ();

                } catch (SQLException e) {
                    e.printStackTrace ();
                }
                registerWithNotificationHubs (korisnik);
                if(korisnik.equals ("Administrator"))
                {
                    new Thread (new Runnable () {
                        public void run() {
                            // a potentially  time consuming task
                            i = new Intent (MainActivity.this , admin_activity.class);
                            i.putExtra ("username" , usernam);
                            i.putExtra ("pass" , passwordd);
                            startActivity (i);
                        }
                    }).start ();
                    finish();
                }else if(korisnik.equals ("Osoblje"))
                {
                    createNotificationChannel ();
                    NotificationsManager.handleNotifications (MainActivity.this , NotificationSettings.SenderId , OsobljeNotificationHandler.class);
                    new Thread (new Runnable () {
                        public void run() {
                            // a potentially  time consuming task
                            i = new Intent (MainActivity.this , OsobljeIzbornik.class);
                            i.putExtra ("username" , usernam);
                            i.putExtra ("pass" , passwordd);
                            startActivity (i);
                        }
                    }).start ();
                    finish();

                }else if (korisnik.equals ("Gost"))
                {
                    createNotificationChannel ();
                    NotificationsManager.handleNotifications (MainActivity.this , NotificationSettings.SenderId , MyHandler.class);
                    new Thread (new Runnable () {
                        public void run() {
                            // a potentially  time consuming task
                            i = new Intent (MainActivity.this , GostIzbornik.class);
                            i.putExtra ("username" , usernam);
                            i.putExtra ("pass" , passwordd);
                            startActivity (i);
                            finish();
                        }
                    }).start ();
                }
            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            usernam = username.getText ().toString ();
            passwordd = password.getText ().toString ();

            if (usernam.trim ().equals ("") || passwordd.trim ().equals (""))
                z = "Please enter Username and Password";
            else {
                try {
                    ConnectionHelper ch = new ConnectionHelper (usernam , passwordd);
                    con = ch.connections ();
                    ConnectivityManager cm = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

                    if (cm.getActiveNetworkInfo () == null) {
                        z = "Provjerite internet";
                       ToastNotify (z);
                    } else if (con==null) {
                        z = "Pogrešni podaci";
                        ToastNotify (z);
                    } else {
                        isSuccess = true;
                        con.close ();
                    }

                } catch (SQLException e) {
                    isSuccess = false;
                   z = e.getMessage ();
                   // Toast.makeText (MainActivity.this , z , Toast.LENGTH_LONG).show ();
                }

            }
            return z;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance ();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable (this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError (resultCode)) {
                apiAvailability.getErrorDialog (this , resultCode , PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show ();
            } else {
                Log.i (TAG , "This device is not supported by Google Play Services.");
                Toast.makeText (this , "This device is not supported by Google Play Services." , Toast.LENGTH_LONG).show ();
                finish ();
            }
            return false;
        }
        return true;
    }

    public void registerWithNotificationHubs(String role) {
        Log.i (TAG , " Registering with Notification Hubs");
        Intent intent;
        if (checkPlayServices ()) {
            // Start IntentService to register this application with GCM.
            String uloga=role;
            intent = new Intent (MainActivity.this , RegistrationIntentService.class);
            intent.putExtra("uloga", uloga);
            intent.putExtra ("idKorisnika",id_korisnika);
            startService (intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart ();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause ();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume ();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop ();
        isVisible = false;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable () {
            @Override
            public void run() {
                Toast.makeText (MainActivity.this , notificationMessage , Toast.LENGTH_LONG).show ();
                //TextView helloText = (TextView) findViewById(R.id.text_hello);
                //username.setText (notificationMessage);
            }
        });

    }
    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "APP chanell";
            String description ="vjezba";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("0", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
