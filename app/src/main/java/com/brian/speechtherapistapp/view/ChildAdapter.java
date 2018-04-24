package com.brian.speechtherapistapp.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brian.speechtherapistapp.R;
import com.brian.speechtherapistapp.models.Child;

import java.util.List;

public class ChildAdapter extends ArrayAdapter<Child> {

    private Context mContext;
    private List<Child> childList;

    public ChildAdapter(@NonNull Context context, List<Child> list) {
        super(context, 0, list);
        mContext = context;
        childList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false);

        Child currentChild = childList.get(position);

        TextView nameTextView = listItem.findViewById(R.id.tv_first_name);
        TextView emailTextView = listItem.findViewById(R.id.tv_email);
        nameTextView.setText(currentChild.getFirstName());
        emailTextView.setText(currentChild.getEmail());

        return listItem;
    }
}

