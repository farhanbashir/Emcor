package com.example.fbashir.emcor.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.MyUtils;
import com.example.fbashir.emcor.models.companyDetails.Market;
import com.example.fbashir.emcor.models.companyDetails.Staff;

import java.util.ArrayList;

import static com.example.fbashir.emcor.R.id.textView;

/**
 * Created by fbashir on 7/25/2016.
 */

public class CompanyStaffListAdaptar extends ArrayAdapter<Staff>{

    //String[] color_names;
    Context context;
    ArrayList<Staff> staffs;

    public CompanyStaffListAdaptar(Activity context, ArrayList<Staff> staffs){
        super(context, R.layout.company_listview_layout, staffs);
        // TODO Auto-generated constructor stub
        //this.color_names = text;
        this.context = context;
        this.staffs = staffs;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row;
        single_row = inflater.inflate(R.layout.company_team_listview_layout, null, true);
        TextView team_name = (TextView) single_row.findViewById(R.id.company_team_name);
        TextView team_designation = (TextView) single_row.findViewById(R.id.company_team_designation);
        TextView team_phone = (TextView) single_row.findViewById(R.id.company_team_phone);
        TextView team_phone_heading = (TextView) single_row.findViewById(R.id.company_team_phone_heading);
        TextView team_email = (TextView) single_row.findViewById(R.id.company_team_email);
        TextView team_email_heading = (TextView) single_row.findViewById(R.id.company_team_email_heading);

        team_name.setText(staffs.get(position).first_name.concat(" ").concat(staffs.get(position).last_name));
        team_designation.setText(staffs.get(position).designation);
        team_phone.setText(staffs.get(position).contact_number);
        team_email.setText(staffs.get(position).email);

        final String contact_number = staffs.get(position).contact_number;

        if(!staffs.get(position).contact_number.equals(""))
        {
            team_phone.setVisibility(View.VISIBLE);
            team_phone_heading.setVisibility(View.VISIBLE);

            team_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openDialer(context, contact_number);
                }
            });

        }

        if(!staffs.get(position).email.equals(""))
        {
            team_email.setVisibility(View.VISIBLE);
            team_email_heading.setVisibility(View.VISIBLE);
        }

        if(staffs.get(position).designation != null)
        {
            team_designation.setVisibility(View.VISIBLE);
        }


        return single_row;
    }


}
