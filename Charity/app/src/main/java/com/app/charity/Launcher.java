package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Launcher extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp=getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        String userID= sp.getString("userId" , "" );

        if (userID.isEmpty())
        {
            Intent loginIntent = new Intent(getApplicationContext(), Login.class);
            startActivity(loginIntent);
            finish();
        }
        else{
            Intent mainActivityintent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivityintent);
            finish();
        }
    }
}
