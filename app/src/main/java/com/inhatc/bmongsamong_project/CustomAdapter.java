package com.inhatc.bmongsamong_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    LayoutInflater inflater;
    Context context;
    int layout;
    ArrayList<Item> alist;

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        TextView tv_item_title = (TextView) convertView.findViewById(R.id.item_title);
        TextView tv_item_date = (TextView) convertView.findViewById(R.id.item_date);
        LinearLayout item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);

        tv_item_title.setText(alist.get(position).getTitle());
        tv_item_date.setText(alist.get(position).getDate());
//        item_layout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                item_list.remove(position);
//                ca.notifyDataSetChanged();
//                Toast.makeText(MainActivity.this, "Removed", Toast.LENGTH_SHORT).show();
//
//                return false;
//            }
//        });

        return convertView;
    }

    public CustomAdapter(Context context, int layout, ArrayList<Item> alist) {
        this.context = context;
        this.layout = layout;
        this.alist = alist;
        inflater = LayoutInflater.from(context);
    }
}
