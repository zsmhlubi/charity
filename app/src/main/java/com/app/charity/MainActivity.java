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

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Boolean fabisOpen=false;
    ListView listView_rank;
    FloatingActionButton fabMain ;
    FloatingActionButton fabRequest;
    FloatingActionButton fabDonate;
    String biography;
    Integer userId;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

        //User setup
        // get userId stored in user preferences
        SharedPreferences sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        userId = Integer.parseInt(sp.getString("userId", ""));
        System.out.println(userId);

        // handle previous activity
        String prevActivity = ProjectManager.prevActivity(getIntent());
        if (prevActivity.equals("Signup")){
            makeBiography();
        }

        //page setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabMain = findViewById(R.id.main_floating_main);
        fabRequest = findViewById(R.id.main_floating_request);
        fabDonate= findViewById(R.id.main_floating_donate);
        listView_rank=findViewById(R.id.main_listview);


        DatabaseHelper.getTopUsers(this, new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                ArrayList<User> topUsers= DatabaseHelper.makeUserList(response);

                RankingListAdapter rankingListAdapter = new RankingListAdapter(getApplicationContext(), R.layout.main_list_ranking_row, topUsers);
                listView_rank.setAdapter(rankingListAdapter);
            }
        });

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
                startActivity(intent);
            }
        });
        fabRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Request.class);
                intent.putExtra("prevActivity", "MainActivity");
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

            case R.id.menutransactions:
                Intent transIntent = new Intent(MainActivity.this , TransactionActivity.class);
                startActivity(transIntent);
                finish();


        }
        return super.onOptionsItemSelected(item);
    }
    // make Biography popup and set biography equal to user input
    public String makeBiography(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" Welcome, please set Biography");
        // Set up the input
        final EditText input = new EditText(this);

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
                    DatabaseHelper.updateBiography(getApplicationContext(), userId, biography, new DatabaseHelper.Callback() {
                        @Override
                        public void myResponseCallback(String response) throws JSONException {

                            toast.setText("You've successfully update your Biography");
                            toast.show();


                        }
                    });
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
