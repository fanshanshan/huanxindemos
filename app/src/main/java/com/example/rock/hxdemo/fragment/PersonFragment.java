package com.example.rock.hxdemo.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.rock.hxdemo.LoginActivity;
import com.example.rock.hxdemo.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment implements View.OnClickListener{


    private ImageView ivHead;
    private TextView tvName;
    private Button btnLogout;
    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);
        ivHead = v.findViewById(R.id.iv_head);
        tvName = v.findViewById(R.id.tv_name);
        btnLogout = v.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
        Glide.with(getActivity()).load(sharedPreferences.getString("url","")).into(ivHead);
        tvName.setText(sharedPreferences.getString("nick",""));
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_logout:
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("user","").commit();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub

                    }
                });
                break;
        }
    }
}
