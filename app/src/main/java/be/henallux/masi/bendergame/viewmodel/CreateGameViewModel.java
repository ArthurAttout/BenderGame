package be.henallux.masi.bendergame.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.HashMap;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumMode;
import be.henallux.masi.bendergame.model.Mode;
import be.henallux.masi.bendergame.model.Rule;
import be.henallux.masi.bendergame.model.conditions.ConditionQuadruple;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrBelow;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrGreater;
import be.henallux.masi.bendergame.model.conditions.ConditionTriple;

public class CreateGameViewModel extends ViewModel {

    public final MutableLiveData<Mode> availableModes = new MutableLiveData<>();
    public final MutableLiveData<EnumMode> chosenMode = new MutableLiveData<>();


    public HashMap<String,HashMap> getDefaultRules(Context ctx) {
        return new HashMap<String,HashMap>(){{
            put("def1",new Rule(
                    new ConditionTriple(0),
                    ctx.getString(R.string.default_rule_invent_rule),
                    ctx.getString(R.string.default_rule_invent_rule_title)).getHashMap());
            put("def2",new Rule(
                    new ConditionQuadruple(0),
                    ctx.getString(R.string.default_rule_cancel_rule),
                    ctx.getString(R.string.default_rule_cancel_rule_title)).getHashMap());
            put("def3",new Rule(
                    new ConditionSumEqualOrGreater(21),
                    ctx.getString(R.string.default_rule_all_drink),
                    ctx.getString(R.string.default_rule_all_drink_title)).getHashMap());
            put("def4",new Rule(
                    new ConditionSumEqualOrBelow(7),
                    ctx.getString(R.string.default_rule_thrower_drinks),
                    ctx.getString(R.string.default_rule_thrower_drinks_title)).getHashMap());
        }};
    }

}
