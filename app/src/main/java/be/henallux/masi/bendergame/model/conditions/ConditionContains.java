package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import be.henallux.masi.bendergame.R;
import be.henallux.masi.bendergame.model.EnumTypeCondition;
import be.henallux.masi.bendergame.utils.Constants;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public class ConditionContains extends Condition implements Parcelable {

    private ArrayList<Integer> value;

    public ConditionContains(ArrayList<Integer> value) {
        this.value = value;
        Iterator<Integer> iterator = value.iterator();
        while(iterator.hasNext()){
            if(iterator.next() == 0)
                iterator.remove();
        }
    }

    @Override
    public boolean isSatisfied(Collection<Integer> dices) {
        return dices == null || dices.containsAll(value);

    }

    protected ConditionContains(Parcel in) {
        if (in.readByte() == 0x01) {
            value = new ArrayList<Integer>();
            in.readList(value, Integer.class.getClassLoader());
        } else {
            value = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (value == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(value);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ConditionContains> CREATOR = new Parcelable.Creator<ConditionContains>() {
        @Override
        public ConditionContains createFromParcel(Parcel in) {
            return new ConditionContains(in);
        }

        @Override
        public ConditionContains[] newArray(int size) {
            return new ConditionContains[size];
    }
    };


    public String toString(Context ctx) {
        switch (value.size()){
            case 0:
                return "";
            case 1:
                return ctx.getString(R.string.prefix_condition_contains_1,value.get(0));
            case 2:
                return ctx.getString(R.string.prefix_condition_contains_2,value.get(0),value.get(1));
            case 3:
                return ctx.getString(R.string.prefix_condition_contains_3,value.get(0),value.get(1),value.get(2));
            case 4:
                return ctx.getString(R.string.prefix_condition_contains_4,value.get(0),value.get(1),value.get(2),value.get(3));
            default:
                throw new IllegalStateException("Illegal condition");
        }
    }

    @Override
    public HashMap<String, Object> getHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Constants.JSONFields.FIELD_TYPE, EnumTypeCondition.CONTAINS);
        map.put(Constants.JSONFields.FIELD_VALUES, value);
        return map;
    }


    @Override
    public EnumTypeCondition getType() {
        return EnumTypeCondition.CONTAINS;
    }

    @Override
    public ArrayList<Integer> getValues() {
        return value;
    }
}