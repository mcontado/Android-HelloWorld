package com.example.myfirstapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
}
