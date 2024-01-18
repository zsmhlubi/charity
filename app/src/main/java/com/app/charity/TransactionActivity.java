package com.app.charity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    Integer userId;
    ArrayList TransactionArrayList;
    ListView TransactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);


        SharedPreferences sp = getApplicationContext().getSharedPreferences("myUserPrefs", Context.MODE_PRIVATE);
        userId = Integer.parseInt(sp.getString("userId", ""));

        Runnable r = new Runnable() {
            @Override
            public void run(){
                TransactionList = findViewById(R.id.transactionList);

                DatabaseHelper.getTransactions(getApplicationContext(), userId, new DatabaseHelper.Callback() {
                    @Override
                    public void myResponseCallback(String response) throws JSONException {

                        ArrayList<Transaction> transactions = DatabaseHelper.makeTransactionList(response);
                        TransactionListAdapter adapter = new TransactionListAdapter(getApplicationContext(), R.layout.transactions_list_item, transactions);
                        TransactionList.setAdapter(adapter);
                    };
                });
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 1000); // <-- the "1000" is the delay time in miliseconds.



    }
}
