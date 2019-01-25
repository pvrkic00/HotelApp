package com.example.pvrki.bazaprobav2.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pvrki.bazaprobav2.ConnectionHelper;
import com.example.pvrki.bazaprobav2.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Thread.sleep;

public class DodajKorisnika extends AppCompatActivity {

    Intent i;
    String username;
    String password;
    Statement stmt;

    String korisnickoIme,lozinka,email,brojMobitela,brojSobe;
    String uloga="3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_dodaj_korisnika);
        i=getIntent();
        username=i.getStringExtra ("username");
        password=i.getStringExtra ("pass");

    }



    public void ClickDodaj(View view) throws SQLException{
        Intent i2;
        TextInputLayout inputPassLayout=(TextInputLayout)findViewById (R.id.layout_input_pass) ;

        inputPassLayout.setError (null);
        inputPassLayout.setErrorEnabled (false);
        TextInputEditText korisnickoImeInput=(TextInputEditText)findViewById (R.id.input_username);
        TextInputEditText passwordInput=(TextInputEditText)findViewById (R.id.password_input);
        EditText emailInput=(EditText)findViewById (R.id.email_input);
        EditText brojMobitelaInput=(EditText)findViewById (R.id.mobitel_input);
        EditText brojSobeInput=(EditText)findViewById (R.id.btojSobe_input);

        korisnickoIme=korisnickoImeInput.getText().toString();
        lozinka=passwordInput.getText().toString();
        email=emailInput.getText().toString();
        brojMobitela=brojMobitelaInput.getText ().toString ();
        brojSobe=brojSobeInput.getText ().toString ();
        try {
            boolean isUpperCase = Character.isUpperCase(lozinka.charAt (0));
            if (lozinka.length() < 8 || isUpperCase==false) {
                inputPassLayout.setErrorEnabled (true);
                inputPassLayout.setError("Lozinka treba sadržavati minimalno 8 znakova te jedno veliko i malo slovo.");
            }else{
                inputPassLayout.setError (null);
                inputPassLayout.setErrorEnabled (false);
            }
        }catch (Exception e)
        {
            e.printStackTrace ();
        }
        String errorCheck=(String) inputPassLayout.getError ();

        if(errorCheck==null){
            new Thread (new Runnable () {
                public void run() {
                    ConnectionHelper ch = new ConnectionHelper (username , password);
                    PreparedStatement updateGoste = null;


                    String updateGost = "INSERT INTO korisnik (korisnicko_ime" +
                            "           ,lozinka " +
                            "           ,[email] " +
                            "           ,[kontakt_broj] " +
                            "           ,[broj_sobe] " +
                            "           ,[id_uloga]) VALUES (?,?,?,?,?,?)";


                    Connection connection = null;
                    try {
                        connection = ch.connections ();

                        updateGoste = connection.prepareStatement (updateGost);
                        updateGoste.setString (1 , korisnickoIme);
                        updateGoste.setString (2 , lozinka);
                        updateGoste.setString (3 , email);
                        updateGoste.setString (4 , brojMobitela);
                        updateGoste.setString (5 , brojSobe);
                        updateGoste.setString (6 , uloga);

                        updateGoste.executeUpdate ();





                    } catch (SQLException e) {
                        e.printStackTrace ();
                    } finally {
                        if (updateGoste != null) {
                            try {
                                updateGoste.close ();
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
            }).start ();
            new Thread (new Runnable () {
                public void run() {
                    ConnectionHelper ch = new ConnectionHelper (username , password);
                    PreparedStatement addUserDatabase =null;
                    Connection connection = null;
                    try {
                        connection = ch.connections ();

                        String addUser="CREATE USER "+korisnickoIme+
                                " WITH PASSWORD ='"+lozinka+"'" ;

                        addUserDatabase=connection.prepareStatement(addUser);

                        addUserDatabase.executeUpdate ();


                    } catch (SQLException e) {
                        e.printStackTrace ();
                    } finally {
                        if (addUserDatabase != null) {
                            try {
                                addUserDatabase.close ();
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
            }).start ();
            new Thread (new Runnable () {
                public void run() {
                    ConnectionHelper ch = new ConnectionHelper (username , password);
                    PreparedStatement addUserDatabase =null;
                    Connection connection = null;
                    try {
                        connection = ch.connections ();

                        String addUser= "ALTER ROLE [gost] ADD MEMBER "+korisnickoIme;

                        addUserDatabase=connection.prepareStatement(addUser);


                        addUserDatabase.executeUpdate ();



                    } catch (SQLException e) {
                        e.printStackTrace ();
                    } finally {
                        if (addUserDatabase != null) {
                            try {
                                addUserDatabase.close ();
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
            }).start ();
        }



        if(errorCheck==null)
        {
            runOnUiThread(new Runnable () {
                public void run() {
                    Toast.makeText (DodajKorisnika.this , "Korisnik uspješno dodan" , Toast.LENGTH_SHORT).show ();
                }});
            try {
                sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace ();
            }
            finish ();
        }


    }
}
