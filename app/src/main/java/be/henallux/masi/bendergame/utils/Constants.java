package be.henallux.masi.bendergame.utils;

import android.os.Bundle;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public class Constants {
    public static final String EXTRA_GAME_KEY = "extra_game_key";
    public static final String EXTRA_GAME_KEY_FRAGMENT = "extra_game_key_fragment";
    public static final String CURRENT_STEP_POSITION_KEY = "current_step_position_key";
    public static final int REQUEST_CODE_CREATE_RULE = 0x87;
    public static final String EXTRA_RULE_KEY = "extra_rule_key";

    public class JSONFields{
        public static final String FIELD_CONDITION = "condition";
        public static final String FIELD_OUTCOME = "outcome";
        public static final String FIELD_TITLE = "title";
        public static final String FIELD_VALUE = "value";
        public static final String FIELD_TYPE = "type";
        public static final String FIELD_VALUES = "values";
    }
}
