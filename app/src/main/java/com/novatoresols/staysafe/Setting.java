package com.novatoresols.staysafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class Setting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    Switch mySwitch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mySwitch = (Switch) findViewById(R.id.myswitch);
        mySwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            // do something when check is selected
            Toast.makeText(this,"Accurate Location Feature is Now ON",Toast.LENGTH_LONG).show();

        } else {
            //do something when unchecked
            Toast.makeText(this,"Accurate Location Feature is Now OFF",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }




    public void updateMethod(View v) {

        Intent i=new Intent(this,Profile.class);
        startActivity(i);

    }
}
