package com.heliam1.hackathon.presenters;

import com.heliam1.hackathon.models.GroupMessage;
import com.heliam1.hackathon.repositories.GroupMessageRepository;
import com.heliam1.hackathon.ui.ChatView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChatPresenter {
    private ChatView mView;

    private GroupMessageRepository mGroupMessageRepository;

    private final Scheduler mainScheduler;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ChatPresenter(ChatView view, GroupMessageRepository groupMessageRepository,
                         Scheduler mainScheduler) {
        mView = view;
        mGroupMessageRepository = groupMessageRepository;
        this.mainScheduler = mainScheduler;
    }

    public void loadGroupMessages() {
        compositeDisposable.add(mGroupMessageRepository.getGroupMessages()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<GroupMessage>>() {
                    @Override
                    public void onSuccess(List<GroupMessage> groupMessageList) {
                        if (groupMessageList.isEmpty()) {
                            mView.displayNoGroupMessages();
                        } else {
                            mView.displayGroupMessages(groupMessageList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.displayToast("Something wrong with db");
                        mView.displayNoGroupMessages();
                    }
                })
        );
    }

    public void sendGroupMessage(GroupMessage groupMessage) {
        compositeDisposable.add(mGroupMessageRepository.saveGroupMessage(groupMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long idOfSavedGroup) {
                        if (idOfSavedGroup == null) {
                            // TODO: this should be in on error only?
                            mView.displayToast("Message not sent!");
                        } else {
                            mView.displaySuccessfulGroupMessage();
                            mView.displayToast("Message sent!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.displayToast("Message not sent!");
                    }
                })
        );
        // TODO: better solution than this surely... maybe on database upsert? ondatabasechange?
        loadGroupMessages();
    }
}
