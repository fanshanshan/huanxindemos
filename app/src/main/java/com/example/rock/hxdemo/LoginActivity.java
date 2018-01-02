package com.example.rock.hxdemo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Random;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAcc;
    private EditText etPwd;
    private Button btnLogin;
    private Button btnRegiste;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etAcc = (EditText)findViewById(R.id.et_account);
        etPwd = (EditText)findViewById(R.id.et_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegiste = (Button)findViewById(R.id.btn_registe);
        btnLogin.setOnClickListener(this);
        btnRegiste.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_registe:
                registe();
                break;
        }
    }
    private void registe(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().createAccount(etAcc.getText().toString(),etPwd.getText().toString());//同步方法
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        login();

    }
    private void login(){
        btnLogin.setEnabled(false);
        btnLogin.setText("正在登陆...");
        EMClient.getInstance().login(etAcc.getText().toString(),etPwd.getText().toString(),new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                Random random=new Random();
                int temp = random.nextInt(10);
                String avatarUrl = String.format("http://duoroux.com/chat/avatar/%d.jpg",temp);// 用
                String nick =temp+"号小甜甜";
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.e("main", "登录聊天服务器成功！");
                SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                sharedPreferences.edit().putString("user",etAcc.getText().toString()).commit();
                sharedPreferences.edit().putString("pwd",etPwd.getText().toString()).commit();
                sharedPreferences.edit().putString("nick",nick).commit();
                sharedPreferences.edit().putString("url",avatarUrl).commit();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.e("main", "登录聊天服务器失败！");
                Toast.makeText(LoginActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
