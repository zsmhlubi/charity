package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DonateActivity extends AppCompatActivity {

    TextView tvDonationStatement;
    ListView recipientList;
    TextView tvDonationQuantityLeft;
    TextView error;
    ArrayList<userRequest> userRequests;
    ArrayList<Integer> clickedOnPositions;
    ArrayList<Integer> clickedOnValues;
    Button btnConfirmDonation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        //setup page
        tvDonationStatement = findViewById(R.id.DonationStatement);
        recipientList = findViewById(R.id.lvRecipientList);
        tvDonationQuantityLeft = findViewById(R.id.tvDonateQuantityLeft);
        btnConfirmDonation = findViewById(R.id.btnDialogDonte);

        // get resource type and quantity selected on previous page
        String text = getResources().getQuantityString(R.plurals.DonationMessage, Integer.parseInt(getResourceQuantity()), Integer.parseInt(getResourceQuantity()), getResourceName());
        tvDonationStatement.setText(text);
        tvDonationQuantityLeft.setText(getResourceQuantity());


        //setup list
        userRequests = DatabaseHelper.makeRequestArray(DatabaseHelper.getResourceId("coffee"));
        setupRequestlist(getResourceName());
        clickedOnPositions= new ArrayList<>();
        clickedOnValues = new ArrayList<>();



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
    // make array of all requests for particular value
    public void setupRequestlist(String resource) {
        RecipientListAdapter adapter = new RecipientListAdapter(this, R.layout.recipient_list_item, userRequests);
        recipientList.setAdapter(adapter);
        recipientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                userRequest request = userRequests.get(position);
                makeRequestDialog(request, position);
            }
        });

    }

    public void makeRequestDialog(userRequest request, Integer position) {

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

        User user =DatabaseHelper.makeUserInstance(userRequests.get(position).userId);
        fname.setText(user.firstname);
        surname.setText(user.surname);
        biography.setText(user.biography);
        quantityRequested.setText(request.amountRequested.toString());
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

                    if (clickedOnPositions.contains(position)){ // we just update the stored value
                        clickedOnValues.set(clickedOnPositions.indexOf(position), Integer.parseInt(etQuantityDonated.getText().toString()));
                    }
                    else {
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
    public Boolean checkDonationValue(Integer donationValue, Integer quantityRequested, Integer donationsLeft){
        // check to see if donation value is allowed

        if (donationValue.toString().isEmpty()) {
            error.setText("Please choose a nonzero number to donate");
            return false;
        }

        if (donationValue> quantityRequested){
            error.setText("You can not donate more than what is requested");
            return false;
        }

        if (donationValue> donationsLeft){
            error.setText("You can not donate more than you have pledged");
            return false;
        }

        return true;


    }


}


