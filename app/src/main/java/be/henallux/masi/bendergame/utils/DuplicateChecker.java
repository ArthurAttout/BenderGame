package be.henallux.masi.bendergame.utils;

import java.util.Set;

/**
 * Created by Le Roi Arthur on 01-08-18.
 */

public class DuplicateChecker {

    public static HashMap<Integer,Integer> hasDuplicates(Collection<Integer> integers){
        HashMap<Integer,Integer> possibleValues = new HashMap<>();
        for (Integer integer : integers) {
            if(possibleValues.containsKey(integer)){
                possibleValues.put(integer,possibleValues.get(integer)+1);
            }
            else
            {
                possibleValues.put(integer,1);
            }
        }
        Iterator<Map.Entry<Integer, Integer>> it = possibleValues.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Integer, Integer> next = it.next();
            if(next.getValue() == 1) it.remove();
        }
        return possibleValues;
    }
}
