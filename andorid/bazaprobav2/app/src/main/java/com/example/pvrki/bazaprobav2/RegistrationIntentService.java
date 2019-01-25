package com.example.pvrki.bazaprobav2;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.microsoft.windowsazure.messaging.NotificationHub;

import static com.example.pvrki.bazaprobav2.MainActivity.mainActivity;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";


    protected NotificationHub hub;
    String uloga;
    Integer id_korisnika;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String resultString = null;
        String regID = null;
        Toast t;
        uloga=intent.getStringExtra("uloga");
        id_korisnika=intent.getIntExtra ("idKorisnika",0);

        String Tag=uloga+id_korisnika.toString ();

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(NotificationSettings.SenderId,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.i(TAG, "Got GCM Registration Token: " + token);

            // Storing the registration id that indicates whether the generated token has been
            // sent to your server. If it is not stored, send the token to your server,
            // otherwise your server should have already received the token.
           /* if ((regID=sharedPreferences.getString("registrationID", null)) == null) {*/

                 hub = new NotificationHub(NotificationSettings.HubName, NotificationSettings.HubListenConnectionString, this);
                Log.i(TAG, "Attempting to register with NH using token : " + token);

               // regID = hub.register(token).getRegistrationId();

                // If you want to use tags...
                // Refer to : https://azure.microsoft.com/documentation/articles/notification-hubs-routing-tag-expressions/
                    regID = hub.register(token, Tag).getRegistrationId();


                resultString = "Registered Successfully - RegId : " + regID;
                Log.i(TAG, resultString);
                sharedPreferences.edit().putString("registrationID", regID ).apply();

        } catch (Exception e) {
            Log.e(TAG, resultString="Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating the registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }
         if(mainActivity.isVisible) {

            mainActivity.ToastNotify (resultString);

        }


    }

}
