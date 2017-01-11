package com.johnny.practice.transitionspractice.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.johnny.practice.transitionspractice.R;

public class FTransitionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftransition);

        MainFragment fragment = new MainFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
          .add(R.id.fragment_container, fragment)
          .commit();
    }
}
