package com.example.rock.hxdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.example.rock.hxdemo.fragment.ConversationFragment;
import com.example.rock.hxdemo.fragment.PersonFragment;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EaseContactListFragment contactFragment;//联系人
    private PersonFragment personFragment;
    private RadioButton rbConver;
    private RadioButton rbContack;
    private RadioButton rbPerson;
    private ConversationFragment conversationFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rbConver = (RadioButton) findViewById(R.id.rb_conver);
        rbContack = (RadioButton) findViewById(R.id.rb_contack);
        rbPerson = (RadioButton) findViewById(R.id.rb_person);
        rbContack.setOnClickListener(this);
        rbConver.setOnClickListener(this);
        rbPerson.setOnClickListener(this);
        contactFragment = new EaseContactListFragment();
        conversationFragment = new ConversationFragment();
        personFragment = new PersonFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.contanier,conversationFragment).commit();
        rbConver.setTextColor(Color.GREEN);
        //注册一个监听连接状态的listener

        new Thread() {//需要在子线程中调用
            @Override
            public void run() {
                //需要设置联系人列表才能启动fragment
                contactFragment.setContactsMap(getContact());
            }
        }.start();

        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                for (EMMessage message : list) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //接收并处理扩展消息
                    String userName = message.getStringAttribute(Constant.USER_NAME, "");
                    String userId = message.getStringAttribute(Constant.USER, "");
                    String userPic = message.getStringAttribute(Constant.HEAD_IMAGE_URL, "");
                    String hxIdFrom = message.getFrom();
                    EaseUser easeUser = new EaseUser(hxIdFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNick(userName);
                    getSharedPreferences("user",MODE_PRIVATE).edit().putString(hxIdFrom,userName+"&"+userPic).commit();
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });

        //设置item点击事件
        contactFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {


            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
            }

            @Override
            public void onFriendRequestAccepted(String s) {

            }

            @Override
            public void onFriendRequestDeclined(String s) {

            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
                        contactFragment.setContactsMap(getContact());
                        contactFragment.refresh();
                    }
                }.start();
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法


                new Thread() {//需要在子线程中调用
                    @Override
                    public void run() {
                        //需要设置联系人列表才能启动fragment
                        contactFragment.setContactsMap(getContact());
                        contactFragment.refresh();
                    }
                }.start();

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rb_conver:
                rbConver.setTextColor(Color.GREEN);
                rbContack.setTextColor(Color.BLACK);
                rbPerson.setTextColor(Color.BLACK);
                getSupportFragmentManager().beginTransaction().replace(R.id.contanier,conversationFragment).commit();
                break;
            case R.id.rb_contack:
                rbConver.setTextColor(Color.BLACK);
                rbContack.setTextColor(Color.GREEN);
                rbPerson.setTextColor(Color.BLACK);
                getSupportFragmentManager().beginTransaction().replace(R.id.contanier,contactFragment).commit();
                break;
            case R.id.rb_person:
                rbConver.setTextColor(Color.BLACK);
                rbContack.setTextColor(Color.BLACK);
                rbPerson.setTextColor(Color.GREEN);
                getSupportFragmentManager().beginTransaction().replace(R.id.contanier,personFragment).commit();
                break;
        }
    }
    private Map<String, EaseUser> getContact() {
        Map<String, EaseUser> map = new HashMap<>();
        try {
            List<String> userNames = EMClient.getInstance().contactManager().getAllContactsFromServer();
//            KLog.e("......有几个好友:" + userNames.size());
            for (String userId : userNames) {
//                KLog.e("好友列表中有 : " + userId);
                map.put(userId, new EaseUser(userId));
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return map;
    }

}
