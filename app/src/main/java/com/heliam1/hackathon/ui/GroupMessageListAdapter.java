package com.heliam1.hackathon.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heliam1.hackathon.R;
import com.heliam1.hackathon.models.GroupMessage;
import com.heliam1.hackathon.models.User;

import java.util.List;

public class GroupMessageListAdapter extends BaseAdapter {
    private Context mContext;
    private List<GroupMessage> mGroupMessages;
    private List<User> mUsers;

    private TextView groupMessageUserName;
    private TextView groupMessageText;

    public GroupMessageListAdapter(Context context, List<GroupMessage> groupMessages, List<User> users) {
        // super(context, 0, workouts);
        mContext = context;
        mGroupMessages = groupMessages;
        mUsers = users;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View groupMessageView = convertView;

        if (groupMessageView == null) {
            groupMessageView = LayoutInflater.from(mContext).inflate(R.layout.item_group_message, parent, false);
        }

        GroupMessage groupMessage = getItem(position);

        groupMessageUserName = (TextView) groupMessageView.findViewById(R.id.text_view_group_message_user_name);
        groupMessageText = (TextView) groupMessageView.findViewById(R.id.text_view_group_message_text);

        // distanceAwayTextView.setText(Double.toString(getDistance(position)));
        groupMessageUserName.setText(groupMessage.getUserName());
        groupMessageText.setText(groupMessage.getMessageText());

        return groupMessageView;
    }

    @Override
    public int getCount() { return mGroupMessages.size(); }

    @Override
    public GroupMessage getItem(int i) {
        return mGroupMessages.get(i);
    }

    // private User getUser() { return mDistances.get(i); }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
