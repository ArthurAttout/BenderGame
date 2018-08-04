package be.henallux.masi.bendergame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.henallux.masi.bendergame.utils.DuplicateChecker;

import static org.junit.Assert.assertEquals;

/**
 * Created by Le Roi Arthur on 01-08-18.
 */

public class DuplicateUnitTest {
    @Test
    public void noDuplicates() throws Exception {
        HashMap<Integer,Integer> result = DuplicateChecker.hasDuplicates(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(4);
        }});
        assertEquals("1 2 3 4 contains no duplicates",0,result.entrySet().size());
    }

    @Test
    public void oneDuplicate() throws Exception {
        HashMap<Integer,Integer> result = DuplicateChecker.hasDuplicates(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(1);
        }});
        assertEquals("1 2 3 1 contains 1 duplicate",1,result.entrySet().size());
    }

    @Test
    public void twoDuplicates() throws Exception {
        HashMap<Integer,Integer> result = DuplicateChecker.hasDuplicates(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(2);
            add(1);
        }});
        assertEquals("1 2 2 1 contains 2 duplicates",2,result.entrySet().size());
    }

    @Test
    public void quadruple() throws Exception {
        HashMap<Integer,Integer> result = DuplicateChecker.hasDuplicates(new ArrayList<Integer>(){{
            add(3);
            add(3);
            add(3);
            add(3);
        }});
        Map.Entry<Integer, Integer> next = result.entrySet().iterator().next();
        Integer value = next.getValue();
        assertEquals("3 3 3 3 has one duplicate of size 4",4,value.intValue());
    }

    @Test
    public void triple() throws Exception {
        HashMap<Integer,Integer> result = DuplicateChecker.hasDuplicates(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(1);
            add(1);
        }});
        Map.Entry<Integer, Integer> next = result.entrySet().iterator().next();
        Integer value = next.getValue();
        assertEquals("1 2 1 1 has one duplicate of size 3",3,value.intValue());
    }
}
