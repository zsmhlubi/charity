package com.app.charity;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecipientListAdapter extends ArrayAdapter<UserRequest> {

    private ArrayList<UserRequest> recipientList;

    private Context mContext;
    int mResource;

    public RecipientListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserRequest> items) {
        super(context, resource, items);
        mContext=context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get data from ArrayList

        UserRequest userRequest  =getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvFname = convertView.findViewById(R.id.tvRecipientListFirstName);
        TextView tvSurname = convertView.findViewById(R.id.tvRecipientListSurname);
        TextView tvQuantityRequested = convertView.findViewById(R.id.RecipientListQuantityRequested);
        TextView tvBiography = convertView.findViewById(R.id.RecipientListBiography);


        tvFname.setText(userRequest.firstName);
        tvSurname.setText(userRequest.surname);
        tvQuantityRequested.setText(userRequest.amountRequested.toString());
        tvBiography.setText(userRequest.getBiography());

        return convertView;
    }




    }
