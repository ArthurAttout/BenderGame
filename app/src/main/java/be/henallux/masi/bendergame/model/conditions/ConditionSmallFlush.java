package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.utils.Constants;
import be.henallux.masi.bendergame.utils.DuplicateChecker;

/**
 * Created by Le Roi Arthur on 01-08-18.
 */

public class ConditionSmallFlush implements Condition {

    private int value;

    public ConditionSmallFlush(int value) {
        this.value = value;
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {

        List<Integer> list = new ArrayList<>(dices);
        Collections.sort(list, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if(o1 < o2) return -1;
                if(o1.equals(o2)) return 0;
                return 1;
            }
        });

        HashMap<Integer,Integer> hash = new HashMap<>();
        for (Integer integer : list) { //remove duplicates
            hash.put(integer,0);
        }


        int consecutiveIntervals = 0;
        int firstConsecutiveOccurence = -1;

        for (int i = 0; i < hash.keySet().size(); i++) {
            if(i+1 < hash.keySet().size()){
                if(((int)hash.keySet().toArray()[i]+1 == (int)hash.keySet().toArray()[i+1])){
                    if(consecutiveIntervals == 0)
                        firstConsecutiveOccurence = (int)hash.keySet().toArray()[i];
                    consecutiveIntervals++;
                }
            }
        }

        if(consecutiveIntervals < 2 || consecutiveIntervals > 2)
            return false;

        if(value != 0){ //Flush musts start with value
            return firstConsecutiveOccurence == value;
        }
        return true;
    }

    protected ConditionSmallFlush(Parcel in) {
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
    public static final Parcelable.Creator<ConditionSmallFlush> CREATOR = new Parcelable.Creator<ConditionSmallFlush>() {
        @Override
        public ConditionSmallFlush createFromParcel(Parcel in) {
            return new ConditionSmallFlush(in);
        }

        @Override
        public ConditionSmallFlush[] newArray(int size) {
            return new ConditionSmallFlush[size];
        }
    };


    public String toString(Context ctx) {
        if(value == 0)
            return ctx.getString(R.string.prefix_condition_small_flush);
        else
            return ctx.getString(R.string.prefix_condition_small_flush_starting_with,value);
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.SMALL_FLUSH);
        map.put(Constants.JSONFields.FIELD_VALUE, value);
        return map;
    }
}
