package com.example.rock.hxdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        final Intent intent;
        if(sharedPreferences.getString("user","").equals("")){
            intent = new Intent(this, LoginActivity.class);
        }else{
            intent = new Intent(this,MainActivity.class);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              startActivity(intent);
                finish();
            }
        },1000);
    }
}
