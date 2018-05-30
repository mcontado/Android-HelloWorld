package com.example.myfirstapp;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentProvider;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private static final int HOURS = 11;
    private static final int MINUTES = 20;
    private static final String MILES_DISTANCE = "5";

    private Location locationMock;
    private LocationManager locationManagerMock;

    @Before
    public void setUp() {
        locationMock = mock(Location.class);
        locationManagerMock = mock(LocationManager.class);

        when(locationManagerMock.isProviderEnabled(LocationManager.GPS_PROVIDER)).thenReturn(true);
        when(locationMock.distanceTo(any(Location.class))).thenReturn(16093.4f);

    }

    @Test
    public void testSubmitButton_ShouldValidateForm() throws InterruptedException {
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
        Thread.sleep(5000); // Needed due to slow rotation of the screen layout

        try {
            Intents.init();
            onView(withId(R.id.submitButtonId)).perform(click());
            intended(hasComponent(TabActivity.class.getName()));
            intended(hasExtra(Constants.KEY_NAME, NAME));
            intended(hasExtra(Constants.KEY_AGE, AGE));
            intended(hasExtra(Constants.KEY_EMAIL, EMAIL));
            intended(hasExtra(Constants.KEY_USERNAME, USERNAME));
            intended(hasExtra(Constants.KEY_BDATE, BIRTHDATE));
            intended(hasExtra(Constants.KEY_DESCRIPTION, DESCRIPTION));
            intended(hasExtra(Constants.KEY_OCCUPATION, OCCUPATION));

            // TEST MATCHES TAB
            onView(allOf(withText("Matches"), isDescendantOfA(withId(R.id.tabs)))).perform(click());

            Thread.sleep(5000);
            onView(allOf(
                    getElementFromMatchAtPosition(
                            allOf(withId(R.id.card_title)), 0), isDisplayed()))
            .check(matches(withText("Cool Guy Mike")));

            onView(allOf(
                    getElementFromMatchAtPosition(
                            allOf(withId(R.id.favorite_button)), 0), isDisplayed()))
            .perform(click());

            // TEST SETTINGS TAB
            onView(allOf(withText("Settings"), isDescendantOfA(withId(R.id.tabs)))).perform(click());
            onView(withId(R.id.reminderTime)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                    .perform(PickerActions.setTime(HOURS, MINUTES));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.distanceSearch)).perform(typeText(MILES_DISTANCE));
            onView(withId(R.id.distanceSearch)).perform(closeSoftKeyboard());
            onView(withId(R.id.radioFemale)).perform(click());
            onView(withId(R.id.radioPrivate)).perform(click());

            onView(withId(R.id.spinnerAgeRange)).perform(click());
            onData(anything()).atPosition(1).perform(click());
            onView(withId(R.id.spinnerAgeRange))
                    .check(matches(withSpinnerText(containsString("26 - 35"))));
            onView(withId(R.id.saveSettingsId)).perform(click());


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

    private static Matcher<View> getElementFromMatchAtPosition(final Matcher<View> matcher, final int position) {
        return new BaseMatcher<View>() {
            int counter = 0;

            @Override
            public boolean matches(final Object item) {
                if (matcher.matches(item)) {
                    if (counter == position) {
                        counter++;
                        return true;
                    }
                    counter++;
                }
                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("Element at hierarchy position " + position);
            }
        };
    }
}