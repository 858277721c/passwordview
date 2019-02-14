package com.sd.passwordview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.sd.lib.passwordview.FPasswordView;

public class MainActivity extends AppCompatActivity
{
    public static final String TAG = MainActivity.class.getSimpleName();

    private FPasswordView mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPasswordView = findViewById(R.id.view_password);

        /**
         * 设置只允许输入数字
         */
        mPasswordView.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        /**
         * 设置输入内容变化回调
         */
        mPasswordView.setCallback(new FPasswordView.Callback()
        {
            @Override
            public void onTextChanged(String text)
            {
                Log.i(TAG, "onTextChanged:" + text);

                if (text.length() == mPasswordView.getItemCount())
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
