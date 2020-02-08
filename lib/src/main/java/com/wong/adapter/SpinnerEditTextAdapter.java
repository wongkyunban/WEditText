package com.wong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wong.widget.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerEditTextAdapter extends BaseAdapter {

    private List<Object> list = new ArrayList<Object>();
    private int itemTextColor;
    private float itemTextSize;
    private Context mContext;

    public SpinnerEditTextAdapter(Context context) {
        this.mContext = context;
    }


    public SpinnerEditTextAdapter(Context context,List list,int itemTextColor,float itemTextSize) {
        this.mContext = context;
        this.list = list;
        this.itemTextColor = itemTextColor;
        this.itemTextSize = itemTextSize;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public void setItemTextSize(float itemTextSize) {
        this.itemTextSize = itemTextSize;
    }

    public void setItemTextColor(int itemTextColor) {
        this.itemTextColor = itemTextColor;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.simple_list_item_w, null, false);
            holder.itemTextView = convertView.findViewById(R.id.sp_item_text);
            convertView.setTag(holder);

            if (itemTextColor != 0)
                holder.itemTextView.setTextColor(itemTextColor);
            if (itemTextSize != 0)
                holder.itemTextView.setTextSize(itemTextSize);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            final String itemName = list.get(position).toString();
            if (holder.itemTextView != null) {
                holder.itemTextView.setText(itemName);
            }
        }
        return convertView;
    }
    private static class ViewHolder {
        TextView itemTextView;
    }
}

