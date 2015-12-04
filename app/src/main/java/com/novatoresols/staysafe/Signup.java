package com.novatoresols.staysafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.novatoresols.staysafe.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Signup extends AppCompatActivity {

    EditText user;
    EditText password;
    EditText Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user=(EditText)findViewById(R.id.edtUser);
        password=(EditText)findViewById(R.id.edtEmail);
        Email=(EditText)findViewById(R.id.edtemail);

    }


   public void createAccount(View view){

       if (user.getText().toString().length()==0) {

           user.setError("Enter User Name");
           user.requestFocus();

       }
       else if (password.getText().toString().length()==0) {

           password.setError("Enter Password");
           password.requestFocus();

       }
      else if (Email.getText().toString().length()==0) {

           Email.setError("Enter Email");
           Email.requestFocus();

       }
       else {

           User u=new User();
           u.setName(user.getText().toString());
           u.setPassword(password.getText().toString());
           u.setEmail(Email.getText().toString());

           SharedPreferences ulist;
           List<User> aList;
           ulist = getSharedPreferences("UserRecords", Context.MODE_PRIVATE);
           String jsonFavorite = ulist.getString("ulist", null);
           GsonBuilder gsonb = new GsonBuilder();
           Gson gso = new Gson();
           User[]  favoriteItems = gso.fromJson(jsonFavorite, User[].class);
           aList = Arrays.asList(favoriteItems);
           aList = new ArrayList(aList);

           SharedPreferences prefs = getSharedPreferences("UserRecords", Context.MODE_PRIVATE);
           SharedPreferences.Editor editor = prefs.edit();

           aList.add(u);

           Gson gson = new Gson();
           String jsonFavorites = gson.toJson(aList);
           editor.putString("ulist", jsonFavorites);
           editor.commit();


           Toast.makeText(this,"Your Account Created Successfuly",Toast.LENGTH_LONG).show();

           SharedPreferences cpref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
           SharedPreferences.Editor ceditor = cpref.edit();
           ceditor.putString("n", user.getText().toString());
           ceditor.putString("p",password.getText().toString());
           ceditor.putString("e", Email.getText().toString());
           ceditor.putBoolean("isLoginIN", false);
           ceditor.commit();






           Intent i=new Intent(this,MainActivity.class);
           startActivity(i);
           this.finish();


       }

   }


}
