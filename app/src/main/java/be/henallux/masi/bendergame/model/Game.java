package be.henallux.masi.bendergame.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.model.conditions.ConditionBigFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionContains;
import be.henallux.masi.bendergame.model.conditions.ConditionDouble;
import be.henallux.masi.bendergame.model.conditions.ConditionDoubleDouble;
import be.henallux.masi.bendergame.model.conditions.ConditionQuadruple;
import be.henallux.masi.bendergame.model.conditions.ConditionSmallFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqual;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrBelow;
import be.henallux.masi.bendergame.model.conditions.ConditionSumEqualOrGreater;
import be.henallux.masi.bendergame.model.conditions.ConditionTriple;
import be.henallux.masi.bendergame.utils.EnumMode;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public class Game implements Parcelable {

    private String ID;
    private EnumMode mode;
    private ArrayList<Rule> rules;

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public Game() {
    }

    public EnumMode getMode() {
        return mode;
    }

    public void setMode(EnumMode mode) {
        this.mode = mode;
    }

    public Game(String ID,EnumMode mode,ArrayList<Rule> rules) {
        this.ID = ID;
        this.rules = rules;
        this.mode = mode;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public static Game fromDataSnapshot(Map opts){

        String gameID = String.valueOf(opts.get("id"));
        String modeString = String.valueOf(opts.get("mode"));
        EnumMode mode = EnumMode.valueOf(modeString);
        ArrayList<Rule> rules = null;

        switch(mode){

            case MODE_REMAINDER:
                rules = getRulesFromRemainder((HashMap<Long,Map>)opts.get("rules"));
                return new Game(gameID,mode,rules);

            case MODE_NO_REAL_DICES:
                rules = getRulesFromNoRealDices((ArrayList)opts.get("rules"));
                return new Game(gameID,mode,rules);

            default:
                throw new IllegalStateException("Unrecognized game mode");
        }
    }


    private static ArrayList<Rule> getRulesFromNoRealDices(ArrayList rules) {
        return null;
    }

    private static ArrayList<Rule> getRulesFromRemainder(HashMap<Long,Map> rules) {

        ArrayList<Rule> rulesModel = new ArrayList<>();
        if(rules == null)
            return rulesModel;

        for (Map o : rules.values()) {
            if(o == null) continue;
            Condition condition = getConditionFromMap((Map)o.get("condition"));
            rulesModel.add(new Rule(condition,String.valueOf(o.get("outcome")), String.valueOf(o.get("title"))));
        }
        return rulesModel;
    }

    private static Condition getConditionFromMap(Map condition) {
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
        }
        return null;
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

    protected Game(Parcel in) {
        ID = in.readString();
        mode = (EnumMode) in.readValue(EnumMode.class.getClassLoader());
        if (in.readByte() == 0x01) {
            rules = new ArrayList<Rule>();
            in.readList(rules, Rule.class.getClassLoader());
        } else {
            rules = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeValue(mode);
        if (rules == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(rules);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}