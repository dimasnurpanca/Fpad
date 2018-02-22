package com.android.fpad.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.android.fpad.R;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.RegisterList;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    APIInterface apiInterface;
    @BindView(R.id.input_username)
    EditText _usernameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_namalengkap)
    EditText _namalengkapText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    String fullname,email;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        fullname = intent.getStringExtra("fullname");
        email = intent.getStringExtra("email");

        _emailText.setText(email);
        _namalengkapText.setText(fullname);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupError();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Proses pendaftaran...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String namalengkap = _namalengkapText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.


        Call<RegisterList> call = apiInterface.doGetRegisterList(username,email,namalengkap,password);
        call.enqueue(new Callback<RegisterList>() {
            @Override
            public void onResponse(Call<RegisterList> call, Response<RegisterList> response) {
                RegisterList registerList = response.body();
                String status = registerList.status;
                Log.e("testid : ", registerList.status);

                if(status.equals("1")){
                    progressDialog.dismiss();
                    onSignupSuccess();
                }else if(status.equals("0")){
                    progressDialog.dismiss();
                    onSignupFailed();
                }else if(status.equals("3")){
                    progressDialog.dismiss();
                    onSignupError();
                }

            }

            @Override
            public void onFailure(Call<RegisterList> call, Throwable t) {
                call.cancel();
            }
        });

       // onSignupSuccess();
        // onSignupFailed();
       // progressDialog.dismiss();

       /**
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
        **/



    }


    public void onSignupSuccess() {
       // Toast.makeText(getBaseContext(), "Register Success! Silahkan cek email anda untuk melakukan konfirmasi pendaftaran.", Toast.LENGTH_LONG).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignupActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);

        final TextView symbol2 = (TextView) dialogView.findViewById(R.id.symbol2);
        final TextView text = (TextView) dialogView.findViewById(R.id.text);
        final CheckBox checkbox = (CheckBox) dialogView.findViewById(R.id.simpleCheckBox);

        checkbox.setVisibility(View.GONE);
        symbol2.setText("Registrasi Berhasil!");
        text.setText("Silahkan cek email Anda untuk Aktivasi account Fpad.\n"
        );
        dialogBuilder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                _signupButton.setEnabled(true);
                setResult(RESULT_OK, null);
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.getWindow().setBackgroundDrawableResource(R.color.warnaLight);



        Button buttonNegative = b.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(SignupActivity.this, R.color.colorPrimaryLight));
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);



    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Register Failed! Email/Username sudah terdaftar, silahkan gunakan email/username lain.", Toast.LENGTH_LONG).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignupActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);

        final TextView symbol2 = (TextView) dialogView.findViewById(R.id.symbol2);
        final TextView text = (TextView) dialogView.findViewById(R.id.text);
        final CheckBox checkbox = (CheckBox) dialogView.findViewById(R.id.simpleCheckBox);

        checkbox.setVisibility(View.GONE);
        symbol2.setText("Registrasi Tidak Berhasil!");
        text.setText("Email/Username sudah terdaftar, silahkan gunakan email/username lain.\n"
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
        buttonNegative.setTextColor(ContextCompat.getColor(SignupActivity.this, R.color.colorPrimaryLight));
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        _signupButton.setEnabled(true);
    }

    public void onSignupError() {
       // Toast.makeText(getBaseContext(), "Terjadi kesalahan! Silahkan cek ulang data pendaftaran anda.", Toast.LENGTH_LONG).show();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignupActivity.this);
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View dialogView = inflater.inflate(R.layout.popup, null);
        dialogBuilder.setView(dialogView);

        final TextView symbol2 = (TextView) dialogView.findViewById(R.id.symbol2);
        final TextView text = (TextView) dialogView.findViewById(R.id.text);
        final CheckBox checkbox = (CheckBox) dialogView.findViewById(R.id.simpleCheckBox);

        checkbox.setVisibility(View.GONE);
        symbol2.setText("Terjadi kesalahan!");
        text.setText("Silahkan cek ulang data pendaftaran anda.\n"
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
        buttonNegative.setTextColor(ContextCompat.getColor(SignupActivity.this, R.color.colorPrimaryLight));
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String namalengkap = _namalengkapText.getText().toString();
        String username = _usernameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (namalengkap.isEmpty() || namalengkap.length() < 3) {
            _namalengkapText.setError("at least 3 characters");
            valid = false;
        } else {
            _namalengkapText.setError(null);
        }

        if (username.isEmpty()) {
            _usernameText.setError("Enter Valid Username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }


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

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity


        super.onBackPressed();
        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

}