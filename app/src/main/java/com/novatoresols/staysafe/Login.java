package com.novatoresols.staysafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.novatoresols.staysafe.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    TextView retry;
    EditText name;
    EditText pass;
    List<User> aList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences = getSharedPreferences("UserSession",0);
        if (!preferences.getBoolean("isLoginIN",true)) {

            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            finish();

        }


            name=(EditText)findViewById(R.id.edtUser);
        pass=(EditText)findViewById(R.id.edtEmail);

        SharedPreferences ulist;
        ulist = getSharedPreferences("UserRecords", Context.MODE_PRIVATE);
        String jsonFavorite = ulist.getString("ulist", null);
        GsonBuilder gsonb = new GsonBuilder();
        Gson gso = new Gson();
        User[]  favoriteItems = gso.fromJson(jsonFavorite, User[].class);
        aList = Arrays.asList(favoriteItems);
        aList = new ArrayList(aList);


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
        //Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibe.vibrate(100);



        if (name.getText().toString().trim().length()==0) {
            name.setError("Enter User Name");
            name.requestFocus();
        }
        if (name.getText().toString().trim().length()==0) {
            pass.setError("Enter Password");
            pass.requestFocus();
        }
        else {

            for(int i=0;i<aList.size();i++) {

                User a=aList.get(i);

                if (!a.getName().equalsIgnoreCase(name.getText().toString().trim()) && !a.getPassword().equalsIgnoreCase(pass.getText().toString().trim())) {

                    name.setError("Enter Correct Credentials");
                    name.requestFocus();
                }
                if (a.getName().equalsIgnoreCase(name.getText().toString().trim()) && a.getPassword().equalsIgnoreCase(pass.getText().toString().trim())) {

                    SharedPreferences cpref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ceditor = cpref.edit();
                    ceditor.putString("n",a.getName());
                    ceditor.putString("p",a.getPassword());
                    ceditor.putString("e",a.getEmail());
                    ceditor.putBoolean("isLoginIN", false);
                    ceditor.commit();

                    Intent ii = new Intent(Login.this, MainActivity.class);
                    startActivity(ii);
                    finish();
                }



                }

            }




        }






    public void forgotMethod(View v) {
        //Toast.makeText(this,"Comming Soon",Toast.LENGTH_SHORT).show();
    }

    public void createAccount(View v) {

        //Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //vibe.vibrate(100);
        Intent i=new Intent(Login.this,Signup.class);
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
