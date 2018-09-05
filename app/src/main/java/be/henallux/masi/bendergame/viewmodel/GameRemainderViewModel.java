package be.henallux.masi.bendergame.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.utils.Constants;

/**
 * Created by Le Roi Arthur on 08-08-18.
 */

public class GameRemainderViewModel extends ViewModel {

    public final MutableLiveData<Game> currentGameLiveData = new MutableLiveData<>();
    public final MutableLiveData<ArrayList<Integer>> dices = new MutableLiveData<>();
    public final MutableLiveData<String> outcome = new MutableLiveData<>();
    private final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    public final MutableLiveData<Boolean> showDeleteIcon = new MutableLiveData<>();

    public GameRemainderViewModel(){
        currentGameLiveData.setValue(new Game());
        dices.setValue(new ArrayList<>());
        outcome.setValue("");
        showDeleteIcon.setValue(false);
    }

    public void deleteRule(Rule rule) {
        firebaseDatabase
                .child(Constants.JSONFields.FIELD_ROOT_GAME)
                .child(currentGameLiveData.getValue().getID())
                .child(Constants.JSONFields.FIELD_GAME_RULES)
                .child(rule.getID()).removeValue();
    }
}
