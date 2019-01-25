package com.example.pvrki.bazaprobav2.osoblje;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pvrki.bazaprobav2.ConnectionHelper;
import com.example.pvrki.bazaprobav2.OsobljeNotificationHandler;
import com.example.pvrki.bazaprobav2.R;
import com.example.pvrki.bazaprobav2.RezevacijaDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static android.os.SystemClock.sleep;

public class PopisDostupnihZadataka extends AppCompatActivity {
    Intent i;
    String naziv_zadatka, napomena, username, password, brojSobe;
    Integer id_zadatak_trazen, id_osoblja;
    ArrayList<RezevacijaDataModel> PopisZadataka;
    RezevacijaDataModel Zadatak;
    ListView listView;
    private ConstraintLayout mConstraintLayout;
    ProgressBar progressBar;
    private Context mContext;
    private PopupWindow mPopupWindow;


    private static CustomAdapterPopisZadataka adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_popis_dostupnih_zadataka);
        i = getIntent ();
        username = i.getStringExtra ("username");
        password = i.getStringExtra ("pass");
        mContext = getApplicationContext ();

        mConstraintLayout = (ConstraintLayout) findViewById (R.id.CLDostupniZadaci);
        listView = (ListView) findViewById (R.id.listaDostupnihZadataka);
        progressBar = (ProgressBar) findViewById (R.id.progressBarDostupniZadaci);
        PopisZadataka = new ArrayList<> ();
        adapter = new CustomAdapterPopisZadataka (PopisZadataka , getApplicationContext ());
        listView.setAdapter (adapter);
        CheckDostupneZadatke checkDostupneZadatke = new CheckDostupneZadatke ();

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent , View view , int position ,
                                    long id) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService (LAYOUT_INFLATER_SERVICE);

                Zadatak = adapter.getItem (position);
                String info = String.format ("ID: %d \nNAZIV: %s \nNAPOMENA:%s \nBROJ SOBE :%s" , Zadatak.getID () , Zadatak.getName () , Zadatak.getNapomena () , Zadatak.getBrojSobe ());
                // Inflate the custom layout/view
                View customView = inflater.inflate (R.layout.popup_window_dostupni_zadatak , null);
                mPopupWindow = new PopupWindow (
                        customView ,
                        LayoutParams.WRAP_CONTENT ,
                        LayoutParams.WRAP_CONTENT
                );
               /* if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }*/

                TextView infoZadatka = (TextView) customView.findViewById (R.id.infoZadtaka);
                infoZadatka.setText (info);
                ImageButton closeButton = (ImageButton) customView.findViewById (R.id.closePopUp);
                closeButton.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss ();
                    }
                });

                Button buttonPotvrdi = (Button) customView.findViewById (R.id.buttonPotvrdi);
                buttonPotvrdi.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        UpdateObavljenihZadataka updateObavljenihZadataka = new UpdateObavljenihZadataka (Zadatak.getID ());
                        updateObavljenihZadataka.execute ("");
                        adapter.remove (adapter.getItem (position));
                        mPopupWindow.dismiss ();
                        sleep (1000);
                        ToastNotify ("Zadatak obavljen");


                        OsobljeNotificationHandler osobljeNotificationHandler = new OsobljeNotificationHandler ();
                        osobljeNotificationHandler.sendNotificationZadatakObavljen ();
                    }
                });

                mPopupWindow.showAtLocation (mConstraintLayout , Gravity.CENTER , 0 , 0);

            }
        });
        checkDostupneZadatke.execute ("");
    }

    public void ReturnPopisZadataka(String username , String password) {

        ConnectionHelper ch = new ConnectionHelper (username , password);
        PreparedStatement countMyRestauranRequest = null;
        Connection connection = null;
        String countMyRestaurantRequest = "select Zadatak_trazen.id_zadatak_trazen,Zadatak.naziv,Zadatak_trazen.napomena,Korisnik.broj_sobe from Zadatak_trazen " +
                "inner join Zadatak on Zadatak_trazen.id_zadatak=Zadatak.id_zadatak " +
                "inner join Korisnik on Zadatak_trazen.id_korisnik=Korisnik.id_korisnik " +
                " where id_zadatak_dodijeljen is null";
        try {
            connection = ch.connections ();

            countMyRestauranRequest = connection.prepareStatement (countMyRestaurantRequest);

            ResultSet resultSet = countMyRestauranRequest.executeQuery ();

            while (resultSet.next ()) {
                id_zadatak_trazen = resultSet.getInt (1);
                naziv_zadatka = resultSet.getString (2);
                napomena = resultSet.getString (3);
                brojSobe = resultSet.getString (4);
                if (id_zadatak_trazen != null) {
                    PopisZadataka.add (new RezevacijaDataModel (id_zadatak_trazen , naziv_zadatka , napomena , brojSobe));
                }
            }


        } catch (SQLException e) {
            e.printStackTrace ();
        } finally {
            if (countMyRestauranRequest != null) {
                try {
                    countMyRestauranRequest.close ();
                } catch (SQLException e) {
                    e.printStackTrace ();
                }
            }
            if (connection != null) {
                try {
                    connection.close ();
                } catch (SQLException e) {
                    e.printStackTrace ();
                }
            }
        }

    }

    public class CheckDostupneZadatke extends AsyncTask<String, String, String> {

        Integer brojZadatka, idZadatka;
        String napomenaZadatka;


        @Override
        protected void onPreExecute() {
            listView.setVisibility (View.GONE);
            progressBar.setVisibility (View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {
            progressBar.setVisibility (View.GONE);
            listView.setVisibility (View.VISIBLE);


            //finish();
        }

        @Override
        protected String doInBackground(String... params) {

            Integer brojac;


            ReturnPopisZadataka (username , password);


            return null;
        }
    }

    public class UpdateObavljenihZadataka extends AsyncTask<String, String, String> {

        Integer brojZadatka, id_zadtaka;
        String napomenaZadatka;

        public UpdateObavljenihZadataka(Integer idZadatka2) {
            this.id_zadtaka = idZadatka2;
        }


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String r) {


            //finish();
        }

        @Override
        protected String doInBackground(String... params) {

            Integer brojac;


            ConnectionHelper ch = new ConnectionHelper (username , password);
            PreparedStatement updateObavljenihZadataka = null;
            PreparedStatement insertZadatakDodijeljen = null;
            Connection connection = null;
            String napomena = "Zadatak u obradi";
            String INSERT = "INSERT INTO [dbo].[Zadatak_dodijeljen] ([napomena]" +
                    ",[id_korisnik] " +
                    ",[vrijeme_izvrseno] " +
                    ", [id_trazenog_zadatka]) " +
                    "VALUES (?,?,GETDATE(),?)";
            String Update = "Update Zadatak_trazen " +
                    "set id_zadatak_dodijeljen =  (SELECT id_zadatak_dodijeljen from Zadatak_dodijeljen where id_korisnik=" + ReturnIdOsoblja (username , password) + " and id_trazenog_zadatka=" + id_zadtaka + " )" +
                    "where id_zadatak_trazen=" + id_zadtaka + ";";


            try {
                connection = ch.connections ();

                insertZadatakDodijeljen = connection.prepareStatement (INSERT);
                insertZadatakDodijeljen.setString (1 , napomena);
                insertZadatakDodijeljen.setInt (2 , ReturnIdOsoblja (username , password));
                insertZadatakDodijeljen.setInt (3 , id_zadtaka);


                updateObavljenihZadataka = connection.prepareStatement (Update);

                insertZadatakDodijeljen.executeUpdate ();
                updateObavljenihZadataka.executeUpdate ();


            } catch (SQLException e) {
                e.printStackTrace ();
            } finally {
                if (updateObavljenihZadataka != null) {
                    try {
                        updateObavljenihZadataka.close ();
                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close ();
                    } catch (SQLException e) {
                        e.printStackTrace ();
                    }
                }
            }


            return null;
        }
    }

    public Integer ReturnIdOsoblja(String username , String password) {
        ConnectionHelper ch = new ConnectionHelper (username , password);
        PreparedStatement returnID = null;
        Connection connection = null;
        String countMyRestaurantRequest = "select id_korisnik from Korisnik  where korisnicko_ime='" + username + "'";
        try {
            connection = ch.connections ();

            returnID = connection.prepareStatement (countMyRestaurantRequest);

            ResultSet resultSet = returnID.executeQuery ();

            while (resultSet.next ()) {
                id_osoblja = resultSet.getInt (1);


            }


        } catch (SQLException e) {
            e.printStackTrace ();
        } finally {
            if (returnID != null) {
                try {
                    returnID.close ();
                } catch (SQLException e) {
                    e.printStackTrace ();
                }
            }
            if (connection != null) {
                try {
                    connection.close ();
                } catch (SQLException e) {
                    e.printStackTrace ();
                }
            }
        }
        return id_osoblja;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread (new Runnable () {
            public void run() {
                // a potentially  time consuming task
                Toast.makeText (PopisDostupnihZadataka.this , notificationMessage , Toast.LENGTH_LONG).show ();
            }
        });
    }
}
