package com.app.charity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;

public class TransactionListAdapter  extends ArrayAdapter<Transaction> {

    private ArrayList<Transaction> transactionsList;

    private Context mContext;
    int mResource;

    public TransactionListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Transaction> items) {
        super(context, resource, items);
        mContext=context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get data from ArrayList

        Transaction transaction  =getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);
        TextView firstname=  convertView.findViewById(R.id.transactionListFirstName);
        TextView surname = convertView.findViewById(R.id.transactionSurname);
        TextView resourceName = convertView.findViewById(R.id.transactionsResource);
        TextView quantity = convertView.findViewById(R.id.transactionsQuantity);
        TextView phone = convertView.findViewById(R.id.transactionsPhone);
        Button clear = convertView.findViewById(R.id.transactionClear);


        DatabaseHelper.getUser(getContext(), transaction.recipientID, new DatabaseHelper.Callback() {
            @Override
            public void myResponseCallback(String response) throws JSONException {

                User user= DatabaseHelper.makeUserList(response).get(0);
                firstname.setText(user.firstname+" ");
                surname.setText(user.surname);
                phone.setText(user.phone);
                quantity.setText(transaction.quantityDonated.toString());
                DatabaseHelper.getResourceName(getContext(), transaction.resourceID, new DatabaseHelper.Callback() {
                    @Override
                    public void myResponseCallback(String response) throws JSONException {
                        resourceName.setText(response);
                    }
                });
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper.updateTransaction(getContext(), transaction.transactionID);
                remove(transaction);
            }
        });
        return convertView;
    }
}
