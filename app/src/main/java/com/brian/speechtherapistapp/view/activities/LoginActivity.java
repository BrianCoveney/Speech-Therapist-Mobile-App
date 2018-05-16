package com.brian.speechtherapistapp.view.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.view.activities.base.BaseActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    boolean boolean_google;
    ProgressDialog dialog;
    private boolean isLoginTherapist;
    private boolean isLoginChild;
    private Child child;

    @BindView(R.id.login_constraint_layout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.btn_google_sign_in)
    SignInButton googleSignInButton;

    @BindView(R.id.tv_first_name_caption)
    TextView nameCaptionTextView;

    @BindView(R.id.tv_first_name)
    TextView textViewName;

    @BindView(R.id.tv_email)
    TextView testViewEmail;

    @BindView(R.id.iv_app_image)
    ImageView appImage;

    @BindView(R.id.et_email)
    EditText editTextChildEmail;

    @BindView(R.id.et_password)
    EditText editTextChildPassword;

    @BindView(R.id.btn_login)
    Button buttonChildLogin;

    @BindView(R.id.btn_continue)
    Button buttonContinue;

    public static final String EXTRA_MESSAGE = "therapist_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.activity_login, frameLayout);
        ButterKnife.bind(this);

        init();
        displayLoginType();

        if (isLoginTherapist == true) {
            appImage.setImageResource(R.drawable.therapist2);
            googleSignInButton.setVisibility(View.VISIBLE);
            buttonContinue.setVisibility(View.INVISIBLE);
            editTextChildEmail.setVisibility(View.INVISIBLE);
            editTextChildPassword.setVisibility(View.INVISIBLE);
            buttonChildLogin.setVisibility(View.INVISIBLE);
        }
        else if (isLoginChild == true) {
            appImage.setImageResource(R.drawable.bubbles);
            googleSignInButton.setVisibility(View.INVISIBLE);
            buttonContinue.setVisibility(View.INVISIBLE);
            editTextChildEmail.setVisibility(View.VISIBLE);
            editTextChildPassword.setVisibility(View.VISIBLE);
            buttonChildLogin.setVisibility(View.VISIBLE);
        }
    }

    private void displayLoginType() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isLoginChild = extras.getBoolean("child_login");
            isLoginTherapist = extras.getBoolean("therapist_login");
        }
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    setTextOfGoogleSignInButton("Sign Out");

                    // Prompt user to Continue, or they can press GoogleSignInButton to sign out
                    buttonContinue.setVisibility(View.VISIBLE);

                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    setTextViewsInvisible();
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @OnClick({R.id.btn_google_sign_in, R.id.btn_continue})
    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                if (isLoginTherapist == true) {
                    Intent intentTherapistActivity = new Intent(this, TherapistMenuActivity.class);
                    startActivity(intentTherapistActivity);
                }
                break;
            case R.id.btn_google_sign_in:
                if (boolean_google) {
                    signOut();
                    textViewName.setText("");
                    testViewEmail.setText("");
                    boolean_google = false;
                } else {
                    signIn();
                }
                break;
        }
    }


    /**
     * When the Child logs in we save their email to SharedPreferences.
     * The email is accessed by the following class and method:
     *
     * @link GameMenuActivity#getChildFromChildLoginActivity()
     */
    @OnClick(R.id.btn_login)
    public void onClickButtonLoginChild() {

        // We retrieve the email address typed in the EditText email field
        String email = editTextChildEmail.getText().toString();

        // We create a SharedPreferences object so that we can save key-value data. Here, we are
        // storing the object, with the key 'email_pref_key', so that we can share this object with
        // the GameActivity class.
        SharedPreferences sharedPref =
                getSharedPreferences("email_pref_key", Activity.MODE_PRIVATE);

        // We write to a SharedPreference file by passing the key: 'email_key' and value: 'email'
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email_key", email);
        editor.apply();

        // We start the GameMenuActivity by creating an intent, passing this activities context,
        // and the target activity.
        Intent intentGameMenuActivity = new Intent(this, GameActivity.class);
        startActivity(intentGameMenuActivity);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                if (isLoginChild == true) {
                    Intent intentGameMenuActivity = new Intent(this, GameMenuActivity.class);
                    startActivity(intentGameMenuActivity);
                }
                else if (isLoginTherapist == true) {
                    Intent intentTherapistActivity = new Intent(this, TherapistMenuActivity.class);
                    startActivity(intentTherapistActivity);
                }
            } else {
                // Google Sign In failed, update UI appropriately

                updateUI(null);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());
        try {
            dialog.show();
        } catch (Exception e) {

        }

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithCredential", task.getException());
                            showToast("Authentication failed.");
                        }

                        try {
                            dialog.dismiss();
                        } catch (Exception e) {

                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        try {
            mAuth.signOut();

            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            updateUI(null);
                        }
                    });

            buttonContinue.setVisibility(View.INVISIBLE);
        } catch (Exception e) {

        }
        setTextOfGoogleSignInButton("Sign In");

    }

    private void updateUI(FirebaseUser user) {
        try {
            dialog.dismiss();
        } catch (Exception e) {

        }

        if (user != null && isLoginTherapist) {
            testViewEmail.setVisibility(View.VISIBLE);
            textViewName.setVisibility(View.VISIBLE);
            nameCaptionTextView.setVisibility(View.VISIBLE);

            String str_emailGoogle = user.getEmail();
            Log.e(LOG_TAG, "Email: " + str_emailGoogle);
            testViewEmail.setText("Press Continue to Sign in with: " + str_emailGoogle);
            textViewName.setText(user.getDisplayName());
            boolean_google = true;

            Log.e(LOG_TAG, "Profile: " + user.getPhotoUrl() + "");
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(LOG_TAG, "onConnectionFailed: " + connectionResult);
        showToast("Google Play Services error.");
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        signOut();
        Intent intentHomeActivity = new Intent(this, HomeActivity.class);
        startActivity(intentHomeActivity);
    }

    private void setTextOfGoogleSignInButton(String text) {
        TextView textView = (TextView) googleSignInButton.getChildAt(0);
        textView.setText(text);
    }

    private void setTextViewsInvisible() {
        testViewEmail.setVisibility(View.INVISIBLE);
        nameCaptionTextView.setVisibility(View.INVISIBLE);
        textViewName.setVisibility(View.INVISIBLE);
    }
}
