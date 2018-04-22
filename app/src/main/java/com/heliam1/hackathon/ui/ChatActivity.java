package com.heliam1.hackathon.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.heliam1.hackathon.HackathonApplication;
import com.heliam1.hackathon.R;
import com.heliam1.hackathon.models.Chat;
import com.heliam1.hackathon.models.GroupMessage;
import com.heliam1.hackathon.presenters.ChatPresenter;
import com.heliam1.hackathon.repositories.GroupMessageRepository;

import java.util.ArrayList;
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
    private GroupMessageListAdapter mGroupMessageListAdapter;
    private ChatsListAdapter mChatsListAdapter;
    private EditText mSendMessageEditText;

    // Firebase instance variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    private String mUsername = "Challenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(LOG_TAG, "OnCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ((HackathonApplication) getApplication()).getAppComponent().inject(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("Chat");

        mChatsListView = (ListView) findViewById(R.id.chat_list_view);
        List<Chat> chats = new ArrayList<>();
        mChatsListAdapter = new ChatsListAdapter(this, R.layout.item_chat, chats);
        mChatsListView.setAdapter(mChatsListAdapter);

        mSendMessageEditText = (EditText) findViewById(R.id.chat_send_message_edit_text) ;
        mFabSendMessage = (FloatingActionButton) findViewById(R.id.fab_send_message);
        // mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        // mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);

        // mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mFabSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayToast("Fab clicked");

                // GroupMessage groupMessage = new GroupMessage(Long.parseLong("1"), 1, 99126207,
                //        "Challenger", "today",
                //        mSendMessageEditText.getText().toString().trim());

                // ......sendGroupMessage(groupMessage);
                Chat chat = new Chat(mSendMessageEditText.getText().toString().trim(), "Challenger");
                mMessagesDatabaseReference.push().setValue(chat);

                /*
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */
            }
        });

        // ImagePicker on click listener

        // text change listener

        // Child event listener
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);
                Log.v(LOG_TAG, "Chat Received: " + chat.getText());
                mChatsListAdapter.add(chat);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
    }

    // onCreateOptionsMenu

    // onOptionsSelected

    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "OnCreate called");
        mChatPresenter = new ChatPresenter(this, mGroupMessagesRepository,
                AndroidSchedulers.mainThread());
        // mChatPresenter.loadGroupMessages();
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
    public void displaySuccessfulGroupMessage() {
        mSendMessageEditText.setText("");
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
