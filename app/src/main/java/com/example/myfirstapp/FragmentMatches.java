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

import com.example.myfirstapp.models.Matches;
import com.example.myfirstapp.viewmodels.FirebaseMatchesViewModel;

import java.util.ArrayList;
import java.util.List;

public class FragmentMatches extends Fragment {

    public static final String ARG_DATA_SET = "data-set";
    private List<Matches> mDataSet;
    private OnListFragmentInteractionListener mListener;

    private FirebaseMatchesViewModel viewModel;

    private MatchesRecyclerViewAdapter matchesRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        viewModel = new FirebaseMatchesViewModel();

        matchesRecyclerViewAdapter = new MatchesRecyclerViewAdapter(mDataSet, mListener);

        recyclerView.setAdapter(matchesRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getMatches(
                (ArrayList<Matches> matchesList) -> {
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

//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView picture;
//        public TextView name;
//
//        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
//            super(inflater.inflate(R.layout.fragment_matches, parent, false));
//            picture = itemView.findViewById(R.id.card_image);
//            name = itemView.findViewById(R.id.card_title);
//
//            ImageButton favoriteImageButton = itemView.findViewById(R.id.favorite_button);
//            favoriteImageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), "You liked " + name.getText(), Toast.LENGTH_LONG).show();
//
//                }
//            });
//        }
//    }

    //TODO: cleanup: remove this class
    /**
     * Adapter to display recycler view.
     */
//    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
//        // Set numbers of List in RecyclerView.
//        private static final int LENGTH = 5;
//        private final String[] matchesNames;
//        private final Drawable[] matchesPictures;
//
//        public ContentAdapter(Context context) {
//            Resources resources = context.getResources();
//            matchesNames = resources.getStringArray(R.array.matches_names);
//            TypedArray a = resources.obtainTypedArray(R.array.matches_pictures);
//            matchesPictures = new Drawable[a.length()];
//
//            for (int i = 0; i < matchesPictures.length; i++) {
//                matchesPictures[i] = a.getDrawable(i);
//            }
//            a.recycle();
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.picture.setImageDrawable(matchesPictures[position % matchesPictures.length]);
//            holder.name.setText(matchesNames[position % matchesNames.length]);
//        }
//
//        @Override
//        public int getItemCount() {
//            return LENGTH;
//        }
//    }

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
        void onListFragmentInteraction(Matches matches);
    }
}
