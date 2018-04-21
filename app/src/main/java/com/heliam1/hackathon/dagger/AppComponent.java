package com.heliam1.hackathon.dagger;

import android.app.Application;
import android.app.LauncherActivity;

import com.heliam1.hackathon.HackathonApplication;
import com.heliam1.hackathon.presenters.MainPresenter;
import com.heliam1.hackathon.repositories.GroupsRepository;
import com.heliam1.hackathon.repositories.UserRepository;
import com.heliam1.hackathon.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton @Component(modules = {AppModule.class})    // , DbModule.class}
public interface AppComponent {
    void inject(HackathonApplication application);

    void inject(MainActivity mainActivity);

    void inject(MainPresenter mainPresenter);

    void inject(GroupsRepository groupsRepository);

    void inject(UserRepository userRepository);

}

// these are the places you want to inject
// dagger will look in the modules for things to provide
// this is where you choose to inject, you have to write the places you want to inject into here
// and then write a line of code into them (see Hackathon Interface and MainActivity class)
