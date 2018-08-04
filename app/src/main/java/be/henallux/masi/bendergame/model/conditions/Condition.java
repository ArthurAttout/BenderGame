package be.henallux.masi.bendergame.model.conditions;

import android.content.Context;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Le Roi Arthur on 30-07-18.
 */

public interface Condition extends Parcelable {

    boolean isSatisfied(Collection<Integer> dices);
    String toString(Context ctx);
    HashMap<String,Object> getHashMap();
}
