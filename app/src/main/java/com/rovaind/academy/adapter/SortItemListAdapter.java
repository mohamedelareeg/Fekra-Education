package com.rovaind.academy.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;

import java.util.Map;


public class SortItemListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Map<Integer , String> sortBy;
    private int selectedId;

    public SortItemListAdapter(Context context, Map<Integer , String> sortBy, int selectedId) {
        this.sortBy = sortBy;
        this.selectedId = selectedId;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sortBy.size();
    }

    @Override
    public Object getItem(int i) {
        return sortBy.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.sort_filter_listitem, null);
        holder.name = rowView.findViewById(R.id.name);
        holder.tick = rowView.findViewById(R.id.tick);

        holder.name.setText(sortBy.get(position));

        if (position == selectedId) {
            holder.tick.setVisibility(View.VISIBLE);
        } else {
            holder.tick.setVisibility(View.GONE);
        }

        return rowView;
    }

    public class Holder {
        TextViewAr name;
        ImageView tick;
    }
}
