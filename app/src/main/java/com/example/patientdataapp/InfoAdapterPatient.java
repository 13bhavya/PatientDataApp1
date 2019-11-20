package com.example.patientdataapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class InfoAdapterPatient extends ArrayAdapter {
    List list = new ArrayList<>();

    public InfoAdapterPatient(@NonNull Context context, int resource) {
        super(context, resource);
    }


    public void add(InfoDataPatient object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row;
        row = convertView;
        InfoHolder infoHolder;

        if (row == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_patient,parent,false);
            infoHolder = new InfoHolder();
            infoHolder.tx_id = row.findViewById(R.id.idi);
            infoHolder.tx_first = row.findViewById(R.id.first_name);
            infoHolder.tx_last = row.findViewById(R.id.last_name);

            row.setTag(infoHolder);
        }
        else {
            infoHolder = (InfoHolder) row.getTag();
        }
        InfoDataPatient infoData = (InfoDataPatient) this.getItem(position);
        //infoHolder.tx_id.setText(infoData.getId());
        infoHolder.tx_first.setText(infoData.getFirstName());
        infoHolder.tx_last.setText(infoData.getLastName());

        return row;

    }

    static class InfoHolder
    {
        TextView tx_id, tx_first, tx_last;
    }
}