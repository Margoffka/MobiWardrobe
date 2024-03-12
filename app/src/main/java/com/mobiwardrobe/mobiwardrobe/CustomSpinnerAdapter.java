package com.mobiwardrobe.mobiwardrobe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<CharSequence> {
    private LayoutInflater inflater;
    private int textColor;

    public CustomSpinnerAdapter(Context context, int resource, List<CharSequence> items, int textColor) {
        super(context, resource, items);
        inflater = LayoutInflater.from(context);
        this.textColor = textColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view;
        textView.setTextColor(textColor);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        TextView textView = (TextView) view;
        textView.setText(getItem(position));
        textView.setTextColor(textColor);
        return view;
    }
}
