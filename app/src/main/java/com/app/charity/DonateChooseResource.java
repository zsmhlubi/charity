package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import java.util.List;

public class DonateChooseResource extends AppCompatActivity {

    //These two values will be passed on to the recipients class
    String requestQuantity;
    String requestType;

    TextView donateResourceSearch; //will hold currently selected resource
    ArrayList<String> resourceList; //list of all possible resources
    EditText resourceQuantity;
    ListView resourceListSearchList;
    Button donateChooseResourceButton;
    ArrayList resourceArrayList;
    ArrayAdapter<String>  adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_choose_resource);


        //page set up
        resourceListSearchList= findViewById(R.id.donate_choose_resourceList);
        donateResourceSearch = findViewById(R.id.donate_choose_searchBar);
        resourceQuantity=findViewById(R.id.donate_choose_resourceQuantity);
        donateChooseResourceButton = findViewById(R.id.donate_choose_donateButton);

        //list set up
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
                adapter  = new ArrayAdapter<String>(DonateChooseResource.this, android.R.layout.simple_list_item_1, resourcesNames);
                resourceListSearchList.setAdapter(adapter);
            }
        });
        donateResourceSearch.addTextChangedListener(new TextWatcher() {
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
        donateResourceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resourceListSearchList.getVisibility()==View.GONE) {
                    donateResourceSearch.setText("");
                    resourceListSearchList.setVisibility(View.VISIBLE);
                }
            }
        });
        //Save resource choice in searchbox text
        resourceListSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                donateResourceSearch.setText(adapter.getItem(i));
                resourceListSearchList.setVisibility(View.GONE);
            }
        });

        //donate button setup
        donateChooseResourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast= Toast.makeText(DonateChooseResource.this, "", Toast.LENGTH_LONG);

                if (donateResourceSearch.getText().toString().equals("")){
                    toast.setText("Please select a resource");
                    toast.show();
                    return;
                }

                if (!resourceArrayList.contains(donateResourceSearch.getText().toString())){
                    toast.setText("Please choose a resource item already on the list");
                    toast.show();
                    return;
                }

                if (resourceQuantity.getText().toString().equals("")){
                    toast.setText("Please choose how much you want to donate");
                    toast.show();
                    return;
                }

                if (resourceQuantity.getText().toString().equals("0")){
                    toast.setText("Please choose a value other than zero to donate");
                    toast.show();
                    return;
                }

                else{
                    requestQuantity= resourceQuantity.getText().toString();
                    requestType = donateResourceSearch.getText().toString();
                    Intent intent_choose_recipients = new Intent(getApplicationContext(), DonateActivity.class);
                    intent_choose_recipients.putExtra("donateResource", requestType);
                    intent_choose_recipients.putExtra("donateQuantity", requestQuantity);
                    startActivity(intent_choose_recipients);
                    finish();
                }
            }
        });
    }
}

