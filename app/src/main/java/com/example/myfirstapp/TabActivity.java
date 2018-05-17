package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.TabLayout;
import android.widget.FrameLayout;

import com.example.myfirstapp.models.Matches;
import com.example.myfirstapp.viewmodels.FirebaseMatchesViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity implements FragmentMatches.OnListFragmentInteractionListener {

    private FirebaseMatchesViewModel viewModel;
    //private FrameLayout frameLayout;
    public static final String ARG_DATA_SET = "data-set";

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
        //Adapter adapter = new Adapter(getFragmentManager());
        Adapter adapter = new Adapter(getSupportFragmentManager());

        // Fragment Profile
        FragmentProfile fragmentProfile = new FragmentProfile();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fragmentProfile.setArguments(bundle);
        adapter.addFragment(fragmentProfile, "Profile");

        // Fragment Matches
        viewModel = new FirebaseMatchesViewModel();

        FragmentMatches fragmentMatches = new FragmentMatches();
        viewModel.getMatches(
                (ArrayList<Matches> matchesList) -> {
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentMatches fragment = (FragmentMatches) manager.findFragmentByTag("fragmentMatches");

                    if (fragment != null) {
                        // remove fragment to re-add it
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.remove(fragment);
                        transaction.commit();
                    }

                    Bundle bundleForFragmentMatches = new Bundle();
                    bundleForFragmentMatches.putParcelableArrayList(ARG_DATA_SET, matchesList);

                    fragmentMatches.setArguments(bundleForFragmentMatches);

                }
        );
        adapter.addFragment(fragmentMatches, "Matches");

        // Fragment Settings
        adapter.addFragment(new FragmentSettings(), "Settings");
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
    public void onListFragmentInteraction(Matches matches) {
        matches.liked = true;
        viewModel.updateMatchesItem(matches);
    }
}
