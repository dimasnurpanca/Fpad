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
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import java.util.Locale;

import static com.android.fpad.ui.LoginActivity.TAG_USERNAME;
import static com.android.fpad.ui.LoginActivity.session_status;

public class MainSettingsActivity extends AppCompatActivity {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.val_language)
    TextView val_language;


    APIInterface apiInterface;
    Activity context = this;
    String usrmail,usrname,nama,hp,gender,birthday,language;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        setContentView(R.layout.activity_main_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button
        setTitle(getResources().getString(R.string.main_title_settings));


        sharedpreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);

        usrmail = sharedpreferences.getString("email", null);
        usrname = sharedpreferences.getString("username", null);
        nama = sharedpreferences.getString("namalengkap", null);
        hp = sharedpreferences.getString("nomorhp", null);
        gender = sharedpreferences.getString("gender", null);
        birthday = sharedpreferences.getString("birthday", null);
        language = sharedpreferences.getString("language", null);


        val_language.setText(language);



        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.getMenu().getItem(4).setChecked(true);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        break;
                    case R.id.action_library:
                        startActivity(new Intent(getApplicationContext(), LibraryStoryActivity.class));
                        finish();
                        break;
                    case R.id.action_post_story:
                        startActivity(new Intent(getApplicationContext(), StoryActivity.class));
                        finish();
                        break;
                    case R.id.action_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                       finish();
                        break;
                    case R.id.action_setting:
                        //startActivity(new Intent(getApplicationContext(), MainSettingsActivity.class));
                       // finish();
                        break;
                }
                return true;
            }
        });
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }


    public void gantibahasa(View v)
    {
        final CharSequence[] items = { "English", "Bahasa Indonesia" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.change_language));

        int checkedItem = -1;
        // cow
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("language", items[which].toString());
                editor.commit();
                if(items[which].toString().equals("English")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").commit();
                    setLangRecreate("en");
                }else{
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "in").commit();
                    setLangRecreate("in");
                }
                dialog.dismiss();
                reload();
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

    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    public void account(View v)
    {
        Intent intent = new Intent(MainSettingsActivity.this, SettingsActivity.class);
        startActivity(intent);

    }

    public void about(View v)
    {
        Intent intent = new Intent(MainSettingsActivity.this, AboutActivity.class);
        startActivity(intent);

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

        Intent intent = new Intent(MainSettingsActivity.this, LoginActivity.class);
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






    @Override
    public void onBackPressed()
    {
        // moveTaskToBack(true);
        super.onBackPressed();
        startActivity(new Intent(MainSettingsActivity.this, HomeActivity.class));
        finish();

    }


}