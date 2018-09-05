package be.henallux.masi.bendergame.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.henallux.masi.bendergame.model.conditions.Condition;

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
                rules = getRulesFromRemainder((HashMap<String,Map>)opts.get("rules"));
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

    private static ArrayList<Rule> getRulesFromRemainder(HashMap<String,Map> rules) {

        ArrayList<Rule> rulesModel = new ArrayList<>();
        if(rules == null)
            return rulesModel;

        for (Map.Entry<String, Map> o : rules.entrySet()) {
            if(o == null || o.getValue() == null) continue;
            Condition condition = Condition.fromMap((Map)o.getValue().get("condition"));
            rulesModel.add(new Rule(o.getKey(),condition,String.valueOf(o.getValue().get("outcome")), String.valueOf(o.getValue().get("title"))));
        }
        return rulesModel;
    }


    protected Game(Parcel in) {
        ID = in.readString();
        mode = (EnumMode) in.readValue(EnumMode.class.getClassLoader());
        if (in.readByte() == 0x01) {
            rules = new ArrayList<>();
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