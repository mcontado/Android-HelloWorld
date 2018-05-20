package com.example.myfirstapp;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfirstapp.FragmentMatches.OnListFragmentInteractionListener;
import com.example.myfirstapp.models.MatchesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.LTGRAY;
import static android.graphics.Color.RED;

public class MatchesRecyclerViewAdapter extends RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder> {
    private List<MatchesModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MatchesRecyclerViewAdapter(List<MatchesModel> matches, OnListFragmentInteractionListener listener) {
        mValues = matches;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_matches, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mMatches = mValues.get(position);
        Picasso.get().load(mValues.get(position).imageUrl).into(holder.mImageView);
        holder.mTitleView.setText(mValues.get(position).name);
        Boolean liked = mValues.get(position).liked;
        if(liked) {
            holder.favoriteImageButton.setColorFilter(RED);
        } else {
            holder.favoriteImageButton.setColorFilter(LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public MatchesModel mMatches;
        public ImageButton favoriteImageButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.card_image);
            mTitleView = view.findViewById(R.id.card_title);

            favoriteImageButton = itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mMatches.liked = !mMatches.liked;
                        if (mMatches.liked) {
                            favoriteImageButton.setColorFilter(RED);
                            Toast.makeText(v.getContext(), "You liked " + mTitleView.getText(), Toast.LENGTH_LONG).show();
                        } else {
                            favoriteImageButton.setColorFilter(LTGRAY);
                            Toast.makeText(v.getContext(), "You no longer liked " + mTitleView.getText(), Toast.LENGTH_LONG).show();
                        }
                        // Notify the active callbacks interface (the activity, if the fragment
                        // is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(mMatches);
                    }

                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }

    public void updateMatchListItems(List<MatchesModel> matches) {
        if (mValues == null) {
            mValues = new ArrayList<>();
        }
        final MatchesDiffCallback diffCallback = new MatchesDiffCallback(mValues, matches);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        mValues.clear();
        mValues.addAll(matches);
        diffResult.dispatchUpdatesTo(this);
    }
}
