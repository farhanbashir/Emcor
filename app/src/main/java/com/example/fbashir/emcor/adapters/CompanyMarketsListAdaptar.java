package com.example.fbashir.emcor.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbashir.emcor.R;
import com.example.fbashir.emcor.helpers.BulletTextUtil;
import com.example.fbashir.emcor.models.companyDetails.Market;
import com.example.fbashir.emcor.models.divisions.DivisionsBasic;

import java.util.ArrayList;

/**
 * Created by fbashir on 7/25/2016.
 */

public class CompanyMarketsListAdaptar extends ArrayAdapter<Market>{

    //String[] color_names;
    Context context;
    ArrayList<Market> markets;

    public CompanyMarketsListAdaptar(Activity context, ArrayList<Market> markets){
        super(context, R.layout.company_listview_layout, markets);
        // TODO Auto-generated constructor stub
        //this.color_names = text;
        this.context = context;
        this.markets = markets;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View single_row;
        single_row = inflater.inflate(R.layout.company_markets_listview_layout, null, true);
        TextView textView = (TextView) single_row.findViewById(R.id.company_market_item);

        TextView item_details = (TextView) single_row.findViewById(R.id.company_market_item_detail);

        textView.setText(markets.get(position).title);

        int len = markets.get(position).child.size();
        CharSequence[] cslines = new CharSequence[len];

        for(int a=0;a<len;a++)
        {
            cslines[a] = markets.get(position).child.get(a).title;
        }

        CharSequence bulletedList = BulletTextUtil.makeBulletList(10, cslines);//BulletListUtil.makeBulletList("First line", "Second line", "Really long third line that will wrap and indent properly.");
        item_details.setText(bulletedList);


        return single_row;
    }


}
