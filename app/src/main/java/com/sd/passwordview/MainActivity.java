package com.sd.passwordview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private FPasswordView mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPasswordView = findViewById(R.id.view_password);

        mPasswordView.setItemBackgroundResource(R.drawable.bg);
    }
}
