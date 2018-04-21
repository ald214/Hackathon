package com.heliam1.hackathon.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.heliam1.hackathon.HackathonApplication;
import com.heliam1.hackathon.R;
import com.heliam1.hackathon.models.GroupMessage;
import com.heliam1.hackathon.models.User;
import com.heliam1.hackathon.presenters.ChatPresenter;
import com.heliam1.hackathon.repositories.GroupMessageRepository;
import com.heliam1.hackathon.repositories.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class ChatActivity extends AppCompatActivity implements ChatView {
    public static final String LOG_TAG = ChatActivity.class.getSimpleName();

    @Inject
    GroupMessageRepository mGroupMessagesRepository;

    // @Inject
    // UserRepository mUserRepository;

    private ChatPresenter mChatPresenter;

    private FloatingActionButton mFabSendMessage;
    private Toast mToast;

    private ListView mChatsListView;
    private EditText mSendMessageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "OnCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ((HackathonApplication) getApplication()).getAppComponent().inject(this);

        mChatsListView = (ListView) findViewById(R.id.chat_list_view);
        mSendMessageEditText = (EditText) findViewById(R.id.chat_send_message_edit_text) ;
        mFabSendMessage = (FloatingActionButton) findViewById(R.id.fab_send_message);

        mFabSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToast("Fab clicked");
                
                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "OnCreate called");
        mChatPresenter = new ChatPresenter(this, mGroupMessagesRepository,
                AndroidSchedulers.mainThread());
        mChatPresenter.loadGroupMessages();
    }

    @Override
    public void displayGroupMessages(List<GroupMessage> groupMessages) {
        GroupMessageListAdapter groupMessageListAdapter = new GroupMessageListAdapter(this, groupMessages);

        mChatsListView.setAdapter(groupMessageListAdapter);
    }

    @Override
    public void displayNoGroupMessages() {

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
