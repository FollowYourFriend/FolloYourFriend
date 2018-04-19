package com.ericsson.followyourfriend;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ericsson.Person.Friend;

import java.util.ArrayList;

public class TestAdapter extends ArrayAdapter<Friend> {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Friend> mFriends;

    public TestAdapter(Context context, ArrayList<Friend> items) {
        super(context, R.layout.item);
        mContext = context;
        mFriends = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Friend getItem(int i) {
        return mFriends.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.item,viewGroup,false);

        ImageView offline = (ImageView) rowView.findViewById(R.id.offline);
        ImageView notRegistered = (ImageView) rowView.findViewById(R.id.notRegistered);
        ImageView online = (ImageView) rowView.findViewById(R.id.online);
        TextView textItem = (TextView) rowView.findViewById(R.id.item_text);
        TextView textItem2 = (TextView) rowView.findViewById(R.id.item_text2);

        Resources res = Resources.getSystem();

        Friend friend = (Friend) getItem(i);

        if(friend != null) {
            textItem.setText((CharSequence) friend.getmName());
            textItem2.setText(((CharSequence) String.valueOf(friend.getmNumber())));
            switch (friend.getmStatus()) {
                case VISIBLE:
                    online.setVisibility(View.VISIBLE);
                    offline.setVisibility(View.GONE);
                    notRegistered.setVisibility(View.GONE);
                    break;
                case INVISIBLE:
                    online.setVisibility(View.GONE);
                    offline.setVisibility(View.VISIBLE);
                    notRegistered.setVisibility(View.GONE);
                    break;
                case NOT_REGISTERED:
                    online.setVisibility(View.GONE);
                    offline.setVisibility(View.GONE);
                    notRegistered.setVisibility(View.VISIBLE);
                    break;
            }
        }

        return rowView;
    }
}
