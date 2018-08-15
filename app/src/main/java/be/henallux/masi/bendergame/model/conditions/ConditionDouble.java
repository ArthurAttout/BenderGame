package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.DuplicateChecker;

/**
 * Created by Le Roi Arthur on 02-08-18.
 */

public class ConditionDouble extends Condition {private int value;

    public ConditionDouble(int value) {
        this.value = value;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        if(value == 0){ //Any double
            HashMap<Integer, Integer> integerIntegerHashMap = DuplicateChecker.hasDuplicates(dices);
            return integerIntegerHashMap.size() == 1 && integerIntegerHashMap.entrySet().iterator().next().getValue() == 2;
        }
        int count = 0;
        for (Integer dice : dices) {
            if(dice.equals(value)) count++;
        }
        return count == 2;
    }

    protected ConditionDouble(Parcel in) {
        value = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConditionDouble> CREATOR = new Parcelable.Creator<ConditionDouble>() {
        @Override
        public ConditionDouble createFromParcel(Parcel in) {
            return new ConditionDouble(in);
        }

        @Override
        public ConditionDouble[] newArray(int size) {
            return new ConditionDouble[size];
        }
    };

    public String toString(Context ctx) {
        if(value == 0){
            return ctx.getString(R.string.prefix_condition_any_double);
        }
        return ctx.getString(R.string.prefix_condition_double,value);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.DOUBLE);
        map.put(Constants.JSONFields.FIELD_VALUE, value);
        return map;
    }


    @Override
    public EnumTypeCondition getType() {
        return EnumTypeCondition.DOUBLE;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return new ArrayList<Integer>(){{
            add(value);
        }};
    }
}
