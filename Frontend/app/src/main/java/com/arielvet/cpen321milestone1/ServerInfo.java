package com.arielvet.cpen321milestone1;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerInfo extends AppCompatActivity {

    // CONSTANTS
    private static final String TAG = "ServerInfo";
    private static final String HOST = "http://10.0.2.2";
    //    private static final String HOST = "20.63.36.199"; REPLACE ONCE SERVER GOES LIVE
    private static final Integer PORT = 8081;
    private static final String DEFAULT_TEXT = "NOT FOUND";

    // Google Sign In Variables
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    // Displayed Data
    private TextView server_ip;
    private TextView client_ip;
    private TextView server_time;
    private TextView client_time;
    private TextView backend_Name;
    private TextView logged_in_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);

        // Set Up the google instances
        googleSetUp();

        // Triggers the sign in process. Once a successful sign in occurs, updateUI() is called
        // and the TextViews on the screen update with the correct values.
        signIn();


    }

    /* Helping Functions */

    /**
     * Purpose: Function Updates the TextElements on the screen with their associated values
     *
     * @param accountName : the name attached to the account that just logged in
     */
    @SuppressLint("SetTextI18n")
    private void updateUI(String accountName) {

        /* Sets Up and Updates the TextViews */
        server_ip = findViewById(R.id.server_ip_text);
        fetchAPIValue("/ipAddress", server_ip, getString(R.string.server_ip_cap));

        client_ip = findViewById(R.id.client_ip_text);
        client_ip.setText(getString(R.string.client_ip_cap) + " " + getClientIP());

        server_time = findViewById(R.id.server_time_text);
        fetchAPIValue("/time", server_time, getString(R.string.server_time_cap));

        client_time = findViewById(R.id.client_time_text);
        client_time.setText(getString(R.string.client_time_cap) + " " + getTime());


        backend_Name = findViewById(R.id.backend_name_text);
        fetchAPIValue("/name", backend_Name, getString(R.string.backend_name_cap));

        logged_in_name = findViewById(R.id.logged_in_name_text);
        logged_in_name.setText(getString(R.string.logged_in_name_cap) + " " + accountName);
    }

    /**
     * Purpose: retrieves the IPv4 address of the device
     *
     *          Based on: https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
     *
     * @return the IP address
     */
    private String getClientIP(){
        Context context = getApplicationContext();
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiMan.getConnectionInfo().getIpAddress();

        String ip = String.valueOf(ipAddress & 255) + '.' +
                ((ipAddress >> 8) & 255) + '.' +
                ((ipAddress >> 16) & 255) + '.' +
                ((ipAddress >> 24) & 255) + '.';

        return ip;
    }

    /**
     * Purpose: Gets the time on the device in HH:mm:ss format
     *
     *          Based On: https://www.w3schools.com/java/java_date.asp
     *
     * @return time as a string
     *
     */
    private String getTime(){
        DateTimeFormatter prefFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime currTime = LocalDateTime.now();
        return currTime.format(prefFormat);
    }

    /**
     * Purpose: Definition of the Callback Interface that is used by the
     *          API to export the values retrieved.
     *          Defines functions that are used when the API succeeds/fails
     *
     *          Design Based on: https://stackoverflow.com/questions/18051276/return-a-value-from-asynchronous-call-to-run-method
     */
    interface Callback {
        void onSuccess(String val);
        void onError();
    }

    /**
     * Purpose: Calls the API and defines the Callback to set the appropriate TextView
     *          element on the screen with the value returned from API
     *
     * @param endpoint : a String representing the targeted endpoint at the server
     * @param textElement : the TextView element we want to target
     * @param prefixText : the already existing text on the screen (eg: "Server IP Address:")
     */
    private void fetchAPIValue (String endpoint, TextView textElement, String prefixText){

        makeGetRequest(HOST + ":" + PORT.toString() + endpoint, new Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String value) {
                textElement.setText(prefixText + " " + value);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(){
                server_ip.setText(prefixText + " " + DEFAULT_TEXT);
            }
        });

    }

    /**
     * Purpose: Make an API call to a desired url and run a corresponding callback function
     *          when the get request succeeds / fails.
     *
     *          Based On: https://www.geeksforgeeks.org/how-to-make-an-http-request-with-android/
     *
     * @param url : the url we are making a GET request to
     * @param cb : the callback object that will be called to report the results of the API
     */
    private void makeGetRequest(String url, Callback cb) {
        RequestQueue volleyQueue = Volley.newRequestQueue(ServerInfo.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    try {
                        cb.onSuccess(response.getString("data"));
                    } catch (JSONException e) {
                        cb.onError();
                    }
                },
                (Response.ErrorListener) error -> {
                    cb.onError();
                }
        );
        volleyQueue.add(jsonObjectRequest);
    }


    /* Google Handler Functions */

    /**
     * Purpose: Function configures the google sign in to request certain info and
     *          creates the client
     * */
    private void googleSetUp (){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    /**
     * Purpose: Function is called when the button is clicked to trigger the signIn process
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        // If the signInIntent is successful it will return RC_SIGN_IN
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Purpose: Function listens for the user's activity,
     *          Checks if its successful, and if it is, handle the sign in
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Purpose: Once we confirm the logIn is successful using onActivityResult(),
     *          the function gets the associated account and calls getAccountInfo()
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            getAccountInfo(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            getAccountInfo(null);
        }
    }

    /**
     * Purpose: Function saves the User's Name from the account and connects to the back-end
     */
    private void getAccountInfo(GoogleSignInAccount account) {
        if (account == null){
            Log.d(TAG, "No Users Signed In");
        }
        else {
            updateUI(account.getDisplayName());
        }
    }
}