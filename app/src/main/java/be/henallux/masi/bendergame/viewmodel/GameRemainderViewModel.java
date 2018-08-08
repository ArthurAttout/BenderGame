package be.henallux.masi.bendergame.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.Game;

/**
 * Created by Le Roi Arthur on 08-08-18.
 */

public class GameRemainderViewModel extends ViewModel {

    public final MutableLiveData<Game> currentGameLiveData = new MutableLiveData<>();
    public final MutableLiveData<ArrayList<Integer>> dices = new MutableLiveData<>();
    public final MutableLiveData<String> outcome = new MutableLiveData<>();

    public GameRemainderViewModel(){
        currentGameLiveData.setValue(new Game());
        dices.setValue(new ArrayList<Integer>());
        outcome.setValue("");
    }
}
