package com.android.fpad.ui;

/**
 * Created by dimasnurpanca on 10/2/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.android.fpad.helper.BottomNavigationViewHelper;
import com.android.fpad.retrofit.APIClient;
import com.android.fpad.retrofit.APIInterface;
import com.android.fpad.retrofit.StoryRespond;
import com.android.fpad.ui.stories.EditStoryActivity;
import com.android.fpad.ui.stories.LibraryStoryActivity;
import com.android.fpad.ui.stories.StoryActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Calendar;

import static com.android.fpad.ui.LoginActivity.TAG_USERNAME;
import static com.android.fpad.ui.LoginActivity.session_status;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.val_username)
    TextView val_username;
    @BindView(R.id.val_fullname)
    TextView val_fullname;
    @BindView(R.id.val_gender)
    TextView val_gender;
    @BindView(R.id.val_birthday)
    TextView val_birthday;
    @BindView(R.id.val_email)
    TextView val_email;


    APIInterface apiInterface;
    Activity context = this;
    String usrmail,usrname,nama,hp,gender,birthday;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        setContentView(R.layout.settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        setTitle(getResources().getString(R.string.create_title_settings));


        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        usrmail = sharedpreferences.getString("email", null);
        usrname = sharedpreferences.getString("username", null);
        nama = sharedpreferences.getString("namalengkap", null);
        hp = sharedpreferences.getString("nomorhp", null);
        gender = sharedpreferences.getString("gender", null);
        birthday = sharedpreferences.getString("birthday", null);


        val_username.setText(usrname);
        val_fullname.setText(nama);
        val_email.setText(usrmail);
        val_gender.setText(gender);
        val_birthday.setText(birthday);
        if(gender.equals("")){
            val_gender.setText("-");
        }
        if(birthday.equals("")){
            val_birthday.setText("-");
        }




    }
    public void username(View v)
    {
        if(usrname.equals(usrmail)){
            final EditText edittext = new EditText(context);
            edittext.setHint("@username");


        edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.change_username));

        alert.setView(edittext);

        alert.setPositiveButton(getResources().getString(R.string.save_dialog), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(edittext.getText().toString().matches("[a-z]+")) {
                    simpan("username", edittext.getText().toString());
                }else{
                    Toast.makeText(context, "Harap masukan username dengan format alphabet dan lowercase", Toast.LENGTH_LONG).show();
                }

            }
        });

        alert.setNegativeButton(getResources().getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

        }else{
            Toast.makeText(this, "Anda hanya bisa mengganti username sekali saja", Toast.LENGTH_LONG).show();
        }
    }
    public void fullname(View v)
    {
            final EditText edittext = new EditText(context);
            edittext.setText(nama);


            edittext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.change_fullname));

            alert.setView(edittext);

            alert.setPositiveButton(getResources().getString(R.string.save_dialog), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if(edittext.getText().toString().matches("[a-zA-Z ]+")) {
                        simpan("fullname", edittext.getText().toString());
                    }else{
                        Toast.makeText(context, "Harap masukan fullname dengan format alphabet dan lowercase", Toast.LENGTH_LONG).show();
                    }

                }
            });

            alert.setNegativeButton(getResources().getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();

    }
    public void gender(View v)
    {
        final CharSequence[] items = { "Laki-laki", "Perempuan" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.change_gender));

        int checkedItem = -1;
        // cow
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                simpan("gender", items[which].toString());
                dialog.dismiss();
            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton(getResources().getString(R.string.save_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK


            }
        });

        AlertDialog dialog = builder.create();

            dialog.show();

    }
    public void birthday(View v)
    {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String datesbirth = dayOfMonth + "-" + monthOfYear + "-" + year;
                        simpan("birthday", datesbirth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public void password(View v)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Values");
        final EditText oldpassword = new EditText(SettingsActivity.this);
        final EditText newpassword1 = new EditText(SettingsActivity.this);
        final EditText newpassword2 = new EditText(SettingsActivity.this);


        oldpassword.setHint(getResources().getString(R.string.old_password));
        newpassword1.setHint(getResources().getString(R.string.new_password));
        newpassword2.setHint(getResources().getString(R.string.retype_password));
        newpassword1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});
        newpassword2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(35)});
        oldpassword.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        newpassword1.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );
        newpassword2.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD );




        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(oldpassword);
        ll.addView(newpassword1);
        ll.addView(newpassword2);
        alertDialog.setView(ll);

        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getResources().getString(R.string.save_dialog),  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String a = oldpassword.getText().toString();
                String b = newpassword1.getText().toString();
                String c = newpassword2.getText().toString();

                if(a==""){
                    Toast.makeText(context, getResources().getString(R.string.old_password_error), Toast.LENGTH_LONG).show();
                }else if(b==""){
                    Toast.makeText(context, getResources().getString(R.string.old_password_error), Toast.LENGTH_LONG).show();
                }else if(c==""){
                    Toast.makeText(context, getResources().getString(R.string.old_password_error), Toast.LENGTH_LONG).show();
                }else{
                    if(b.equals(c)){
                        gantipassword("gantipassword",a,b);
                    }else{
                        Toast.makeText(context, getResources().getString(R.string.new_password_error), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.close_dialog), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }

    public void forgot(View v)
    {
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




    public void logout(View v)
    {
        Toast.makeText(this, "Berhasil Logout!", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(session_status, false);
        editor.clear();
        editor.commit();
        FirebaseAuth.getInstance().signOut();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(getResources().getString(R.string.topics));

        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

    private void reload(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.recreate();
        } else {
            final Intent intent = this.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            this.finish();
            this.overridePendingTransition(0, 0);
            this.startActivity(intent);
            this.overridePendingTransition(0, 0);
        }
    }

    private void simpan(String type, String value) {
        final String user_value = value;
        final String type_value = type;
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingprogress));
        progressDialog.show();

        RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), usrmail);
        RequestBody val_type = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody val_value = RequestBody.create(MediaType.parse("multipart/form-data"), value);

        Call<StoryRespond> resultCall = null;
        if(type_value.equals("forgotpassword")){
            resultCall = apiInterface.etc(val_type, val_value);
        }else {
            resultCall = apiInterface.simpanuser(val_type, val_email, val_value);
        }

        resultCall.enqueue(new Callback<StoryRespond>() {
            @Override
            public void onResponse(Call<StoryRespond> call, Response<StoryRespond> response) {

                progressDialog.dismiss();

                // Response Success or Fail

                if (!response.body().getError()) {
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if(type_value.equals("username")){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(TAG_USERNAME, user_value);
                        editor.commit();
                        reload();
                    }
                    else if(type_value.equals("fullname")){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("namalengkap", user_value);
                        editor.commit();
                        reload();
                    }
                    else if(type_value.equals("gender")){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("gender", user_value);
                        editor.commit();
                        reload();
                    }
                    else if(type_value.equals("birthday")){
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("birthday", user_value);
                        editor.commit();
                        reload();
                    }

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

    private void gantipassword(String type, String oldpassword, String newpasword) {
        final String user_value1 = oldpassword;
        final String user_value2 = newpasword;
        final String type_value = type;
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SettingsActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.loadingprogress));
        progressDialog.show();

        RequestBody val_email = RequestBody.create(MediaType.parse("multipart/form-data"), usrmail);
        RequestBody val_type = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody val_value1 = RequestBody.create(MediaType.parse("multipart/form-data"), user_value1);
        RequestBody val_value2 = RequestBody.create(MediaType.parse("multipart/form-data"), user_value2);



        Call<StoryRespond> resultCall = apiInterface.gantipassword(val_type, val_email, val_value1,val_value2);


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




    @Override
    public void onBackPressed()
    {
        // moveTaskToBack(true);
        super.onBackPressed();
        startActivity(new Intent(SettingsActivity.this, MainSettingsActivity.class));
        finish();

    }


}