package com.heliam1.hackathon.presenters;

import com.heliam1.hackathon.models.Group;
import com.heliam1.hackathon.repositories.GroupsRepository;
import com.heliam1.hackathon.repositories.UserRepository;
import com.heliam1.hackathon.ui.MainView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter {
    // TODO: Inject these?
    private MainView mView;

    private GroupsRepository mGroupsRepository;

    private UserRepository mUserRepository;

    private final Scheduler mainScheduler;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public MainPresenter(MainView view, GroupsRepository groupsRepository,
                         UserRepository userRepository, Scheduler mainScheduler) {
        mView = view;
        mGroupsRepository = groupsRepository;
        mUserRepository = userRepository;
        this.mainScheduler = mainScheduler;
    }

    public void loadGroups() {
        compositeDisposable.add(mGroupsRepository.getGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<List<Group>>() {
                    @Override
                    public void onSuccess(List<Group> groupsList) {
                        if (groupsList.isEmpty()) {
                            mView.displayNoGroups();
                        } else {
                            List<Double> distances = new ArrayList<>();
                            mView.displayGroups(groupsList, distances);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.displayToast("Something wrong with db");
                        mView.displayNoGroups();
                    }
                })
        );
    }

    private void saveGroups(Group group) {
        compositeDisposable.add(mGroupsRepository.saveGroup(group)
                .subscribeOn(Schedulers.io())
                .observeOn(mainScheduler)
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(Long idOfSavedGroup) {
                        if (idOfSavedGroup == null) {
                            // TODO: this should be in on error only?
                            mView.displayErrorSavingGroup();
                        } else {
                            mView.displaySuccessSavingGroup();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.displayErrorSavingGroup();
                    }
                })
        );
    }
}
