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
    private Boolean visible;
    private String ID;

    public Rule() {

    }

    public Rule(String ID,Condition condition, String outcome, String title, Boolean visible) {
        this.ID = ID;
        this.condition = condition;
        this.outcome = outcome;
        this.title = title;
        this.visible = visible;
    }

    public Rule(String ID,Condition condition, String outcome, String title) {
        this.ID = ID;
        this.condition = condition;
        this.outcome = outcome;
        this.title = title;
        this.visible = true;
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
        ID = in.readString();
        condition = (Condition) in.readValue(Condition.class.getClassLoader());
        outcome = in.readString();
        title = in.readString();
        visible = in.readInt() == 1;
    }

    public Boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeValue(condition);
        dest.writeString(outcome);
        dest.writeString(title);
        dest.writeInt(visible ? 1 : 0);
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