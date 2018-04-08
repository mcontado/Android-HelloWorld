package com.example.myfirstapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testPressMeButton_ShouldDisplayHelloWorldAuthorAndDate() {
        onView(withId(R.id.pressMeButtonId)).perform(click());

        onView(withId(R.id.textViewHelloWorld))
                .check(matches(withText(R.string.hello_world)));

        onView(withId(R.id.textViewNameDateId))
                .check(matches(withText(R.string.lastname_firstname_date)));
    }
}