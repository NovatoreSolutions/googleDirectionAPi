package com.novatoresols.staysafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.novatoresols.staysafe.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Profile extends AppCompatActivity {

    EditText n;
    EditText p;
    EditText e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);


        SharedPreferences ulist;
        ulist = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        String name=ulist.getString("n",null);
        String pass=ulist.getString("e",null);
        String email=ulist.getString("p",null);

        n=(EditText)findViewById(R.id.edittext_first_name);
        p=(EditText)findViewById(R.id.edittext_lastname);
        e=(EditText)findViewById(R.id.pemail);

        n.setText(name);
        p.setText(pass);
        e.setText(email);

    }

    public void backPressed(View v) {
        this.finish();
    }
    public void udpatePress(View v) {

        SharedPreferences ulist;
        List<User> aList;
        ulist = getSharedPreferences("UserRecords", Context.MODE_PRIVATE);
        String jsonFavorite = ulist.getString("ulist", null);
        GsonBuilder gsonb = new GsonBuilder();
        Gson gso = new Gson();
        User[]  favoriteItems = gso.fromJson(jsonFavorite, User[].class);
        aList = Arrays.asList(favoriteItems);
        aList = new ArrayList(aList);

        User u=new User();
        u.setName(n.getText().toString().trim());
        u.setPassword(p.getText().toString().trim());
        u.setEmail(e.getText().toString().trim());


        SharedPreferences prefs = getSharedPreferences("UserRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        aList.add(u);

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(aList);
        editor.putString("ulist", jsonFavorites);
        editor.commit();

        Toast.makeText(this,"Profile Updated Successfuly",Toast.LENGTH_LONG).show();

        this.finish();


    }

}
