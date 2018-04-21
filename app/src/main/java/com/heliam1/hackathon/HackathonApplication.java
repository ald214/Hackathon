package com.heliam1.hackathon;

import android.app.Application;

import com.heliam1.hackathon.dagger.AppComponent;
import com.heliam1.hackathon.dagger.AppModule;
import com.heliam1.hackathon.dagger.DaggerAppComponent;

public class HackathonApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
