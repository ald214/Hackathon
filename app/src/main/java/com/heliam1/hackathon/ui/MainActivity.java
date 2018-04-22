package com.heliam1.hackathon.ui;

import android.content.ContentUris;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.heliam1.hackathon.HackathonApplication;
import com.heliam1.hackathon.R;
import com.heliam1.hackathon.models.Group;
import com.heliam1.hackathon.presenters.MainPresenter;
import com.heliam1.hackathon.repositories.GroupsRepository;
import com.heliam1.hackathon.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements MainView {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Inject
    GroupsRepository mGroupsRepository;

    @Inject
    UserRepository mUserRepository;

    private MainPresenter mMainPresenter;

    private FloatingActionButton mFabAddGroup;
    private Toast mToast;

    ListView mGroupsListView;
    // @BindView(R.id.groups_list_view) ListView mGroupsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "OnCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((HackathonApplication) getApplication()).getAppComponent().inject(this);

        mGroupsListView = (ListView) findViewById(R.id.groups_list_view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Setup the item click listener
        mGroupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long currentGroupId) {

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);

                // pass the group id to the chat activity
                intent.setAction(Long.toString(currentGroupId));

                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "OnCreate called");
        mMainPresenter = new MainPresenter(this, mGroupsRepository, mUserRepository,
                AndroidSchedulers.mainThread());
        mMainPresenter.loadGroups();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayGroups(List<Group> groups, List<Double> distancesAway) {
        GroupsListAdapter groupsListAdapter = new GroupsListAdapter(this, groups, distancesAway);

        mGroupsListView.setAdapter(groupsListAdapter);
    }

    @Override
    public void displayNoGroups() {

    }

    @Override
    public void displaySuccessSavingGroup() {

    }

    @Override
    public void displayErrorSavingGroup() {

    }

    @Override
    public void displayToast(String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
