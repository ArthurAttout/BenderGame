package be.henallux.masi.bendergame.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.utils.Constants;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public class Rule implements Parcelable {

    private Condition condition;
    private String outcome;
    private String title;

    public Rule() {
    }

    public Rule(Condition condition, String outcome, String title) {
        this.condition = condition;
        this.outcome = outcome;
        this.title = title;
    }

    public Condition getCondition() {
        return condition;
    }

    public String getTitle() {
        return title;
    }

    public String getOutcome() {
        return outcome;
    }

    protected Rule(Parcel in) {
        condition = (Condition) in.readValue(Condition.class.getClassLoader());
        outcome = in.readString();
        title = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(condition);
        dest.writeString(outcome);
        dest.writeString(title);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Rule> CREATOR = new Parcelable.Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

    public HashMap<String,Object> getHashMap() {
        HashMap<String,Object> map = new HashMap<>();

        map.put(Constants.JSONFields.FIELD_OUTCOME,outcome);
        map.put(Constants.JSONFields.FIELD_TITLE,title);
        map.put(Constants.JSONFields.FIELD_CONDITION,condition.getHashMap());

        return map;
    }
}