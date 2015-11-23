package com.novatoresols.staysafe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    TextView retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!isNetworkAvailable()){
            setContentView(R.layout.activity_no_internet);
            retry = (TextView) findViewById(R.id.activity_no_internet_retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Login.this.recreate();
                }
            });
            return;
        }

    }

    public void goTOMain(View v)
    {
        Intent i=new Intent(Login.this,MainActivity.class);
        startActivity(i);
        finish();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
