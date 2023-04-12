package com.rovaind.academy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;


import com.rovaind.academy.R;
import com.rovaind.academy.Utils.views.TextViewAr;

import java.util.HashMap;
import java.util.List;


public class FilterItemListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private List<String> departmentFilter, companyFilter,packFilter ,statusFilter;
//departmentFilter, occasionFilter ,materialFilter
    public FilterItemListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData, List<String> departmentFilter, List<String> companyFilter , List<String> packFilter , List<String> statusFilter) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.departmentFilter = departmentFilter;
        this.companyFilter = companyFilter;
        this.packFilter = packFilter;
        this.statusFilter = statusFilter;
    }
    public FilterItemListAdapter(Context context, List<String> listDataHeader) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        //this._listDataChild = listChildData;
        //this.sizeFilter = sizeFilter;
        //this.colorFilter = colorFilter;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.sort_filter_listitem, null);
        }

        TextViewAr txtListChild = convertView
                .findViewById(R.id.name);

        // Set Tick Visibility
        ImageView img = convertView.findViewById(R.id.tick);
        switch (groupPosition) {
            case 0://
                try {
                    if (departmentFilter.contains(childText)) {
                        img.setVisibility(View.VISIBLE);
                    } else {
                        img.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    img.setVisibility(View.GONE);
                }
                break;

            case 1:
                try {
                    if (companyFilter.contains(childText)) {
                        img.setVisibility(View.VISIBLE);
                    } else {
                        img.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    img.setVisibility(View.GONE);
                }
                break;
            case 2:
                try {
                    if (packFilter.contains(childText)) {
                        img.setVisibility(View.VISIBLE);
                    } else {
                        img.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    img.setVisibility(View.GONE);
                }
            case 3:
                try {
                    if (statusFilter.contains(childText)) {
                        img.setVisibility(View.VISIBLE);
                    } else {
                        img.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    img.setVisibility(View.GONE);
                }
                break;
        }

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.sort_filter_listitem, null);
        }

        // Set Arrow
        ImageView arrow = convertView.findViewById(R.id.arrow);
        arrow.setVisibility(View.VISIBLE);
        if (isExpanded) {
            arrow.setImageResource(R.drawable.ic_chevron_down_grey600_24dp);
        } else {
            arrow.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
        }

        // Set Count
        TextViewAr count = convertView
                .findViewById(R.id.count);
        switch (groupPosition) {
            case 0: // department
                try {
                    if (departmentFilter.size() > 0) {
                        count.setVisibility(View.VISIBLE);
                        count.setText(String.valueOf(departmentFilter.size()));
                    } else {
                        count.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    count.setVisibility(View.GONE);
                }
                break;

            case 1: // company
                try {
                    if (companyFilter.size() > 0) {
                        count.setVisibility(View.VISIBLE);
                        count.setText(String.valueOf(companyFilter.size()));
                    } else {
                        count.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    count.setVisibility(View.GONE);
                }
                break;

            case 2: // pack
                try {
                    if (packFilter.size() > 0) {
                        count.setVisibility(View.VISIBLE);
                        count.setText(String.valueOf(packFilter.size()));
                    } else {
                        count.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    count.setVisibility(View.GONE);
                }
                break;
            case 3: // status
                try {
                    if (statusFilter.size() > 0) {
                        count.setVisibility(View.VISIBLE);
                        count.setText(String.valueOf(statusFilter.size()));
                    } else {
                        count.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    count.setVisibility(View.GONE);
                }
                break;
        }

        // Set Header
        TextViewAr lblListHeader = convertView
                .findViewById(R.id.name);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setTextColor(Color.BLACK);
        lblListHeader.setTextSize(16);
        lblListHeader.setText(headerTitle);

        ImageView tick = convertView.findViewById(R.id.tick);
        tick.setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}