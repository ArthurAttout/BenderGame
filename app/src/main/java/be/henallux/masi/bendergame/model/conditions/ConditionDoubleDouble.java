package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.HashMap;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.DuplicateChecker;

/**
 * Created by Le Roi Arthur on 02-08-18.
 */

public class ConditionDoubleDouble implements Condition {

    private int value1;
    private int value2;

    public ConditionDoubleDouble(int value1, int value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        if(value1 == 0 && value2 == 0){
            HashMap<Integer, Integer> integerIntegerHashMap = DuplicateChecker.hasDuplicates(dices);
            return integerIntegerHashMap.size() == 2;
        }
        int count1 = 0;
        int count2 = 0;

        for (Integer dice : dices) {
            if(dice.equals(value1)) count1++;
            if(dice.equals(value2)) count2++;
        }
        return count1 == 2 && count2 == 2;
    }

    protected ConditionDoubleDouble(Parcel in) {
        value1 = in.readInt();
        value2 = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(value1);
        dest.writeInt(value2);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConditionDoubleDouble> CREATOR = new Parcelable.Creator<ConditionDoubleDouble>() {
        @Override
        public ConditionDoubleDouble createFromParcel(Parcel in) {
            return new ConditionDoubleDouble(in);
        }

        @Override
        public ConditionDoubleDouble[] newArray(int size) {
            return new ConditionDoubleDouble[size];
        }
    };

    public String toString(Context ctx) {
        if(value1 == 0 && value2 == 0){
            return ctx.getString(R.string.prefix_condition_any_double_double);
        }

        if(value2 == 0){
            return ctx.getString(R.string.prefix_condition_double_and_any,value1);
        }

        if(value1 == 0){
            return ctx.getString(R.string.prefix_condition_double_and_any,value2);
        }

        return ctx.getString(R.string.prefix_condition_double_double,value1,value2);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.DOUBLE_DOUBLE);
        map.put(Constants.JSONFields.FIELD_VALUE, new int[]{value2,value1});
        return map;
    }
}
