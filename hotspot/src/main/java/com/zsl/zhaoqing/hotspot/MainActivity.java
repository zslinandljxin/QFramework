package com.zsl.zhaoqing.hotspot;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zsl.zhaoqing.hotspot.fragments.MainFragment;

/**
 * Created by Administrator on 2017/11/13.
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.root_container, new MainFragment())
                .commit();
    }


}