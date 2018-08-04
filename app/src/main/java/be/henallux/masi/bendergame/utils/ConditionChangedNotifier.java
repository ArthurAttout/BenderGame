package be.henallux.masi.bendergame.utils;

import be.henallux.masi.bendergame.model.EnumTypeCondition;

/**
 * Created by Le Roi Arthur on 04-08-18.
 */

public interface ConditionChangedNotifier {
    void onConditionTitleChanged(String title);
    void onConditionTypeChanged(EnumTypeCondition type);
    void onConditionValueChanged(int value);
    void onConditionOutcomeChanged(String outcome);
}
