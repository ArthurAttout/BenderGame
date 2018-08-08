package be.henallux.masi.bendergame.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;

import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.model.Rule;

/**
 * Created by Le Roi Arthur on 08-08-18.
 */

public class CreateRuleViewModel extends ViewModel {

    public final MutableLiveData<Rule> newRule = new MutableLiveData<>();
    public final MutableLiveData<EnumTypeCondition> chosenType = new MutableLiveData<>();
    public final MutableLiveData<Integer> chosenValue = new MutableLiveData<>();

    public CreateRuleViewModel() {
        newRule.setValue(new Rule());
        chosenValue.setValue(0);
        chosenType.setValue(EnumTypeCondition.DOUBLE);
    }
}
