package com.example.rock.hxdemo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rock.hxdemo.ChatActivity;
import com.example.rock.hxdemo.MyConnectionListener;
import com.example.rock.hxdemo.R;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends Fragment {
    private static EMMessageListener emMessageListener;
    private EaseConversationListFragment conversationFragment;
    public ConversationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        conversationFragment = new EaseConversationListFragment();
        getChildFragmentManager().beginTransaction().add(R.id.contanier,conversationFragment).commit();
        EMClient.getInstance().addConnectionListener(new MyConnectionListener(getActivity()));
        conversationFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                //进入聊天页面
                startActivity(new Intent(getActivity(), ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        return inflater.inflate(R.layout.fragment_conversation2, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EMClient.getInstance().chatManager().removeMessageListener(emMessageListener);
    }



}
