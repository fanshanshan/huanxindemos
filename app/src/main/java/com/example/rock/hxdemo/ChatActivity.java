package com.example.rock.hxdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rock.hxdemo.fragment.MyChatFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setContentView(R.layout.activity_chat);
        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(this));
        //new出EaseChatFragment或其子类的实例
        MyChatFragment chatFragment = new MyChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_chat_content, chatFragment).commit();
    }
}
