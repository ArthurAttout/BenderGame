package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.henallux.masi.bendergame.model.EnumTypeCondition;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public abstract class Condition implements Parcelable {

    public abstract boolean isSatisfied(Collection<Integer> dices);
    public abstract String toString(Context ctx);
    public abstract HashMap<String,Object> getHashMap();
    public abstract EnumTypeCondition getType();
    public abstract ArrayList<Integer> getValues();

    public static Condition fromMap(Map condition){
        EnumTypeCondition conditionType = EnumTypeCondition.valueOf(String.valueOf(condition.get("type")));
        int value = 0;
        ArrayList<Integer> values = null;
        switch (conditionType){

            case CONTAINS:
                values = convertToArrayListLong(condition.get("values"));
                return new ConditionContains(values);

            case SUM_EQUAL_OR_BELOW:
                value = (int)((long)condition.get("value"));
                return new ConditionSumEqualOrBelow(value);

            case SUM_EQUAL_OR_GREATER:
                value = (int)((long)condition.get("value"));
                return new ConditionSumEqualOrGreater(value);

            case SUM_EQUAL:
                value = (int)((long)condition.get("value"));
                return new ConditionSumEqual(value);

            case DOUBLE:
                value = (int)((long)condition.get("value"));
                return new ConditionDouble(value);

            case TRIPLE:
                value = (int)((long)condition.get("value"));
                return new ConditionTriple(value);

            case QUADRUPLE:
                value = (int)((long)condition.get("value"));
                return new ConditionQuadruple(value);

            case BIG_FLUSH:
                value = (int)((long)condition.get("value"));
                return new ConditionBigFlush(value);

            case SMALL_FLUSH:
                value = (int)((long)condition.get("value"));
                return new ConditionSmallFlush(value);

            case DOUBLE_DOUBLE:
                values = convertToArrayListLong(condition.get("values"));
                return new ConditionDoubleDouble(values.get(0),values.get(1));

            case SUM_EVEN:
                return new ConditionSumEven();

            case SUM_ODD:
                return new ConditionSumOdd();

            default:
                throw new IllegalArgumentException("Unrecognized condition type : " + conditionType);
        }
    }

    private static ArrayList<Integer> convertToArrayListLong(Object values) {
        List<Long> longs = (List<Long>)values;
        int nInts = longs.size();
        List<Integer> ints = new ArrayList<Integer>(nInts);
        for (int i=0;i<nInts;++i) {
            ints.add(longs.get(i).intValue());
        }

        return new ArrayList<>(ints);
    }
}
