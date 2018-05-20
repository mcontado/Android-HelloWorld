package com.example.myfirstapp.viewmodels;

import android.annotation.TargetApi;

import com.example.myfirstapp.datamodels.MatchesDataModel;
import com.example.myfirstapp.models.MatchesModel;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MatchesViewModel {

    MatchesDataModel matchesDataModel;

    public MatchesViewModel() {
        matchesDataModel = new MatchesDataModel();
    }

    @TargetApi(24)
    public void getMatches(Consumer<ArrayList<MatchesModel>> responseCallBack) {
        matchesDataModel.getMatches(
                (DataSnapshot dataSnapshot) -> {
                    ArrayList<MatchesModel> listOfMatches = new ArrayList<>();
                    for (DataSnapshot matchesSnapshot: dataSnapshot.getChildren()) {
                        MatchesModel matchesModel = matchesSnapshot.getValue(MatchesModel.class);
                        assert matchesModel != null;
                        matchesModel.uid = matchesSnapshot.getKey();
                        listOfMatches.add(matchesModel);
                    }
                    responseCallBack.accept(listOfMatches);
                },
                (databaseError -> System.out.println("Error reading matches items: " + databaseError))
        );
    }

    public void updateMatchesItem(MatchesModel matchesModel) {
        matchesDataModel.updateMatchesById(matchesModel);
    }

    public void clear() {
        matchesDataModel.clear();
    }
}
