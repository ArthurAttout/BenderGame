package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.utils.Constants;

/**
 * Created by Le Roi Arthur on 01-08-18.
 */

public class ConditionBigFlush extends Condition {
    private int value;

    public ConditionBigFlush(int value) {
        this.value = value;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {

        List<Integer> list = new ArrayList<>(dices);

        HashMap<Integer,Integer> hash = new HashMap<>();
        for (Integer integer : list) { //remove duplicates
            hash.put(integer,0);
        }

        ArrayList<Integer> listNoDuplicates = new ArrayList<>(hash.keySet());

        Collections.sort(listNoDuplicates, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 < o2) return -1;
                if(o1.equals(o2)) return 0;
                return 1;
            }
        });

        int consecutiveIntervals = 0;
        int firstConsecutiveOccurence = -1;

        for (int i = 0; i < listNoDuplicates.size(); i++) {
            if(i+1 < listNoDuplicates.size()){
                if(((int)listNoDuplicates.toArray()[i]+1 == (int)listNoDuplicates.toArray()[i+1])){
                    if(consecutiveIntervals == 0)
                        firstConsecutiveOccurence = (int)listNoDuplicates.toArray()[i];
                    consecutiveIntervals++;
                }
            }
        }

        if(consecutiveIntervals < 3 || consecutiveIntervals > 3)
            return false;

        if(value != 0){ //Flush musts start with value
            return firstConsecutiveOccurence == value;
        }
        return true;
    }

    protected ConditionBigFlush(Parcel in) {
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
    public static final Parcelable.Creator<ConditionBigFlush> CREATOR = new Parcelable.Creator<ConditionBigFlush>() {
        @Override
        public ConditionBigFlush createFromParcel(Parcel in) {
            return new ConditionBigFlush(in);
        }

        @Override
        public ConditionBigFlush[] newArray(int size) {
            return new ConditionBigFlush[size];
        }
    };


    public String toString(Context ctx) {
        if(value == 0)
            return ctx.getString(R.string.prefix_condition_big_flush);
        else
            return ctx.getString(R.string.prefix_condition_big_flush_starts_with,value);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.BIG_FLUSH);
        map.put(Constants.JSONFields.FIELD_VALUE, value);
        return map;
    }

    @Override
    public EnumTypeCondition getType() {
        return EnumTypeCondition.BIG_FLUSH;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return new ArrayList<Integer>(){{
            add(value);
        }};
    }
}
