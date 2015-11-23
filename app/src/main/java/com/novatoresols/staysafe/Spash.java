package com.novatoresols.staysafe;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import DB.TinyDB;

public class Spash extends AppCompatActivity {

    final String PREFS_NAME = "MyPrefsFile";
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR);
       // requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_spash);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            Log.d("Comments", "First time");
            AssetManager am = this.getAssets();
            List<LatLng> list = new ArrayList();
            try {
                InputStream is = am.open("accidents.txt");
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    String parts[] = line.split(",");
                    Double v1;
                    Double v2;
                    if (parts[0].toString().equals("")){v1=0.0;}
                    else {v1 = Double.parseDouble(parts[0].toString().trim());}
                    if (parts[0].toString().equals("")){v2=0.0;}
                    else { v2 = Double.parseDouble(parts[1].toString().trim());}


                    LatLng obj = new LatLng(v1, v2);
                    list.add(obj);

                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

           // TinyDB db=new TinyDB(this);
            SharedPreferences prefs = getSharedPreferences("a", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            Gson gson = new Gson();
            String jsonFavorites = gson.toJson(list);
            editor.putString("alist", jsonFavorites);
            editor.commit();

            /*//Reading The List
            SharedPreferences getList;
            List<LatLng> aList;
            getList = getSharedPreferences("a", Context.MODE_PRIVATE);
            String jsonFavorite = getList.getString("alist", null);
            Gson gso = new Gson();
            LatLng [] favoriteItems = gso.fromJson(jsonFavorite,LatLng[].class);
            aList = Arrays.asList(favoriteItems);
            aList = new ArrayList(aList);
*/



            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
            Intent i=new Intent(this,Login.class);
            startActivity(i);
        }
        else {
            new android.os.Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(Spash.this, Login.class);
                    startActivity(i);
                    // close this activity
                    finish();
                    // progressBar1.setVisibility(View.GONE);
                }
            }, SPLASH_TIME_OUT);
        }



    }
}
