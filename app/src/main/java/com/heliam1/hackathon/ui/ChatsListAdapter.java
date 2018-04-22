package com.heliam1.hackathon.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.heliam1.hackathon.R;
import com.heliam1.hackathon.models.Chat;

import java.util.List;

public class ChatsListAdapter extends ArrayAdapter<Chat> {
    public ChatsListAdapter(Context context, int resource, List<Chat> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_chat, parent, false);
        }

        TextView chatTextView = (TextView) convertView.findViewById(R.id.chatTextView);
        TextView usernameTextView = (TextView) convertView.findViewById(R.id.usernameTextView);

        Chat chat = getItem(position);


        chatTextView.setText(chat.getText());
        usernameTextView.setText(chat.getName());

        return convertView;
    }
}
