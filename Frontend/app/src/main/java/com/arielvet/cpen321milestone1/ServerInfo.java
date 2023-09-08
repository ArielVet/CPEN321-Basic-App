package com.arielvet.cpen321milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class ServerInfo extends AppCompatActivity {

    private static final String TAG = "ServerInfo";
    // Google Sign In Variables
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);

        googleSetUp();
        signIn();

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
    }

    /**
     * Purpose: Function checks if a user has already signed in with Google
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        handleAccount(account);
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
     *          the function gets the associated account and calls handleAccount()
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            handleAccount(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            handleAccount(null);
        }
    }

    /**
     * Purpose: Function utilises the account data
     */
    private void handleAccount(GoogleSignInAccount account) {
        if (account == null){
            Log.d(TAG, "No Users Signed In");
        }
        else {
            Log.d(TAG, "Pref Name: " + account.getDisplayName());
        }
    }
}