package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class DonateActivity extends AppCompatActivity {

    TextView tvDonationStatement;
    ListView recipientList;
    TextView tvDonationQuantityLeft;
    TextView error;
    ArrayList<UserRequest> userRequests;
    ArrayList<Integer> clickedOnPositions;
    ArrayList<Integer> clickedOnValues;
    Button btnConfirmDonation;
    Button btnDonate;

    Integer userId;
    Integer donationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        userId = Integer.parseInt(sp.getString("userId", ""));

        //setup page
        tvDonationStatement = findViewById(R.id.DonationStatement);
        recipientList = findViewById(R.id.lvRecipientList);
        tvDonationQuantityLeft = findViewById(R.id.tvDonateQuantityLeft);
        btnConfirmDonation = findViewById(R.id.btnDialogDonte);
        btnDonate= findViewById(R.id.btnDonate);

        String text = getResources().getQuantityString(R.plurals.DonationMessage, Integer.parseInt(getResourceQuantity()), Integer.parseInt(getResourceQuantity()), getResourceName());
        tvDonationStatement.setText(text);
        tvDonationQuantityLeft.setText(getResourceQuantity());

        setupRequestlist();
        clickedOnPositions= new ArrayList<>();
        clickedOnValues = new ArrayList<>();



        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDonationtoDB(userId,getResourceName(), Integer.parseInt(getResourceQuantity()));
            }
        });
    }


    public String getResourceName() {
        Bundle extra = getIntent().getExtras();
        if (extra.getString("donateResource") != null) {
            return extra.getString("donateResource");
        } else {
            return "";
        }
    }
    public String getResourceQuantity() {
        Bundle extra = getIntent().getExtras();
        if (extra.getString("donateQuantity") != null) {
            return extra.getString("donateQuantity");
        } else {
            return "";
        }
    }
    public void setupRequestlist() {

        DatabaseHelper.getResourceId(this, getResourceName() , new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                DatabaseHelper.getRequests(getApplicationContext(), userId, Integer.parseInt(response), new DatabaseHelper.Callback() {
                    @Override
                    public void myResponseCallback(String response) throws JSONException {
                        if (!response.equals("error")){

                            userRequests= DatabaseHelper.makeRequestList(response);
                            RecipientListAdapter adapter = new RecipientListAdapter(getApplicationContext(), R.layout.recipient_list_item, userRequests);
                            recipientList.setAdapter(adapter);

                        }
                    }
                });
            }
        });


        recipientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                UserRequest request = userRequests.get(position);
                makeRequestDialog(request, position);
            }
        });

    }
    public void makeRequestDialog(UserRequest request, Integer position) {

        // Make alert dialog for onClick
        AlertDialog.Builder builder = new AlertDialog.Builder(DonateActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.recipient_dialog, null);
        builder.setView(dialogView);

        //set up dialog box
        TextView fname = dialogView.findViewById(R.id.tvRecipientDialogFirstName);
        TextView surname = dialogView.findViewById(R.id.tvRecipientDialogSurname);
        TextView quantityRequested = dialogView.findViewById(R.id.tvRecipientDialogRequestedQuantity);
        TextView biography = dialogView.findViewById(R.id.tvRecipientDialogBio);
        EditText etQuantityDonated = dialogView.findViewById(R.id.etRecipientDialogDonateQuantity);
        Button btnDonate = dialogView.findViewById(R.id.btnDialogDonte);
        error = dialogView.findViewById(R.id.tvdialogError);


        UserRequest userRequest = userRequests.get(position);
        fname.setText(userRequest.firstName);
        surname.setText(userRequest.surname);
        quantityRequested.setText(userRequest.amountRequested.toString());
        biography.setText(userRequest.getBiography());


        AlertDialog alertDialog = builder.create();

        final Integer current_value;
        if (clickedOnPositions.contains(position)){
            current_value= clickedOnValues.get(clickedOnPositions.indexOf(position));
        }
        else{
            current_value=0;
        }

        etQuantityDonated.setText(current_value.toString());

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (! etQuantityDonated.getText().toString().isEmpty() && checkDonationValue(Integer.parseInt(etQuantityDonated.getText().toString()),
                        Integer.parseInt(quantityRequested.getText().toString()) ,Integer.parseInt(tvDonationQuantityLeft.getText().toString())+ current_value)) {

                    if (clickedOnPositions.contains(position)){ // if the user has already clicked on the recipient then just update the value

                        if ( Integer.parseInt(etQuantityDonated.getText().toString()) ==0){
                            Integer test = clickedOnPositions.indexOf(position);
                            clickedOnValues.remove(test);
                            clickedOnPositions.remove(test);
                        }
                        else {
                            clickedOnValues.set(clickedOnPositions.indexOf(position), Integer.parseInt(etQuantityDonated.getText().toString()));
                        }


                    }
                    else if (Integer.parseInt(etQuantityDonated.getText().toString())!=0) {
                        clickedOnPositions.add(position);
                        clickedOnValues.add(Integer.parseInt(etQuantityDonated.getText().toString()));
                    }

                    Integer quantityLeft = Integer.parseInt(tvDonationQuantityLeft.getText().toString()) - Integer.parseInt(etQuantityDonated.getText().toString()) +current_value ;
                    tvDonationQuantityLeft.setText(quantityLeft.toString());

                    alertDialog.dismiss();

                }
            }
        });

        alertDialog.show();
    }
    public void addDonationtoDB(Integer userId, String resourceName, Integer quantityDonated ){

        DatabaseHelper.getResourceId(this, resourceName, new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {

                Integer resourceId = Integer.parseInt(response);
                DatabaseHelper.addDonation(getApplicationContext(), userId, resourceId, quantityDonated, new DatabaseHelper.Callback() {
                    @Override
                    public void myResponseCallback(String response) throws JSONException {
                        donationId= Integer.parseInt(response);
                        DatabaseHelper.updateUserDonationAmount(getApplicationContext(), userId, Integer.parseInt(getResourceQuantity()));


                        for (int j=0; j< clickedOnPositions.size(); j++) {
                            addTransactions(j);
                        }
                        Intent Transactionintent = new Intent(getApplicationContext(), TransactionActivity.class);
                        startActivity(Transactionintent);
                        finish();
                    }
                });

            }
        });
    }
    public void addTransactions(Integer i){

        if (clickedOnPositions.isEmpty()){
            return;
        }
        Integer requestId = userRequests.get(i).requestId;
        Integer quantity = clickedOnValues.get(i);

        DatabaseHelper.getResourceId(getApplicationContext(), getResourceName(), new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {
                Integer resourceId = Integer.parseInt(response);
                DatabaseHelper.addTransactions(getApplicationContext(), requestId, donationId,  quantity, resourceId);
                DatabaseHelper.updateRequest(getApplicationContext(), requestId, quantity);
                DatabaseHelper.updateDonation(getApplicationContext(), donationId, quantity);
            }
        });

    }
    public Boolean checkDonationValue(Integer donationValue, Integer quantityRequested, Integer donationsLeft){
        // check to see if donation value is allowed

        if (donationValue.toString().isEmpty()) {
            error.setText("Please choose a nonzero number to donate");
            return false;
        }

        if (donationValue> quantityRequested){
            error.setText("You can not donate more than what the user has requested");
            return false;
        }

        if (donationValue> donationsLeft){
            error.setText("You can not donate more than you have pledged");
            return false;
        }

        return true;


    }
}


