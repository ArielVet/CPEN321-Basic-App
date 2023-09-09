package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
    private String public_IP_Address;
    private String client_IP_address;
    private String server_local_time;
    private String client_local_time;
    private String backend_Name;
    private String signIn_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);

        // Set Up the google account and sign in
        googleSetUp();
        signIn();


    }

    /* Helping Functions */
    

    /**
     * Purpose: Function is called only after the user signs in to the app.
     *          It makes API calls to the server and saves the data to local variables
     */
    private void updateData() {
        String url = HOST + ":" + PORT.toString();

        makeAPICall(url + "/ipAddress", new Callback() {
            @Override
            public void onSuccess(String val) {
                public_IP_Address = val;
            }

            @Override
            public void onError(){
                public_IP_Address = DEFAULT_TEXT;
            }
        });

        makeAPICall(url + "/time", new Callback() {
            @Override
            public void onSuccess(String val) {
                server_local_time = val;
            }
            @Override
            public void onError(){
                server_local_time = DEFAULT_TEXT;
            }
        });

        makeAPICall(url + "/name", new Callback() {
            @Override
            public void onSuccess(String val) {
                backend_Name = val;
            }
            @Override
            public void onError(){
                backend_Name = DEFAULT_TEXT;
            }
        });
    }

    // TODO: Repalce the varibales with direct modifications of the textview
//    https://stackoverflow.com/questions/18051276/return-a-value-from-asynchronous-call-to-run-method
//    https://www.geeksforgeeks.org/how-to-make-an-http-request-with-android/
//    https://www.geeksforgeeks.org/android-cleartext-http-traffic-not-permitted/
    /**
     * Purpose: Makes an API Call
     * */
    private void makeAPICall(String url, Callback cb) {
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
    interface Callback {
        void onSuccess(String val);
        void onError();
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
     *          the function gets the associated account and calls updateAccount()
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateAccount(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateAccount(null);
        }
    }

    /**
     * Purpose: Function saves the User's Name from the account and connects to the back-end
     */
    private void updateAccount(GoogleSignInAccount account) {
        if (account == null){
            Log.d(TAG, "No Users Signed In");
        }
        else {
            signIn_name = account.getDisplayName();
            Log.d(TAG, signIn_name);
            updateData();
        }
    }
}