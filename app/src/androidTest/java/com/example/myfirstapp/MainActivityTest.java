package com.example.myfirstapp;

import android.support.test.espresso.contrib.PickerActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSubmitButton_ShouldValidateForm() {
        onView(withId(R.id.textViewNameDateId))
                .check(matches(withText(R.string.lastname_firstname_date)));
        onView(withId(R.id.nameEditText)).perform(typeText("Maria Contado"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("mcontado"));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2000, 4, 15));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.birthDateTextView)).check(matches(withText("15/4/2000")));

        TestUtils.rotateScreen(testRule.getActivity());

        // Make sure text view still has birth date after screen rotation
        onView(withId(R.id.birthDateTextView))
                .check(matches(withText("15/4/2000")));

        TestUtils.rotateScreen(testRule.getActivity());

    }


    @Test
    public void testSubmitButton_WithEmptyForm_ShouldNotProceed() {
        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.NAME_EMPTY_OR_NULL +
                                        Constants.EMAIL_EMPTY_OR_NULL +
                                        Constants.USERNAME_EMPTY_OR_NULL +
                                        Constants.BIRTH_DATE_EMPTY_OR_NULL +
                                        Constants.AGE_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyName_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText(""));
        onView(withId(R.id.emailEditText)).perform(typeText("test@email.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("mcontado"));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2000, 1, 15));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.NAME_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyEmail_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("test"));
        onView(withId(R.id.emailEditText)).perform(typeText(""));
        onView(withId(R.id.userNameEditText)).perform(typeText("mcontado"));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2000, 1, 15));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.EMAIL_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithInvalidEmail_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("aaa"));
        onView(withId(R.id.userNameEditText)).perform(typeText("mcontado"));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2000, 1, 15));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.EMAIL_IS_INVALID)));

    }

    @Test
    public void testSubmitButton_WithEmptyUserName_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText(""));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(2000, 1, 15));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.USERNAME_EMPTY_OR_NULL)));
    }

    @Test
    public void testSubmitButton_WithEmptyBirthDate_ShouldNotProceed() {
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("userName"));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.BIRTH_DATE_EMPTY_OR_NULL +
                                        Constants.AGE_EMPTY_OR_NULL)));

    }

    @Test
    public void testSubmitButton_WithAgeLessThan18_ShouldNotProceed() {
        Calendar currentDate = Calendar.getInstance();
        onView(withId(R.id.nameEditText)).perform(typeText("TestName"));
        onView(withId(R.id.emailEditText)).perform(typeText("test@gmail.com"));
        onView(withId(R.id.userNameEditText)).perform(typeText("userName"));
        onView(withId(R.id.userNameEditText)).perform(closeSoftKeyboard());

        onView(withId(R.id.birthDateButtonId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(currentDate.get(Calendar.YEAR) - 18,
                                                    currentDate.get(Calendar.MONTH) + 1,
                                                    currentDate.get(Calendar.DAY_OF_MONTH) + 1));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.submitButtonId)).perform(click());

        onView(withId(R.id.errorMsgTextView))
                .check(matches(withText(Constants.UNDER_18_NOT_ALLOWED)));

    }
}