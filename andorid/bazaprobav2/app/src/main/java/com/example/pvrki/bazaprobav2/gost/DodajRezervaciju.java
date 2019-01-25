package com.example.pvrki.bazaprobav2.gost;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pvrki.bazaprobav2.ConnectionHelper;
import com.example.pvrki.bazaprobav2.MyHandler;
import com.example.pvrki.bazaprobav2.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class DodajRezervaciju extends AppCompatActivity {
    Intent i;
    String username, password;
    String brojOsoba;
    String vrijeme;
    String curDate;
    Integer id_zadatak = 1;
    String napomena;
    Integer idKorisnika;
    private String HubEndpoint = null;
    private String HubSasKeyName = null;
    private String HubSasKeyValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_rezervaciju);
        i = getIntent();

        username = i.getStringExtra("username");
        password = i.getStringExtra("pass");

        Spinner brojGostiju = findViewById(R.id.spinnerBrojGostiju);
//create a list of items for the spinner.
        String[] gosti = new String[]{"1", "2", "3", "4", "5", "6"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterGosti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gosti);
//set the spinners adapter to the previously created one.
        brojGostiju.setAdapter(adapterGosti);
        brojGostiju.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        brojOsoba = "1";
                        break;
                    case 1:
                        brojOsoba = "2";
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
                        brojOsoba = "3";
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 3:
                        brojOsoba = "4";
                        break;
                    case 4:
                        brojOsoba = "5";
                        break;
                    case 5:
                        brojOsoba = "6";
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        Spinner spinnerVrijeme = findViewById(R.id.spinnerVrijeme);
//create a list of items for the spinner.
        String[] Rez = new String[]{"0:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00",};

//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapterVrijeme = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Rez);
//set the spinners adapter to the previously created one.
        spinnerVrijeme.setAdapter(adapterVrijeme);
        spinnerVrijeme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        vrijeme = "0:00";
                        break;
                    case 1:
                        vrijeme = "1:00";
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
                        vrijeme = "2:00";
                        // Whatever you want to happen when the thrid item gets selected
                        break;
                    case 3:
                        vrijeme = "3:00";
                        break;
                    case 4:
                        vrijeme = "4:00";
                        break;
                    case 5:
                        vrijeme = "5:00";
                        break;
                    case 6:
                        vrijeme = "6:00";
                        break;
                    case 7:
                        vrijeme = "7:00";
                        break;
                    case 8:
                        vrijeme = "8:00";
                        break;
                    case 9:
                        vrijeme = "9:00";
                        break;
                    case 10:
                        vrijeme = "10:00";
                        break;
                    case 11:
                        vrijeme = "11:00";
                        break;
                    case 12:
                        vrijeme = "12:00";
                        break;
                    case 13:
                        vrijeme = "13:00";
                        break;
                    case 14:
                        vrijeme = "14:00";
                        break;
                    case 15:
                        vrijeme = "15:00";
                        break;
                    case 16:
                        vrijeme = "16:00";
                        break;
                    case 17:
                        vrijeme = "17:00";
                        break;
                    case 18:
                        vrijeme = "18:00";
                        break;
                    case 19:
                        vrijeme = "19:00";
                        break;
                    case 20:
                        vrijeme = "20:00";
                        break;
                    case 21:
                        vrijeme = "21:00";
                        break;
                    case 22:
                        vrijeme = "22:00";
                        break;
                    case 23:
                        vrijeme = "23:00";
                        break;

                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, Calendar.getInstance().getActualMinimum(Calendar.DATE));
        long date = calendar.getTime().getTime();
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarRezervacija);
        calendarView.setMinDate(date);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                int m = month + 1;
                int y = year;
                curDate = String.valueOf(d + "." + m + "." + year);
            }
        });


    }

    public void Rezerviraj(View view) {
        if (brojOsoba == "1") {
            napomena = "Stol za " + brojOsoba + " osobu.  \n" +
                    "Datum rezervacije : " + curDate + " \n" +
                    "Vrijeme rezervacije : " + vrijeme;
        } else {
            napomena = "Stol za  " + brojOsoba + " ljudi.  \n" +
                    "Datum rezervacije : " + curDate + ". \n" +
                    "Vrijeme rezervacije : " + vrijeme + ".";
        }
        CheckKorisnik checkKorisnik = new CheckKorisnik();
        checkKorisnik.execute("");


    }

    public class CheckKorisnik extends AsyncTask<String, String, String> {
        String z = "prijava uspješna";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(DodajRezervaciju.this, "Rezervacija uspješna", Toast.LENGTH_SHORT).show();
                }
            });

            MyHandler myHandler=new MyHandler ();
            myHandler.sendNotificationZadatakZatrazen ();
            i = new Intent(DodajRezervaciju.this, Restoran.class);
            i.putExtra("username", username);
            i.putExtra("pass", password);
            startActivity(i);

            finish();
        }


        @Override
        protected String doInBackground(String... params) {

            idKorisnika = ReturnID(username, password);
            ConnectionHelper ch = new ConnectionHelper(username, password);
            PreparedStatement updateGoste = null;


            String updateGost = "INSERT INTO Zadatak_trazen " +
                    " (" +
                    "           [napomena] " +
                    "           ,[id_zadatak] " +
                    "           ,[id_korisnik] " +
                    "           ,[vrijeme_trazeno]) " +
                    "            VALUES ('" + napomena + "'," + id_zadatak + "," + idKorisnika + " ,GETDATE())";


            Connection connection = null;
            try {
                connection = ch.connections();

                updateGoste = connection.prepareStatement(updateGost);


                updateGoste.executeUpdate();


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (updateGoste != null) {
                    try {
                        updateGoste.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


    }

    public Integer ReturnID(String username, String password) {

        Integer id_korisnika = 0;
        ConnectionHelper ch = new ConnectionHelper(username, password);
        PreparedStatement countMyRestauranRequest = null;
        Connection connection = null;
        String countMyRestaurantRequest = "SELECT id_korisnik from Korisnik where korisnicko_ime='" + username + "'";
        try {
            connection = ch.connections();

            countMyRestauranRequest = connection.prepareStatement(countMyRestaurantRequest);

            ResultSet resultSet = countMyRestauranRequest.executeQuery();

            while (resultSet.next()) {
                id_korisnika = resultSet.getInt(1);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (countMyRestauranRequest != null) {
                try {
                    countMyRestauranRequest.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return id_korisnika;
    }


}

