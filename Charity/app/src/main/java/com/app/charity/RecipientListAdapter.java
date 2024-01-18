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

import java.util.ArrayList;
import java.util.List;

public class RecipientListAdapter extends ArrayAdapter<userRequest> {

    private ArrayList<userRequest> recipientList;

    private Context mContext;
    int mResource;

    public RecipientListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<userRequest> items) {
        super(context, resource, items);
        mContext=context;
        mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // get data from ArrayList

        Integer userId  =getItem(position).userId;

        User user = DatabaseHelper.makeUserInstance(userId);
        String fname = user.firstname;
        String surname = user.surname;
        Integer amountRequested = getItem(position).amountRequested;
        String biography = user.biography;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvFname = convertView.findViewById(R.id.tvRecipientListFirstName);
        TextView tvSurname = convertView.findViewById(R.id.tvRecipientListSurname);
        TextView tvQuantityRequested = convertView.findViewById(R.id.RecipientListQuantityRequested);
        TextView tvBiography = convertView.findViewById(R.id.RecipientListBiography);

        tvFname.setText(fname);
        tvSurname.setText(surname);
        tvQuantityRequested.setText(amountRequested.toString());
        tvBiography.setText(biography);
        return convertView;
    }




    }
