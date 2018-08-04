package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.HashMap;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.utils.Constants;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public class ConditionSumEqualOrGreater implements Condition, Parcelable {

    private int sumCondition;

    public ConditionSumEqualOrGreater(int sumCondition) {
        this.sumCondition = sumCondition;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        int total = 0;
        for (Integer dice : dices) {
            total += dice;
        }
        return total >= sumCondition;
    }

    protected ConditionSumEqualOrGreater(Parcel in) {
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
    public static final Parcelable.Creator<ConditionSumEqualOrGreater> CREATOR = new Parcelable.Creator<ConditionSumEqualOrGreater>() {
        @Override
        public ConditionSumEqualOrGreater createFromParcel(Parcel in) {
            return new ConditionSumEqualOrGreater(in);
        }

        @Override
        public ConditionSumEqualOrGreater[] newArray(int size) {
            return new ConditionSumEqualOrGreater[size];
        }
    };

    public String toString(Context ctx) {
        return ctx.getString(R.string.prefix_condition_sum_equal_or_greater,sumCondition);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.SUM_EQUAL_OR_GREATER);
        map.put(Constants.JSONFields.FIELD_VALUE, sumCondition);
        return map;
    }
}