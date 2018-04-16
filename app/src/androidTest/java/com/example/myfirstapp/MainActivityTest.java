package com.example.myfirstapp;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Ignore("Crashing when providing date on date picker dialog")
    @Test
    public void testSubmitButton_ShouldValidateForm_HappyPath() {
        onView(withId(R.id.nameEditText)).perform(typeText("Maria Contado"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("mcontado"));
        onView(withId(R.id.ageEditText)).perform(typeText("20"));

        int year = 2000;
        int month = 4;
        int day = 15;

        onView(withId(R.id.birthDateEditText)).perform(click());
        // TODO: Crashing when calling PickerActions for setting date
        onView(withId(R.id.birthDateEditText))
                .perform(PickerActions.setDate(year, month + 1, day));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        intended(hasComponent(SecondActivity.class.getName()));
        intended(hasExtra(Constants.KEY_NAME, "Maria Contado"));
        Intents.release();

        onView(withId(R.id.textViewNameDateId))
                .check(matches(withText(R.string.lastname_firstname_date)));
    }

    @Test
    public void testSubmitButton_WithEmptyName_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(""));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.NAME_EMPTY_OR_NULL)));

    }

    @Test
    public void testSubmitButton_WithEmptyEmail_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText(""));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.EMAIL_EMPTY_OR_NULL)));
        onView(withId(R.id.nameEditText))
                .check(matches(withText("TestName")));

    }

    @Test
    public void testSubmitButton_WithInvalidEmail_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("aaa"));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.EMAIL_IS_INVALID)));

    }

    @Test
    public void testSubmitButton_WithEmptyUserName_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText(""));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.USERNAME_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyAge_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("userName"));
        onView(withId(R.id.ageEditText)).perform(typeText(""));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.AGE_EMPTY_OR_NULL)));

    }

    @Test
    public void testSubmitButton_WithAgeLessThan18_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("userName"));
        onView(withId(R.id.ageEditText)).perform(typeText("17"));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.UNDER_18_NOT_ALLOWED)));

    }

    @Test
    public void testSubmitButton_WithEmptyBirthDate_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("userName"));
        onView(withId(R.id.ageEditText)).perform(typeText("20"));

        Intents.init();
        onView(withId(R.id.submitButtonId)).perform(click());
        Intents.release();

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.BIRTH_DATE_EMPTY_OR_NULL)));

    }
}