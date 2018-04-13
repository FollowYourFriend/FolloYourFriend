package com.ericsson.followyourfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ericsson.Person.Friend;

import java.util.ArrayList;

public class TestAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Friend> mFriends;

    public TestAdapter(Context context, ArrayList<Friend> items) {
        mContext = context;
        mFriends = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int i) {
        return mFriends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.item,viewGroup,false);

        TextView textItem = (TextView) rowView.findViewById(R.id.item_text);
        TextView textItem2 = (TextView) rowView.findViewById(R.id.item_text2);

        Friend friend = (Friend) getItem(i);

        if(friend != null) {
            textItem.setText((CharSequence) friend.getmName());
            textItem2.setText(((CharSequence) String.valueOf(friend.getmNumber())));
        }

        return rowView;
    }
}
