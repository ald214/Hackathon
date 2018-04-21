package com.heliam1.hackathon.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.heliam1.hackathon.R;

import java.util.List;

public class GroupsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Group> mGroups;

    public GroupsListAdapter(Context context, List<Group> groups) {
        // super(context, 0, workouts);
        mContext = context;
        mGroups = groups;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View groupView = convertView;

        if (groupView == null) {
            groupView = LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false);
        }

        Group group = getItem(position);

        // views
        TextView workoutDateView = groupView.findViewById(R.id.text_view_group);

        // workoutImageView.setImage( // TODO:
        groupNameView.setText(group.getName());
        // workoutDateView.setText(workout.getDate());

        return groupView;
    }

    @Override
    public int getCount() { return mGroups.size(); }

    @Override
    public Group getItem(int i) {
        return mGroups.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
