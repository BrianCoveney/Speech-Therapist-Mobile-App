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
import com.brian.speechtherapistapp.models.ChildResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildAdapter extends ArrayAdapter<ChildResponse> {

    private Context mContext;
    private List<ChildResponse> childList;

    @BindView(R.id.tv_first_name)
    TextView nameTextView;


    public ChildAdapter(@NonNull Context context, List<ChildResponse> list) {
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

        ButterKnife.bind(this, listItem);

        ChildResponse currentChild = childList.get(position);

        nameTextView.setText(currentChild.getFirstName());

        return listItem;
    }
}

