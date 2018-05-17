package com.example.myfirstapp;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.myfirstapp.models.Matches;

import java.util.List;

public class MatchesDiffCallback extends DiffUtil.Callback {
    private final List<Matches> mOldMatchesList;
    private final List<Matches> mNewMatchesList;

    public MatchesDiffCallback(List<Matches> oldEmployeeList, List<Matches> newEmployeeList) {
        this.mOldMatchesList = oldEmployeeList;
        this.mNewMatchesList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return mOldMatchesList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMatchesList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldMatchesList.get(oldItemPosition).uid == mNewMatchesList.get(
                newItemPosition).uid;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Matches oldMatches = mOldMatchesList.get(oldItemPosition);
        final Matches newMatches = mNewMatchesList.get(newItemPosition);

        return oldMatches.name.equals(newMatches.name);
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
