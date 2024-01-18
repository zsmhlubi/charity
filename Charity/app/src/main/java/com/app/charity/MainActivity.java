package com.app.charity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.controls.actions.FloatAction;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Boolean fabisOpen=false;
    ListView listView_rank;
    FloatingActionButton fabMain ;
    FloatingActionButton fabRequest;
    FloatingActionButton fabDonate;
    String biography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //User setup
        // get userId stored in user preferences
        SharedPreferences sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
            Integer userId = Integer.parseInt(sp.getString("userId", "")); //sp.getString("userId", "")

        // handle previous activity
        String prevActivity = ProjectManager.prevActivity(getIntent());
        if (prevActivity.equals("Signup")){
            DatabaseHelper.saveBiographytoDB(userId, makeBiography());
        }

        DatabaseHelper.addUsertoDB(this, "harry", "Potter", "ulrichmain28@gmail.com", "0833576032", "TerribleTittles", "981028601111");
        DatabaseHelper.getUserID(this, "ulrichmain28@gmail.com" ,"0833576032", "TerribleTittles");


        //page setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabMain = findViewById(R.id.main_floating_main);
        fabRequest = findViewById(R.id.main_floating_request);
        fabDonate= findViewById(R.id.main_floating_donate);
        listView_rank=findViewById(R.id.main_listview);

        RankingListAdapter rankingListAdapter = new RankingListAdapter(this, R.layout.main_list_ranking_row, DatabaseHelper.getRankingList());
        listView_rank.setAdapter(rankingListAdapter);

        //onClicks
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView fabRequestText= findViewById(R.id.main_floating_requestText);
                TextView fabDonateText= findViewById(R.id.main_floating_donateText);
                if (!fabisOpen){
                    fabRequest.setVisibility(View.VISIBLE); fabDonate.setVisibility(View.VISIBLE);
                    fabDonateText.setVisibility(View.VISIBLE); fabRequestText.setVisibility(View.VISIBLE);
                    fabRequestText.bringToFront(); fabDonateText.bringToFront();
                    fabisOpen=true;
                }
                else{
                    fabRequest.setVisibility(View.GONE); fabDonate.setVisibility(View.GONE); fabDonate.bringToFront();
                    fabDonateText.setVisibility(View.GONE); fabRequestText.setVisibility(View.GONE);
                    fabisOpen=false;
                }
            }
        });
        fabDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DonateChooseResource.class);
                intent.putExtra("prevActivity", "MainActivity");
                intent.putExtra("userId", userId.toString());
                startActivity(intent);
            }
        });

    }

    // Methods for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                SharedPreferences sp=getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("userId", "");
                editor.commit();
                Intent intent = new Intent(MainActivity.this , Login.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.editBiography:
                makeBiography();

        }
        return super.onOptionsItemSelected(item);
    }



    // make Biography popup and set biography equal to user input
    public String makeBiography(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Welcome, please set Biography");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().length() > 80){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please limit your biography to 80 characters, currently it is " + input.getText().toString().length(), Toast.LENGTH_LONG);
                    toast.show();
                    makeBiography();
                }
                else {
                    biography = input.getText().toString();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        return biography;
    }

}
