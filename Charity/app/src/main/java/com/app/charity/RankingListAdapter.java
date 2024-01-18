package com.app.charity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RankingListAdapter extends ArrayAdapter<User> {

    private Context mContext;
    private int mResource;

    public RankingListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mResource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);


        TextView firstname = convertView.findViewById(R.id.ranking_row_firstname);
        TextView surname = convertView.findViewById(R.id.ranking_row_surname);
        TextView donation_amount = convertView.findViewById(R.id.ranking_row_donationNumber);
        TextView rank = convertView.findViewById(R.id.ranking_row_rank);

        if (position + 1 == 1) { //first place

            rank.setBackgroundResource(R.drawable.goldcircle);
        }

        if (position + 1 == 2) { //first place
            rank.setBackgroundResource(R.drawable.silvercircle);
        }

        if (position + 1 == 3) { //first place
            rank.setBackgroundResource(R.drawable.bronzecircle);
        }
        if (position > 2)
            convertView.setBackgroundResource(R.color.DonaterListalternateRowColor);


        firstname.setText(getItem(position).firstname);
        surname.setText(getItem(position).surname);
        donation_amount.setText(Integer.toString(getItem(position).totalDonations));

        rank.setText(Integer.toString(position + 1));

        return convertView;
    }
}
