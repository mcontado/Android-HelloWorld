package com.example.myfirstapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myfirstapp.entity.UserSettings;
import com.example.myfirstapp.models.MatchesModel;
import com.example.myfirstapp.viewmodels.MatchesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentMatches extends Fragment {

    public static final String ARG_DATA_SET = "data-set";
    private List<MatchesModel> mDataSet;
    private OnListFragmentInteractionListener mListener;

    private MatchesViewModel matchesViewModel;

    private MatchesRecyclerViewAdapter matchesRecyclerViewAdapter;

    LocationManager locationManager;

    private static final int SECOND = 60;
    private static final int MILLISECOND = 1000;
    private static final int MAX_DISTANCE_IN_MILES = 10;
    String email = null;
    private Location myLocation;
    private static final double MILE_TO_METER = 1609.34;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        matchesViewModel = new MatchesViewModel();

        matchesRecyclerViewAdapter = new MatchesRecyclerViewAdapter(mDataSet, mListener);

        recyclerView.setAdapter(matchesRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        matchesViewModel.getMatches(
                (ArrayList<MatchesModel> matchesList) -> {
                    matchesRecyclerViewAdapter.updateMatchListItems(matchesList);
                }
        );

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        checkNetworkUpdates();

        return recyclerView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDataSet = getArguments().getParcelableArrayList(ARG_DATA_SET);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(MatchesModel matches);
    }


    public void checkNetworkUpdates() {
        if(!checkLocation()) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, SECOND * MILLISECOND, 1000,
                    locationListenerNetwork);
        }
    }

    private boolean checkLocation() {
        if(!isLocationEnabled()) {
            showAlert();
        }
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.enable_location)
                .setMessage(getString(R.string.location_message))
                .setPositiveButton(R.string.location_settings, (paramDialogInterface, paramInt) -> {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton(R.string.location_cancel, (paramDialogInterface, paramInt) -> {});
        dialog.show();
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            // This is the current user's location
            myLocation = location;
            //longitudeNetwork = location.getLongitude();
            //latitudeNetwork = location.getLatitude();

            getActivity().runOnUiThread(()-> {
                filterMatches();
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    };

    private void filterMatches() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            email = bundle.getString(Constants.KEY_EMAIL);
        }

        String[] emailList = { email };

        final AppDatabase db = Room.databaseBuilder(getActivity(),
                AppDatabase.class, "app-database").allowMainThreadQueries().build();

        List<UserSettings> userSettings = db.userSettingsDao().loadAllByIds(emailList);

        int maxDistance;
        if (userSettings.size() != 0) {
            maxDistance = userSettings.get(0).getMaxDistanceSearchInMiles();
        } else {
            maxDistance = MAX_DISTANCE_IN_MILES;
        }

        filterMatchesBasedOnUserLocation(maxDistance);
    }

    private void filterMatchesBasedOnUserLocation(int userSettingsMaxDistance) {
        matchesViewModel.getMatches(
                (ArrayList<MatchesModel> matchesList) -> {
                    final ArrayList<MatchesModel> filteredListOfMatches = new ArrayList<>();

                    for (MatchesModel matchesModel: matchesList) {
                        final boolean isWithinMaxDistanceSearch =
                                evaluateDistanceSearch(Double.parseDouble(matchesModel.longitude),
                                        Double.parseDouble(matchesModel.lat),
                                        userSettingsMaxDistance);

                        if (isWithinMaxDistanceSearch) {
                            filteredListOfMatches.add(matchesModel);
                        }
                    }

                    matchesRecyclerViewAdapter.updateMatchListItems(filteredListOfMatches);
                }
        );
    }

    private boolean evaluateDistanceSearch(double matchesLongitude, double matchesLatitude,
                                           int userSettingsMaxDistanceInMiles) {

        Location matchesLocation = new Location("");
        matchesLocation.setLatitude(matchesLatitude);
        matchesLocation.setLongitude(matchesLongitude);

        // distanceTo - returns distance in meters.
        final double myDistanceToMatchesLocationInMeters = myLocation.distanceTo(matchesLocation);
        final double userSettingsDistanceInMeters = userSettingsMaxDistanceInMiles * MILE_TO_METER;

        if (myDistanceToMatchesLocationInMeters < userSettingsDistanceInMeters) {
            return true;
        }
        return false;
    }
}
