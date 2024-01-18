package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Request extends AppCompatActivity {



    TextView requestResourceSearch; //will hold currently selected resource
    ArrayList<String> ResourceList; //list of all possible resources
    EditText requestResourceQuantity;
    ListView requestResourceListSearchList;
    Button requestChooseResourceButton;
    ArrayList resourceArrayList;
    ArrayAdapter<String> adapter;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        userId = Integer.parseInt(sp.getString("userId", ""));


        requestResourceListSearchList= findViewById(R.id.request_choose_resourceList);
        requestResourceSearch = findViewById(R.id.request_choose_searchBar);
        requestResourceQuantity=findViewById(R.id.request_choose_resourceQuantity);
        requestChooseResourceButton= findViewById(R.id.request_choose_donateButton);

        DatabaseHelper.makeResourceList(getApplicationContext(), new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {

                ArrayList<String> resourcesNames = new ArrayList<>();
                JSONArray arr = new JSONArray(response);

                for (int i=0; i< arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String resourceName = obj.getString("ResourceName");
                    resourcesNames.add(resourceName);
                }
                resourceArrayList=resourcesNames;
                adapter  = new ArrayAdapter<String>(Request.this, android.R.layout.simple_list_item_1, resourcesNames);
                requestResourceListSearchList.setAdapter(adapter);
            }
        });
        requestResourceSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Hide List when we are not using it
        requestResourceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestResourceListSearchList.getVisibility()==View.GONE) {
                    requestResourceSearch.setText("");
                    requestResourceListSearchList.setVisibility(View.VISIBLE);
                }
            }
        });
        //Save resource choice in searchbox text
        requestResourceListSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                requestResourceSearch.setText(adapter.getItem(i));
                requestResourceListSearchList.setVisibility(View.GONE);
            }
        });


        requestChooseResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast= Toast.makeText(Request.this, "", Toast.LENGTH_LONG);

                if (requestResourceSearch.getText().toString().equals("")){
                    toast.setText("Please select a resource");
                    toast.show();
                    return;
                }

                if (!resourceArrayList.contains(requestResourceSearch.getText().toString())){
                    toast.setText("Please choose a resource item already on the list");
                    toast.show();
                    return;
                }

                if (requestResourceQuantity.getText().toString().equals("")){
                    toast.setText("Please choose how much you want to donate");
                    toast.show();
                    return;
                }

                if (requestResourceQuantity.getText().toString().equals("0")){
                    toast.setText("Please choose a value other than zero to donate");
                    toast.show();
                    return;
                }

                else{

                    DatabaseHelper.getResourceId(getApplicationContext(), requestResourceSearch.getText().toString(), new DatabaseHelper.Callback() {
                        @Override
                        public void myResponseCallback(String response) throws JSONException {

                            DatabaseHelper.addRequest(getApplicationContext(), userId, Integer.parseInt(response), Integer.parseInt(
                                    requestResourceQuantity.getText().toString()));

                        }
                    });

                    Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent_main);
                    toast.show();
                    finish();
                }
            }
        });



    }
}
