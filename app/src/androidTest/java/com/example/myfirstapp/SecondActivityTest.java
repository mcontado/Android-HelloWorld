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
            testIntent.putExtra(Constants.KEY_NAME, "Test Name");
            return testIntent;
        }
    };

    @Test
    public void setsRightMessageBasedOnIntentExtra() {
        onView(withId(R.id.thanksTextView))
                .check(matches(withText(Constants.THANKS_SIGN + "Test Name")));
    }

    @Test
    public void canGoToMainActivity() {
        Intents.init();
        onView(withId(R.id.backToMainBtnId)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
        Intents.release();
    }
}