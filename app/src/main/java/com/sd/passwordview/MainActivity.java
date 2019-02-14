package com.sd.passwordview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sd.lib.passwordview.FPasswordView;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FPasswordView view_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_password = findViewById(R.id.view_password);

        view_password.setCallback(new FPasswordView.Callback()
        {
            @Override
            public void onTextChanged(String text)
            {
                Log.i(TAG, "onTextChanged:" + text);
            }
        });
    }
}
