package com.example.lab6_currencyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrencyAdapter extends ArrayAdapter<CurrencyItem>{

    private ArrayList<CurrencyItem> currencyList;

    public CurrencyAdapter(Context context, int textViewResourceId, ArrayList<CurrencyItem> currencyList){
        super(context,textViewResourceId,currencyList);
        this.currencyList=currencyList;
    }
    public View getView(int position,View convertView, ViewGroup parent){

        View v=convertView;

        //check to see if view is null. If it is, inflate it
        //inflating means to show the view
        if(v==null)
        {
            LayoutInflater inflater=(LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.list_item,null);
        }

        CurrencyItem i=currencyList.get(position);

        if(i!=null)
        {
            TextView tvBase=v.findViewById(R.id.tvBase);
            TextView tvValue=v.findViewById(R.id.tvValue);

            tvBase.setText(i.getBase());
            tvValue.setText(String.valueOf(i.getValue()));
        }
        return v;
    }


}
