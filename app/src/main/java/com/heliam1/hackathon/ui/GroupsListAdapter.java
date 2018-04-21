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
import com.heliam1.hackathon.models.Group;

import java.util.List;

import butterknife.BindView;

public class GroupsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Group> mGroups;
    private List<Double> mDistances;

    @BindView(R.id.distance_away) TextView distanceAwayTextView;
    @BindView(R.id.subject_code) TextView subjectCodeTextView;
    @BindView(R.id.subject_name) TextView subjectNameTextView;
    @BindView(R.id.user_count) TextView userCountTextView;
    @BindView(R.id.rating) TextView ratingTextView;

    public GroupsListAdapter(Context context, List<Group> groups, List<Double> distances) {
        // super(context, 0, workouts);
        mContext = context;
        mGroups = groups;
        mDistances = distances;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View groupView = convertView;

        if (groupView == null) {
            groupView = LayoutInflater.from(mContext).inflate(R.layout.item_group, parent, false);
        }

        Group group = getItem(position);

        distanceAwayTextView.setText(Double.toString(getDistance(position)));
        subjectCodeTextView.setText(group.getSubjectCode());
        subjectNameTextView.setText(group.getName());
        userCountTextView.setText(group.getUserCount());
        ratingTextView.setText(group.getRating());

        return groupView;
    }

    @Override
    public int getCount() { return mGroups.size(); }

    @Override
    public Group getItem(int i) {
        return mGroups.get(i);
    }

    private Double getDistance(int i) { return mDistances.get(i); }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
