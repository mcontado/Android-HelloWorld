package com.example.myfirstapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentProfile extends Fragment {

    TextView nameTextView;
    TextView ageTextView;
    TextView emailTextView;
    TextView userNameTextView;
    TextView birthDateTextView;
    TextView descriptionTextView;
    TextView occupationTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Bundle bundle = getArguments();

        if (bundle != null) {
            setViewFromBundle(bundle, view);
        }

        return view;
    }

    private void setViewFromBundle(final Bundle bundle, final View view) {
        String name = bundle.getString(Constants.KEY_NAME);
        nameTextView = view.findViewById(R.id.nameTextViewFragmentProfile);
        nameTextView.setText(name);

        String age = bundle.getString(Constants.KEY_AGE);
        ageTextView = view.findViewById(R.id.ageTextViewFragmentProfile);
        ageTextView.setText(age);

        String email = bundle.getString(Constants.KEY_EMAIL);
        emailTextView = view.findViewById(R.id.emailTextViewFragmentProfile);
        emailTextView.setText(email);

        String userName = bundle.getString(Constants.KEY_USERNAME);
        userNameTextView = view.findViewById(R.id.userNameTextViewFragmentProfile);
        userNameTextView.setText(userName);

        String birthDate = bundle.getString(Constants.KEY_BDATE);
        birthDateTextView = view.findViewById(R.id.birthDateTextViewFragmentProfile);
        birthDateTextView.setText(birthDate);

        String description = bundle.getString(Constants.KEY_DESCRIPTION);
        descriptionTextView = view.findViewById(R.id.descriptionTextViewFragmentProfile);
        descriptionTextView.setText(description);

        String occupation = bundle.getString(Constants.KEY_OCCUPATION);
        occupationTextView = view.findViewById(R.id.occupationTextViewFragmentProfile);
        occupationTextView.setText(occupation);
    }
}
