package be.henallux.masi.bendergame.utils;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;
import java.util.Map;

import be.henallux.masi.bendergame.model.Game;
import be.henallux.masi.bendergame.model.Rule;

/**
 * Created by Le Roi Arthur on 31-07-18.
 */

public class DiceCheckerSpinnerListener implements AdapterView.OnItemSelectedListener {

    private DiceChangedNotifier listener;
    private Game currentGame;
    private HashMap<Integer,Integer> hashMapViewInteger = new HashMap<>();

    public DiceCheckerSpinnerListener(DiceChangedNotifier listener, Game currentGame) {
        this.listener = listener;
        this.currentGame = currentGame;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hashMapViewInteger.put(parent.getId(),position+1);
        if(hashMapViewInteger.size() == 4){

            String outcome = "";
            for (Rule rule : currentGame.getRules()) {
                if(rule.getCondition().isSatisfied(hashMapViewInteger.values())){
                    outcome += rule.getOutcome() + "\n";
                }
            }
            listener.diceChanged(outcome.equals("") ? "Rien." : outcome);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public interface DiceChangedNotifier{
        void diceChanged(String rulesToShow);
    }
}


