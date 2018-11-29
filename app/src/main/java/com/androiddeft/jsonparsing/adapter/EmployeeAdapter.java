package com.androiddeft.jsonparsing.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androiddeft.jsonparsing.R;
import com.androiddeft.jsonparsing.beans.EmployeeDetails;
import com.squareup.picasso.Picasso;

import java.util.List;



public class EmployeeAdapter extends ArrayAdapter<EmployeeDetails> {

    private List<EmployeeDetails> dataSet;


    public EmployeeAdapter(List<EmployeeDetails> dataSet, Context mContext) {
        super(mContext, R.layout.employee_row, dataSet);

        this.dataSet = dataSet;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.employee_row, null);
        }
        EmployeeDetails employee = dataSet.get(position);
        if (employee != null) {
            //Text View references
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView image = (TextView) v.findViewById(R.id.image);
            TextView price = (TextView) v.findViewById(R.id.price);
            TextView link = (TextView) v.findViewById(R.id.link);
            TextView source = (TextView) v.findViewById(R.id.source);
            ImageView imageView = (ImageView) v.findViewById(R.id.iv_image);


            //Updating the text views
            title.setText(employee.getTitle());
            //image.setText(employee.getImage());
            price.setText(employee.getPrice());
            link.setText(employee.getLink());
            source.setText(employee.getSource());
            Picasso.get().load(employee.getImage()).into(imageView);

            //Picasso.with(getContext()).load(employee.getImage()).into(imageView);
        }

        return v;
    }
}
