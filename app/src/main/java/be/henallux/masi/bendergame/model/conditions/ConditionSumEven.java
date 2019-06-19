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

public class ConditionSumEven extends Condition {

    public ConditionSumEven() {}

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        int total = 0;
        for (Integer dice : dices) {
            total += dice;
        }

        return total % 2 == 0;
    }

    @Override
    public String toString(Context ctx) {
        return ctx.getString(R.string.prefix_condition_sum_even);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.SUM_EVEN);
        return map;
    }

    @Override
    public EnumTypeCondition getType() {
        return EnumTypeCondition.SUM_EVEN;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ConditionSumEven(Parcel in) {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConditionSumEven> CREATOR = new Parcelable.Creator<ConditionSumEven>() {
        @Override
        public ConditionSumEven createFromParcel(Parcel in) {
            return new ConditionSumEven(in);
        }

        @Override
        public ConditionSumEven[] newArray(int size) {
            return new ConditionSumEven[size];
        }
    };

}
