package com.example.myfirstapp;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class SecondActivityTest {
    @Rule
    public ActivityTestRule<SecondActivity> activityTestRule
            = new ActivityTestRule<SecondActivity>(SecondActivity.class) {

        @Override
        protected Intent getActivityIntent() {
            Intent testIntent = new Intent();
            testIntent.putExtra(Constants.KEY_NAME, "Jane Doe");
            testIntent.putExtra(Constants.KEY_AGE, "18");
            testIntent.putExtra(Constants.KEY_EMAIL, "janedoe@gmail.com");
            testIntent.putExtra(Constants.KEY_USERNAME, "jdoe");
            testIntent.putExtra(Constants.KEY_BDATE, "18/04/2000");
            testIntent.putExtra(Constants.KEY_DESCRIPTION, "Music lover, dog lover");
            testIntent.putExtra(Constants.KEY_OCCUPATION, "Software Engineer");

            return testIntent;
        }
    };

    @Test
    public void setsRightMessageBasedOnIntentExtra() {

        Intents.init();
        onView(withId(R.id.backToMainBtnId)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();

        checkFormIsEmptyForNewProfile();

    }

    private void checkFormIsEmptyForNewProfile() {
        onView(withId(R.id.nameEditText)).check(matches(withText("")));
        onView(withId(R.id.emailEditText)).check(matches(withText("")));
        onView(withId(R.id.userNameEditText)).check(matches(withText("")));
        onView(withId(R.id.ageEditText)).check(matches(withText("")));
        onView(withId(R.id.birthDateTextView)).check(matches(withText("")));
        onView(withId(R.id.selfDescriptionEditText)).check(matches(withText("")));
        onView(withId(R.id.occupationEditText)).check(matches(withText("")));
    }
}