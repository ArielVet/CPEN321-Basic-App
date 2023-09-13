package com.arielvet.cpen321milestone1;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerInfo extends AppCompatActivity {

    // CONSTANTS
    private static final String TAG = "ServerInfo";
    private static final String HOST = "http://10.0.2.2";
//        private static final String HOST = "http://20.63.36.199";
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

        Log.d(TAG, "TEST");
        /* Sets Up and Updates the TextViews */
        // Displayed Data
        TextView server_ip = findViewById(R.id.server_ip_text);
        fetchAPIValue("/ipAddress", server_ip, getString(R.string.server_ip_cap));

        TextView client_ip = findViewById(R.id.client_ip_text);
        client_ip.setText(getString(R.string.client_ip_cap) + " " + getClientIP());

        TextView server_time = findViewById(R.id.server_time_text);
        fetchAPIValue("/time", server_time, getString(R.string.server_time_cap));

        TextView client_time = findViewById(R.id.client_time_text);
        client_time.setText(getString(R.string.client_time_cap) + " " + getTime());

        TextView backend_Name = findViewById(R.id.backend_name_text);
        fetchAPIValue("/name", backend_Name, getString(R.string.backend_name_cap));

        TextView logged_in_name = findViewById(R.id.logged_in_name_text);
        logged_in_name.setText(getString(R.string.logged_in_name_cap) + " " + accountName);
    }

    /**
     * Purpose: retrieves the IPv4 address of the device
     *          Based on: https://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device-from-code
     *
     * @return the IP address
     */
    private String getClientIP(){
        Context context = getApplicationContext();
        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiMan.getConnectionInfo().getIpAddress();

        return String.valueOf(ipAddress & 255) + '.' +
                ((ipAddress >> 8) & 255) + '.' +
                ((ipAddress >> 16) & 255) + '.' +
                ((ipAddress >> 24) & 255) + '.';
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
                textElement.setText(prefixText + " " + DEFAULT_TEXT);
            }
        });

    }

    /**
     * Purpose: Make an API call to a desired url and run a corresponding callback function
     *          when the get request succeeds / fails.
     *          Based On: https://www.geeksforgeeks.org/how-to-make-an-http-request-with-android/
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
                (Response.ErrorListener) error -> cb.onError()
        );
        volleyQueue.add(jsonObjectRequest);
    }


    /* Google Handler Functions */

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Start");
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if (acc == null){
            Log.d(TAG, "Start-sign");
            signIn();
        }
        else{
            Log.d(TAG, "start-done");
            updateUI(acc.getDisplayName());
        }
    }

    /**
     * Purpose: Function configures the google sign in to request certain info and
     *          creates the client
     * */
    private void googleSetUp (){
        Log.d(TAG, "SetUp");
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RC_SIGN_IN){
                    Log.d(TAG, "TASK");
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    handleSignInResult(task);
                }
            }
        });
    }

    /**
     * Purpose: Function is called when the button is clicked to trigger the signIn process
     */
    private void signIn() {
        // If the signInIntent is successful it will return RC_SIGN_IN
        Log.d(TAG, "SignIn");
        Intent intent = mGoogleSignInClient.getSignInIntent();
        launcher.launch(intent);
    }

    /**
     * Purpose: Once we confirm the logIn is successful using onActivityResult(),
     *          the function gets the associated account and calls getAccountInfo()
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
       Log.d(TAG, "HANDLE");
        completedTask.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                updateUI(googleSignInAccount.getDisplayName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "FAIL");
            }
        });
    }

}