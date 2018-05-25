package com.example.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.TabLayout;
import com.example.myfirstapp.models.MatchesModel;
import com.example.myfirstapp.viewmodels.MatchesViewModel;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity implements FragmentMatches.OnListFragmentInteractionListener {

    private MatchesViewModel viewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_fragments);

        // Adding Toolbar to Main screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        // Fragment Profile
        FragmentProfile fragmentProfile = new FragmentProfile();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragmentProfile.setArguments(bundle);
        adapter.addFragment(fragmentProfile, "Profile");

        // Fragment Matches
        viewModel = new MatchesViewModel();
        adapter.addFragment(new FragmentMatches(), "Matches");

        // Fragment Settings
        FragmentSettings fragmentSettings = new FragmentSettings();
        fragmentSettings.setArguments(bundle);
        adapter.addFragment(fragmentSettings, "Settings");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void backToMain(View view) {
        Intent intent = new Intent(TabActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(MatchesModel matches) {
        viewModel.updateMatchesItem(matches);
    }
}
