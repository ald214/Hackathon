package com.heliam1.hackathon.dagger;

import android.content.Context;

import com.heliam1.hackathon.HackathonApplication;
import com.heliam1.hackathon.repositories.DatabaseRepository;
import com.heliam1.hackathon.repositories.GroupsRepository;
import com.heliam1.hackathon.repositories.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final HackathonApplication application;

    public AppModule(HackathonApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    GroupsRepository providesGroupsRepository(Context application) {
        return new DatabaseRepository(application);
    }

    @Provides
    @Singleton
    UserRepository providesUserRepository(Context application) {
        return new DatabaseRepository(application);
    }
}

// 1 what do you want to inject
// 2 where do you want to inject
// 3 where will you construct it

