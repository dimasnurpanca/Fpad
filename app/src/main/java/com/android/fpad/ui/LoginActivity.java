package com.android.fpad.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.android.fpad.R;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.LoginList;
import com.android.fpad.retrofit.StoryRespond;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    APIInterface apiInterface;
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_FORGOT = 0;
    SharedPreferences sharedpreferences;
    Boolean session = false;

    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_USERNAME = "username";
    public final static String TAG_PREMIUM = "premium";
    String usrmail, usrname, premium;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.btnEmail)
    TextView _signupLink;
    @BindView(R.id.link_forgot)
    TextView _forgotLink;
    @BindView(R.id.sign_in_google)
    SignInButton signInGoogleButton;
    @BindView(R.id.sign_in_facebook)
    LoginButton signInFacebookButton;

    Activity context = this;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private GoogleSignInClient mGoogleSignInClient;

    private CallbackManager mCallbackManager;
    private ResponseReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        registerReciever();

        pDialog = new ProgressDialog(LoginActivity.this);




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        mAuth = FirebaseAuth.getInstance();

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();


        apiInterface = APIClient.getClient().create(APIInterface.class);

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        usrmail = sharedpreferences.getString(TAG_EMAIL, null);
        if (session) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra(TAG_EMAIL, usrmail);
            finish();
            startActivity(intent);
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _forgotLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                final EditText edittext = new EditText(context);
                edittext.setHint(getResources().getString(R.string.forgot_hint));

                edittext.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                alert.setTitle(getResources().getString(R.string.forgot_builder_header));

                alert.setView(edittext);

                alert.setPositiveButton(getResources().getString(R.string.forgot_builder_submit), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        simpan("forgotpassword",edittext.getText().toString());

                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });






        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getResources().getString(R.string.topics));
    }

    public void googleLogin(View view)
    {
        signInGoogle();
    }

    public void fbLogin(View view)
    {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        firebaseAuthWithFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel()
                    {
                        Log.d(TAG, "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception)
                    {

                    }
                });
    }

    private void displayProgressDialog() {
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        displayProgressDialog();
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,"google.com");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Login Failed: ", Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }

                });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        displayProgressDialog();
        final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user,"facebook.com");
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void updateUI(FirebaseUser user, String social) {

        if (user != null) {
            if(user.getEmail()==null){
                loginsocial(user.getUid()+"@"+social,user.getDisplayName());
            }else {
                loginsocial(user.getEmail(), user.getDisplayName());
            }
        }
    }
    private void hideProgressDialog() {
        pDialog.dismiss();
    }

    public void loginsocial(String email, String namalengkap) {
        Log.d(TAG, "Login Social "+email+namalengkap);


        _loginButton.setEnabled(false);
        signInGoogleButton.setEnabled(false);
        signInFacebookButton.setEnabled(false);



        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // TODO: Implement your own authentication logic here.

        Call<LoginList> call = apiInterface.doGetLoginSocialList(email,namalengkap,android_id);
        call.enqueue(new Callback<LoginList>() {
            @Override
            public void onResponse(Call<LoginList> call, Response<LoginList> response) {
                LoginList loginList = response.body();
                String status = loginList.status;
                Log.e("testid : ", loginList.status);

                if(status.equals("1")){
                    // menyimpan login ke session
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_EMAIL, loginList.email);
                    editor.putString(TAG_USERNAME, loginList.username);
                    editor.putString("namalengkap", loginList.namalengkap);
                    editor.putString("role", loginList.role);
                    editor.putString("status", loginList.aktif);
                    editor.putString("gender", loginList.gender);
                    editor.putString("birthday", loginList.birthday);
                    editor.putString("language", "english");
                    editor.commit();
                    hideProgressDialog();
                    onLoginSuccess();
                }
                else if(status.equals("1")){
                    hideProgressDialog();
                    onLoginFailedNotActive();
                }

                else
                {
                    hideProgressDialog();
                    onLoginFailed();
                }

            }

            @Override
            public void onFailure(Call<LoginList> call, Throwable t) {
                hideProgressDialog();
                onLoginFailed();
                call.cancel();
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        String android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // TODO: Implement your own authentication logic here.

        Call<LoginList> call = apiInterface.doGetLoginList(email,password,android_id);
        call.enqueue(new Callback<LoginList>() {
            @Override
            public void onResponse(Call<LoginList> call, Response<LoginList> response) {
                LoginList loginList = response.body();
                String status = loginList.status;
                Log.e("testid : ", loginList.status);

                if(status.equals("1")){
                    // menyimpan login ke session
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean(session_status, true);
                    editor.putString(TAG_EMAIL, loginList.email);
                    editor.putString(TAG_USERNAME, loginList.username);
                    editor.putString("namalengkap", loginList.namalengkap);
                    editor.putString("role", loginList.role);
                    editor.putString("status", loginList.aktif);
                    editor.putString("gender", loginList.gender);
                    editor.putString("birthday", loginList.birthday);
                    editor.commit();
                    progressDialog.dismiss();
                    onLoginSuccess();
                }
                else if(status.equals("1")){
                    progressDialog.dismiss();
                    onLoginFailedNotActive();
                }

                else
                {
                    progressDialog.dismiss();
                    onLoginFailed();
                }

            }

            @Override
            public void onFailure(Call<LoginList> call, Throwable t) {
                progressDialog.dismiss();
                onLoginFailed();
                call.cancel();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity


        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    public void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "Login Berhasil!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);

        final TextView symbol2 = (TextView) dialogView.findViewById(R.id.symbol2);
        final TextView text = (TextView) dialogView.findViewById(R.id.text);
        final CheckBox checkbox = (CheckBox) dialogView.findViewById(R.id.simpleCheckBox);

        checkbox.setVisibility(View.GONE);
        symbol2.setText("Gagal Login!");
        text.setText("Email atau Password yang Anda masukan salah! \n \n Silahkan Cek kembali data login Anda. \n"
               );
        dialogBuilder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });


        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.getWindow().setBackgroundDrawableResource(R.color.warnaLight);



        Button buttonNegative = b.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryLight));
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);



        _loginButton.setEnabled(true);
    }

    public void onLoginFailedNotActive() {
       // Toast.makeText(getBaseContext(), "Gagal Login! Silahkan cek kembali data login Anda", Toast.LENGTH_LONG).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);

        final TextView symbol2 = (TextView) dialogView.findViewById(R.id.symbol2);
        final TextView text = (TextView) dialogView.findViewById(R.id.text);
        final CheckBox checkbox = (CheckBox) dialogView.findViewById(R.id.simpleCheckBox);

        checkbox.setVisibility(View.GONE);
        symbol2.setText("Akun Belum Di Aktivasi!");
        text.setText("Silahkan Cek Inbox Email Anda, dan lakukan aktivasi segera. \n"
        );
        dialogBuilder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });


        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.getWindow().setBackgroundDrawableResource(R.color.warnaLight);



        Button buttonNegative = b.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimaryLight));
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public class ResponseReceiver extends BroadcastReceiver {

        public static final String ACTION = "com.android.fpad.PROFILE";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Prf", "Receive info");

            String fullName = intent.getStringExtra("fullName");
            String email =  intent.getStringExtra("email");
            String url = intent.getStringExtra("url");
            //createDialog(fullName, email, url);
           //Toast.makeText(getApplicationContext(), "email"+email, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            i.putExtra("fullname",fullName); // input text symbol
            i.putExtra("email",email); // input text symbol
            startActivityForResult(i,0);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

    }


    private void simpan(String type, String value) {
        final String user_value = value;
        final String type_value = type;
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingprogress));
        progressDialog.show();

        RequestBody val_type = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody val_value = RequestBody.create(MediaType.parse("multipart/form-data"), value);



        Call<StoryRespond> resultCall = apiInterface.etc(val_type, val_value);


        resultCall.enqueue(new Callback<StoryRespond>() {
            @Override
            public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {

                progressDialog.dismiss();

                // Response Success or Fail

                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();}


            }

            @Override
            public void onFailure(Call<StoryRespond> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }



    private void registerReciever() {
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION);
        receiver = new ResponseReceiver();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReciever();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(receiver);
    }




}
