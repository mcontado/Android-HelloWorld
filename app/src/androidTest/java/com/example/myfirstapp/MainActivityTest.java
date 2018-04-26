package com.example.myfirstapp;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Calendar;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);
    private static final String NAME = "Jane Doe";
    private static final String EMAIL = "janedoe@gmail.com";
    private static final String USERNAME = "jdoe";
    private static final String AGE = "18";
    private static final int BIRTH_YEAR = 2000;
    private static final int BIRTH_MONTH = 4;
    private static final int BIRTH_DAY = 15;
    private static final String BIRTHDATE = "15/4/2000";
    private static final String DESCRIPTION = "Music lover, dog lover...";
    private static final String OCCUPATION = "Software Engineer";

    @Test
    public void testSubmitButton_ShouldValidateForm() throws InterruptedException {
        onView(withId(R.id.textViewNameDateId))
                .check(matches(withText(R.string.lastname_firstname_date)));
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.birthDateTextView)).check(matches(withText(BIRTHDATE)));

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        TestUtils.rotateScreen(testRule.getActivity());

        // Make sure text view still has birth date after screen rotation
        onView(withId(R.id.birthDateTextView)).check(matches(withText(BIRTHDATE)));

        TestUtils.rotateScreen(testRule.getActivity());

        onView(withId(R.id.errorMsgTextView)).check(matches(withText("")));
        Thread.sleep(2000); // Needed due to slow rotation of the screen layout

        try {
            Intents.init();
            onView(withId(R.id.submitButtonId)).perform(click());
            intended(hasComponent(SecondActivity.class.getName()));
            intended(hasExtra(Constants.KEY_NAME, NAME));
            intended(hasExtra(Constants.KEY_AGE, AGE));
            intended(hasExtra(Constants.KEY_EMAIL, EMAIL));
            intended(hasExtra(Constants.KEY_USERNAME, USERNAME));
            intended(hasExtra(Constants.KEY_BDATE, BIRTHDATE));
            intended(hasExtra(Constants.KEY_DESCRIPTION, DESCRIPTION));
            intended(hasExtra(Constants.KEY_OCCUPATION, OCCUPATION));
        } finally {
            Intents.release();
        }
    }


    @Test
    public void testSubmitButton_WithEmptyForm_ShouldNotProceed() {
        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.NAME_EMPTY_OR_NULL +
                                        Constants.EMAIL_EMPTY_OR_NULL +
                                        Constants.USERNAME_EMPTY_OR_NULL +
                                        Constants.BIRTH_DATE_EMPTY_OR_NULL +
                                        Constants.AGE_EMPTY_OR_NULL +
                                        Constants.DESCRIPTION_IS_EMPTY_OR_NULL +
                                        Constants.OCCUPATION_IS_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyName_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(""));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.NAME_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyEmail_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(""));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.EMAIL_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithInvalidEmail_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText("aaa"));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.EMAIL_IS_INVALID)));

    }

    @Test
    public void testSubmitButton_WithEmptyUserName_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(""));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.USERNAME_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyBirthDate_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.BIRTH_DATE_EMPTY_OR_NULL +
                                        Constants.AGE_EMPTY_OR_NULL)));

    }

    @Test
    public void testSubmitButton_WithAgeLessThan18_ShouldNotProceed() {
        Calendar currentDate = Calendar.getInstance();
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(currentDate.get(Calendar.YEAR) - 18,
                                                    currentDate.get(Calendar.MONTH) + 1,
                                                    currentDate.get(Calendar.DAY_OF_MONTH) + 1));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());
        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.UNDER_18_NOT_ALLOWED)));

    }

    @Test
    public void testSubmitButton_WithNullSelfDescription_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.occupationEditText)).perform(typeText(OCCUPATION));
        onView(withId(R.id.occupationEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.DESCRIPTION_IS_EMPTY_OR_NULL)));

    }

    @Test
    public void testSubmitButton_WithNullOccupation_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(NAME));
        onView(withId(R.id.emailEditText)).perform(typeText(EMAIL));
        onView(withId(R.id.userNameEditText)).perform(typeText(USERNAME));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(BIRTH_YEAR, BIRTH_MONTH, BIRTH_DAY));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.selfDescriptionEditText)).perform(typeText(DESCRIPTION));
        onView(withId(R.id.selfDescriptionEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.OCCUPATION_IS_EMPTY_OR_NULL)));

    }
}