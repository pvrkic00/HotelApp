package com.example.pvrki.bazaprobav2.gost;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pvrki.bazaprobav2.ConnectionHelper;
import com.example.pvrki.bazaprobav2.CustomAdapterRezervacije;
import com.example.pvrki.bazaprobav2.R;
import com.example.pvrki.bazaprobav2.RezevacijaDataModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Restoran extends AppCompatActivity {

    Intent i;
    String username, password, napomena, getNapomena;
    Integer BrojZadataka;
    TextView textViewID, textViewNapomena;
    Integer id_zadatak_trazen, getId_zadatak_trazen;
    Integer Broj = 0;
    ProgressBar progressBar;
    ArrayList<RezevacijaDataModel> Rezervacije;
    ListView listView;

    private static CustomAdapterRezervacije adapter;


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Restoran.this , GostIzbornik.class);
        setIntent.putExtra ("username" , username);
        setIntent.putExtra ("pass" , password);
        startActivity (i);
        startActivity(setIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_restoran);
        i = getIntent ();

        username = i.getStringExtra ("username");
        password = i.getStringExtra ("pass");
        listView=(ListView)findViewById(R.id.listaRezervacija);
        progressBar = (ProgressBar) findViewById (R.id.progressBarRestoran);
        Rezervacije= new ArrayList<>();
        adapter= new CustomAdapterRezervacije(Rezervacije,getApplicationContext());
        listView.setAdapter(adapter);
        CheckRestoran checkRestoran=new CheckRestoran ();
        checkRestoran.execute ("");
    }

    public Integer ProvjeraBrojaZadataka(String username , String password) {

        ConnectionHelper ch = new ConnectionHelper (username , password);
        PreparedStatement countMyRestauranRequest = null;
        String countMyRestaurantRequest = "SELECT COUNT(id_zadatak_trazen) from Zadatak_trazen inner join Korisnik on Zadatak_trazen.id_korisnik=Korisnik.id_korisnik where korisnicko_ime='"+username+"'";
        Connection connection = null;
        try {
            connection = ch.connections ();

            countMyRestauranRequest = connection.prepareStatement (countMyRestaurantRequest);

            ResultSet resultSet = countMyRestauranRequest.executeQuery ();

            while (resultSet.next ()) {
                Broj = resultSet.getInt (1);
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
        return Broj;
    }

    public void ReturnIDINapomenu(String username , String password) {

        ConnectionHelper ch = new ConnectionHelper (username , password);
        PreparedStatement countMyRestauranRequest = null;
        Connection connection = null;
        String countMyRestaurantRequest = "SELECT id_zadatak_trazen,napomena from Zadatak_trazen inner join Korisnik on Zadatak_trazen.id_korisnik=Korisnik.id_korisnik where korisnicko_ime='" + username + "'";
        try {
            connection = ch.connections ();

            countMyRestauranRequest = connection.prepareStatement (countMyRestaurantRequest);

            ResultSet resultSet = countMyRestauranRequest.executeQuery ();

            while (resultSet.next ()) {
                id_zadatak_trazen = resultSet.getInt (1);
                napomena = resultSet.getString (2);
                if(id_zadatak_trazen!=null)
                {
                    Rezervacije.add(new RezevacijaDataModel(id_zadatak_trazen,napomena));
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




    public void DodajRezervaciju(View view) {
        new Thread (new Runnable () {
            public void run() {
                // a potentially  time consuming task
                i = new Intent (Restoran.this , DodajRezervaciju.class);
                i.putExtra ("username" , username);
                i.putExtra ("pass" , password);
                startActivity (i);
            }
        }).start ();

    }

    public class CheckRestoran extends AsyncTask<String, String, String> {

        Integer brojZadatka,idZadatka;
        String  napomenaZadatka;



        @Override
        protected void onPreExecute() {
            listView.setVisibility(View.GONE);
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
            brojZadatka = ProvjeraBrojaZadataka (username , password);

            ReturnIDINapomenu(username,password);



            return null;
        }
    }
}

