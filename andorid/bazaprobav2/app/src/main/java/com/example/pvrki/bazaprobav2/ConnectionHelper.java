package com.example.pvrki.bazaprobav2;
import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    String user, password;
    public  ConnectionHelper(String us,String pass)
    {
        user=us;
        password=pass;
    }


    @SuppressLint ("NewApi")
    public Connection connections()
    {

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder ().permitAll ().build ();
        StrictMode.setThreadPolicy (policy);

        String connectionUrl = "jdbc:jtds:sqlserver://seminar.database.windows.net:1433;DatabaseName=HotelApp;user="+user+";password="+password+";encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        Connection connection = null;

        try {
            // Establish the connection.
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance ();
            connection = DriverManager.getConnection(connectionUrl);

        }

        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
