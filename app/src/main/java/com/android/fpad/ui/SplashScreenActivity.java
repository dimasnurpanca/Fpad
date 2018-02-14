package com.android.fpad.ui;

/**
 * Created by DNP on 9/20/2017.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.fpad.R;

public class SplashScreenActivity extends AppCompatActivity {
    @BindView(R.id.textView2)
    TextView textView2;
    final android.os.Handler  handler = new android.os.Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/coolvetica.ttf");
        textView2.setTypeface(typeface);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);



        final Runnable runnable=new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        };
        handler.postDelayed(runnable,1000);


    }

    @Override
    public void onBackPressed() {


    }
}