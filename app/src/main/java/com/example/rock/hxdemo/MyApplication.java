package com.example.rock.hxdemo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;

import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.hyphenate.util.Utils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Rock on 2017/12/29.
 */

public class MyApplication extends Application{
    private static MyApplication instance;
    private EaseUI easeUI;
    private SharedPreferences sharedPreferences;
    private Map<String, EaseUser> contactList;




    public static MyApplication getInstance(){
        if(instance==null){
            instance = new MyApplication();
        }
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
        //options.setMipushConfig("2882303761517500800", "5371750035800");//小米推送的
        // 默认添加好友时，是不需要验证的，改成需要验证,true:自动验证,false,手动验证
        options.setAcceptInvitationAlways(true);
        //初始化
        EaseUI.getInstance().init(this, options);
        easeUI = EaseUI.getInstance();
        sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        setEaseUIProviders();
        //在做打包混淆时，关闭debug模式，避免消耗不必要的
        registerMessageListener();
    }



    protected void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }
    private EaseUser getUserInfo(String username){
        //获取 EaseUser实例, 这里从内存中读取
        //如果你是从服务器中读读取到的，最好在本地进行缓存
        EaseUser user = null;
        //如果用户是本人，就设置自己的头像
        if(username.equals(EMClient.getInstance().getCurrentUser())){
            user=new EaseUser(username);
            user.setAvatar((String)sharedPreferences.getString("url",""));
            user.setNick((String)sharedPreferences.getString("nick",""));
            return user;
        }
        user=new EaseUser(username);
        String info= sharedPreferences.getString(username,"");
        if(info.split("&").length>1){
            user.setAvatar( info.split("&")[1]);
            user.setNick( info.split("&")[0]);
        }
        Log.i("zcb","头像："+user.getAvatar());
        return user;
    }
    EMMessageListener messageListener;
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //接收并处理扩展消息
                    String userName = message.getStringAttribute(Constant.USER_NAME, "");
                    String userId = message.getStringAttribute(Constant.USER, "");
                    String userPic = message.getStringAttribute(Constant.HEAD_IMAGE_URL, "");
                    String hxIdFrom = message.getFrom();
                    EaseUser easeUser = new EaseUser(hxIdFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNick(userName);
                    sharedPreferences.edit().putString(hxIdFrom,userName+"&"+userPic).commit();
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }
}
