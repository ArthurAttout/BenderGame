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
 * Created by Le Roi Arthur on 30-07-18.
 */

public class ConditionQuadruple extends Condition implements Parcelable {

    private int value;

    public ConditionQuadruple(int value) {
        this.value = value;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        if(value == 0){
            HashMap<Integer, Integer> integerIntegerHashMap = DuplicateChecker.hasDuplicates(dices);
            return integerIntegerHashMap.size() == 1 && integerIntegerHashMap.entrySet().iterator().next().getValue() == 4;
        }
        int count = 0;
        for (Integer dice : dices) {
            if(dice.equals(value)) count++;
        }
        return count == 4;
    }

    protected ConditionQuadruple(Parcel in) {
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
    public static final Parcelable.Creator<ConditionQuadruple> CREATOR = new Parcelable.Creator<ConditionQuadruple>() {
        @Override
        public ConditionQuadruple createFromParcel(Parcel in) {
            return new ConditionQuadruple(in);
        }

        @Override
        public ConditionQuadruple[] newArray(int size) {
            return new ConditionQuadruple[size];
        }
    };


    public String toString(Context ctx) {
        if(value == 0) //any quadruple
        {
            return ctx.getString(R.string.prefix_condition_any_quadruple);
        }
        return ctx.getString(R.string.prefix_condition_quadruple,value);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.QUADRUPLE);
        map.put(Constants.JSONFields.FIELD_VALUE, value);
        return map;
    }


    @Override
    public EnumTypeCondition getType() {
        return EnumTypeCondition.QUADRUPLE;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return new ArrayList<Integer>(){{
            add(value);
        }};
    }
}