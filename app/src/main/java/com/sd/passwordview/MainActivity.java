package com.sd.passwordview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sd.lib.passwordview.FPasswordView;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FPasswordView view_password_1, view_password_2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_password_1 = findViewById(R.id.view_password_1);
        view_password_2 = findViewById(R.id.view_password_2);

        view_password_1.setCallback(new FPasswordView.Callback()
        {
            @Override
            public void onTextChanged(String text)
            {
                Log.i(TAG, "onTextChanged:" + text);
            }
        });
    }
}
