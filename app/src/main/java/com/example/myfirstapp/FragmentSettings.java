package com.example.myfirstapp;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.myfirstapp.entity.UserSettings;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.List;


public class FragmentSettings extends Fragment //{
        implements AdapterView.OnItemSelectedListener {

//    final String[] maxDistanceInMiles = { "5", "10", "15", "20", "25"};
//    private static Spinner spinnerMaxDistanceInMiles;
    String email = null;

    private static EditText distanceSearch;
    private Button onSaveSettingsButton;

    // Gender
    private static RadioGroup radioGenderGroup;
    private static RadioButton radioGenderButton;

    // Privacy
    private static RadioGroup radioPrivacyGroup;
    private static RadioButton radioPrivacyButton;

    // Reminder Time button
    private static Button reminderTimeBtn;

    // Age Range spinner
    private static Spinner spinnerAgeRange;

    // Age Range
    final String[] ageRange = { "18 - 25", "26 - 35", "36 - 45"};

    private static List<UserSettings> users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        distanceSearch = view.findViewById(R.id.distanceSearch);
        radioGenderGroup = view.findViewById(R.id.radioGenderGroup);
        radioPrivacyGroup = view.findViewById(R.id.radioPrivacyGroup);
        reminderTimeBtn = view.findViewById(R.id.reminderTime);

        spinnerAgeRange = view.findViewById(R.id.spinnerAgeRange);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinnerAgeRange.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapterForAgeRange =
                new ArrayAdapter(getActivity(),
                        android.R.layout.simple_spinner_item,
                        ageRange);
        arrayAdapterForAgeRange.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        spinnerAgeRange.setAdapter(arrayAdapterForAgeRange);


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
//        spinnerMaxDistanceInMiles = view.findViewById(R.id.spinnerMaxDistanceInMiles);
//        spinnerMaxDistanceInMiles.setOnItemSelectedListener(this);
//
//        ArrayAdapter arrayAdapter =
//                new ArrayAdapter(getActivity(),
//                        android.R.layout.simple_spinner_item,
//                        maxDistanceInMiles);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        //Setting the ArrayAdapter data on the Spinner
//        spinnerMaxDistanceInMiles.setAdapter(arrayAdapter);


        // Time picker
        reminderTimeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        reminderTimeBtn.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Reminder Time");
                mTimePicker.show();

            }
        });

        Bundle bundle = getArguments();

        if (bundle != null) {
            email = bundle.getString(Constants.KEY_EMAIL);
        }
        new GetUserSettingsTask(this, email).execute();


        // Saving the User Settings by clicking the submit button
        onSaveSettingsButton = view.findViewById(R.id.saveSettingsId);
        onSaveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSettings userSettings;
                if (users.size() != 0) {
                    userSettings = users.get(0);
                } else {
                    userSettings = new UserSettings();
                }

                final String reminderTime = reminderTimeBtn.getText().toString();
                final int distanceInMiles = Integer.parseInt(distanceSearch.getText().toString());
                final int selectedGender = radioGenderGroup.getCheckedRadioButtonId();
                final int selectedPrivacy = radioPrivacyGroup.getCheckedRadioButtonId();
                final String ageRange = spinnerAgeRange.getSelectedItem().toString();

                // find the radiobutton by returned id (gender)
                radioGenderButton = view.findViewById(selectedGender);

                // find the radiobutton by returned id (privacy)
                radioPrivacyButton = view.findViewById(selectedPrivacy);

                userSettings.setEmail(email);
                userSettings.setDailyMatchesReminderTime(reminderTime);
                userSettings.setMaxDistanceSearchInMiles(distanceInMiles);
                userSettings.setGender(radioGenderButton.getText().toString());

                // Set the privacy settings
                boolean isPrivate;
                if (radioPrivacyButton.getText().toString().equalsIgnoreCase("Private")) {
                    isPrivate = true;
                } else {
                    isPrivate = false;
                }
                userSettings.setPrivateAccount(isPrivate);

                // Update age range
                userSettings.setAgeRange(ageRange);

                updateUserSettings(userSettings);

            }
        });
        // End of submit button

        return view;
    }

    public void updateUserSettings(final UserSettings userSettings) {
        new SetUserSettingsTask(this, userSettings).execute();
    }


    // For Spinner
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }


    private static class SetUserSettingsTask extends AsyncTask<Void, Void, UserSettings> {

        private WeakReference<Fragment> weakActivity;
        private UserSettings userSettings;

        public SetUserSettingsTask(Fragment fragment, UserSettings userSettings) {
            weakActivity = new WeakReference<>(fragment);
            this.userSettings = userSettings;
        }

        @Override
        protected UserSettings doInBackground(Void... voids) {
            Fragment fragment = weakActivity.get();
            if(fragment == null) {
                return null;
            }

            AppDatabase db = AppDatabaseSingleton.getDatabase(fragment.getActivity());

            db.userSettingsDao().insertUserSettings(userSettings);
            return userSettings;
        }
    }

    private static class GetUserSettingsTask extends AsyncTask<Void, Void, UserSettings> {
        private WeakReference<Fragment> weakFragment;
        private String userEmail;

        public GetUserSettingsTask(Fragment fragment, String userEmail) {
            weakFragment = new WeakReference<>(fragment);
            this.userEmail = userEmail;
        }

        @Override
        protected UserSettings doInBackground(Void... voids) {
            Fragment fragment = weakFragment.get();
            if(fragment == null) {
                return null;
            }

            AppDatabase db = AppDatabaseSingleton.getDatabase(fragment.getActivity());

            String[] emails = { userEmail };

            //List<UserSettings> users = db.userSettingsDao().loadAllByIds(emails);
            users = db.userSettingsDao().loadAllByIds(emails);

            //userSettings = db.userSettingsDao().getUserSettings(email);

            if(users.size() <= 0 || users.get(0) == null) {
                return null;
            }


            return users.get(0);
        }

        @Override
        protected void onPostExecute(UserSettings userSettings) {
            Fragment fragment = weakFragment.get();

            if(userSettings == null || fragment == null) {
                return;
            }

            // Section below is to display the view based from the database
            reminderTimeBtn.setText(userSettings.getDailyMatchesReminderTime());
            distanceSearch.setText(String.valueOf(userSettings.getMaxDistanceSearchInMiles()));

            if (userSettings.getGender() != null) {
                if (userSettings.getGender().equalsIgnoreCase("female")) {
                    radioGenderGroup.check(R.id.radioFemale);
                } else {
                    radioGenderGroup.check(R.id.radioMale);
                }
            }

            if (userSettings.isPrivateAccount()) {
                radioPrivacyGroup.check(R.id.radioPrivate);
            } else {
                radioPrivacyGroup.check(R.id.radioPublic);
            }

            setSpinText(spinnerAgeRange, userSettings.getAgeRange());

        }
    }



    public static void setSpinText(Spinner spin, String text)
    {
        for(int i= 0; i < spin.getAdapter().getCount(); i++)
        {
            if(spin.getAdapter().getItem(i).toString().contains(text))
            {
                spin.setSelection(i);
            }
        }

    }


}
