package com.heliam1.hackathon;

import android.location.Location;

import com.heliam1.hackathon.models.Group;
import com.heliam1.hackathon.presenters.MainPresenter;
import com.heliam1.hackathon.repositories.GroupsRepository;
import com.heliam1.hackathon.repositories.UserRepository;
import com.heliam1.hackathon.ui.MainView;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MainPresenterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    GroupsRepository groupsRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MainView mainView;

    MainPresenter mainPresenter;
    private final List<Group> MANY_GROUPS = Arrays.asList(
            new Group(1, new Location("Lat/Lng"),"WiseTech Hackathon", 48650, 3,9001),
            new Group(1, new Location("Lat/Lng"),"WiseTech Hackathon", 48650, 3,9001),
            new Group(1, new Location("Lat/Lng"),"WiseTech Hackathon", 48650, 3,9001));
    private final List<Double> MANY_DISTANCES_AWAY = new ArrayList<>();

    @Before
    public void setUp() {
        mainPresenter = new MainPresenter(mainView, groupsRepository, userRepository, Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @After
    public void cleanUp() {
        RxJavaPlugins.reset();
    }

    @Test
    public void passGroupsToView() {
        when(groupsRepository.getGroups()).thenReturn(Single.just(MANY_GROUPS)); // given, find the view

        mainPresenter.loadGroups(); // when, - do something to the view

        Mockito.verify(mainView).displayGroups(MANY_GROUPS, MANY_DISTANCES_AWAY);
    }

    /*
    // should also test edge cases
    @Test
    public void handleNoWorkoutsFound() {
        Mockito.when(workoutRepository.getWorkouts()).thenReturn(Single.<List<Workout>>just(EMPTY_LIST));

        workoutsPresenter.loadWorkouts();

        Mockito.verify(workoutsView).displayNoWorkouts();
    }

    @Test
    public void handleErrorGettingWorkouts() {
        when(workoutRepository.getWorkouts()).thenReturn(Single.<List<Workout>>error(new Throwable("boom")));

        workoutsPresenter.loadWorkouts();

        Mockito.verify(workoutsView).displayErrorLoadingWorkouts();// Remember verify is " did we call this method on the mock"
        Mockito.verify(workoutsView).displayToast("Something wrong with db");
    }

    @Test
    public void savesADefaultWorkout() {

    }

    @Test
    public void lauchesNewWorkout() {

    }

    @Test
    public void savesCustomWorkout() {

    }

    @Test
    public void launchesCurrentWorkout() {

    }*/
}