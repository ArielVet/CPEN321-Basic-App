package com.arielvet.cpen321milestone1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerInfo extends AppCompatActivity {

    /* Constants */
//    private static final String HOST = "http://10.0.2.2";
        private static final String HOST = "http://20.63.36.199";
    private static final Integer PORT = 8081;
    private static final String DEFAULT_TEXT = "NOT FOUND";

    // Google Sign In Variables
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> launcher;
    private static final int RC_SIGN_IN = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);

        // Set Up the google instances
        googleSetUp();
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
        TextView server_ip = findViewById(R.id.server_ip_text);
        fetchAPIValue("/ipAddress", server_ip);

        TextView client_ip = findViewById(R.id.client_ip_text);
        getClientIP(client_ip);

        TextView server_time = findViewById(R.id.server_time_text);
        fetchAPIValue("/time", server_time);

        TextView client_time = findViewById(R.id.client_time_text);
        client_time.setText(getTime());

        TextView backend_Name = findViewById(R.id.backend_name_text);
        fetchAPIValue("/name", backend_Name);

        TextView logged_in_name = findViewById(R.id.logged_in_name_text);
        logged_in_name.setText(accountName);
    }

    /**
     * Purpose: Sets IPv4 Of device
     *
     *          Private IP based on Based on: https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
     *
     * @return the IP address
     */
    private void getClientIP(TextView client_ip){

        makeGetRequest("https://api.ipify.org/?format=json", "ip", new Callback() {
            @Override
            public void onSuccess(String val) {
                client_ip.setText(val);
            }

            @Override
            public void onError() {
                Context context = getApplicationContext();
                WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                int ipAddress = wifiMan.getConnectionInfo().getIpAddress();

                String ip = String.valueOf(ipAddress & 255) + '.' +
                        ((ipAddress >> 8) & 255) + '.' +
                        ((ipAddress >> 16) & 255) + '.' +
                        ((ipAddress >> 24) & 255) + '.';

                client_ip.setText(ip);
            }
        });
    }

    /**
     * Purpose: Gets the time on the device in HH:mm:ss format
     *          Based On: https://www.w3schools.com/java/java_date.asp
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
     */
    private void fetchAPIValue (String endpoint, TextView textElement){
        makeGetRequest(HOST + ":" + PORT.toString() + endpoint, "data", new Callback() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String value) {
                if (value == null) value = DEFAULT_TEXT;
                textElement.setText(value);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onError(){
                textElement.setText(DEFAULT_TEXT);
            }
        });

    }

    /**
     * Purpose: Make an API call to a desired url and run a corresponding callback function
     *          when the get request succeeds / fails.
     *          Based On: https://www.geeksforgeeks.org/how-to-make-an-http-request-with-android/
     * @param url : the url we are making a GET request to
     * @param cb : the callback object that will be called to report the results of the API
     * @param jsonData : the parameter that holds the data in the jsonObject
     */
    private void makeGetRequest(String url, String jsonData, Callback cb) {
        RequestQueue volleyQueue = Volley.newRequestQueue(ServerInfo.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) response -> {
                    try {
                        cb.onSuccess(response.getString(jsonData));
                    } catch (JSONException e) {
                        cb.onError();
                    }
                },
                (Response.ErrorListener) error -> cb.onError()
        );
        volleyQueue.add(jsonObjectRequest);
    }


    /* Google Handler Functions */

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if (acc == null){
            signIn();
        }
        else{
            updateUI(acc.getDisplayName());
        }
    }

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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RC_SIGN_IN){
                Intent data = result.getData();
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        });
    }

    /**
     * Purpose: Function is called when the button is clicked to trigger the signIn process
     */
    private void signIn() {
        // If the signInIntent is successful it will return RC_SIGN_IN
        Intent intent = mGoogleSignInClient.getSignInIntent();
        launcher.launch(intent);
    }

    /**
     * Purpose: Once we confirm the logIn is successful using onActivityResult(),
     *          the function gets the associated account and calls getAccountInfo()
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        completedTask
                .addOnSuccessListener(googleSignInAccount -> updateUI(googleSignInAccount.getDisplayName()));
    }

}