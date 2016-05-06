package com.example.nikhil.os;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class Login extends AppCompatActivity {
    FrameLayout fragmentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentHolder = (FrameLayout)findViewById(R.id.login_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame,new LandingFragment(),"First Screen").commit();
    }
}
