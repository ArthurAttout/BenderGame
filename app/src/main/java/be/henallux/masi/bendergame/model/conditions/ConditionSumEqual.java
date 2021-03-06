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

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public class ConditionSumEqual extends Condition implements Parcelable {

    private int sumCondition;

    public ConditionSumEqual(int sumCondition) {
        this.sumCondition = sumCondition;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        int total = 0;
        for (Integer dice : dices) {
            total += dice;
        }
        return total == sumCondition;
    }

    protected ConditionSumEqual(Parcel in) {
        sumCondition = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sumCondition);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConditionSumEqual> CREATOR = new Parcelable.Creator<ConditionSumEqual>() {
        @Override
        public ConditionSumEqual createFromParcel(Parcel in) {
            return new ConditionSumEqual(in);
        }

        @Override
        public ConditionSumEqual[] newArray(int size) {
            return new ConditionSumEqual[size];
        }
    };

    public String toString(Context ctx) {
        return ctx.getString(R.string.prefix_condition_sum_equal,sumCondition);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.SUM_EQUAL);
        map.put(Constants.JSONFields.FIELD_VALUE, sumCondition);
        return map;
    }


    @Override
    public EnumTypeCondition getType() {
        return EnumTypeCondition.SUM_EQUAL;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return new ArrayList<Integer>(){{
            add(sumCondition);
        }};
    }
}