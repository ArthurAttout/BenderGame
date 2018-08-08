package be.henallux.masi.bendergame;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.henallux.masi.bendergame.model.conditions.Condition;
import be.henallux.masi.bendergame.model.conditions.ConditionBigFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionContains;
import be.henallux.masi.bendergame.model.conditions.ConditionQuadruple;
import be.henallux.masi.bendergame.model.conditions.ConditionSmallFlush;
import be.henallux.masi.bendergame.model.conditions.ConditionTriple;
import be.henallux.masi.bendergame.utils.DuplicateChecker;

import static org.junit.Assert.assertEquals;

/**
 * Created by Le Roi Arthur on 01-08-18.
 */

public class ConditionsUnitTest {

    @Test
    public void quadrupleFive() throws Exception {
        Condition c = new ConditionQuadruple(5);
        assertEquals("5 5 5 5 satisfies quadruple five",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(5);
            add(5);
            add(5);
            add(5);
        }}));
    }

    @Test
    public void quadrupleAny() throws Exception {
        Condition c = new ConditionQuadruple(0);
        assertEquals("3 3 3 3 satisfies quadruple any",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(3);
            add(3);
            add(3);
            add(3);
        }}));
    }

    @Test
    public void tripleFive() throws Exception {
        Condition c = new ConditionTriple(5);
        assertEquals("5 6 5 5 satisfies triple five",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(5);
            add(6);
            add(5);
            add(5);
        }}));
    }

    @Test
    public void tripleAny() throws Exception {
        Condition c = new ConditionTriple(0);
        assertEquals("2 3 3 3 satisfies triple any",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(2);
            add(3);
            add(3);
            add(3);
        }}));
    }

    @Test
    public void notTripleAny() throws Exception {
        Condition c = new ConditionTriple(0);
        assertEquals("2 3 2 3 does not satisfy triple any",false,c.isSatisfied(new ArrayList<Integer>(){{
            add(2);
            add(3);
            add(2);
            add(3);
        }}));
    }

    @Test
    public void contains() throws Exception {
        Condition c = new ConditionContains(new ArrayList<Integer>(){{add(5);add(6);}});
        assertEquals("6 5 1 4 satisfies contains 5 6",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(6);
            add(5);
            add(1);
            add(4);
        }}));
    }

    @Test
    public void hasFlush() throws Exception {
        Condition c = new ConditionSmallFlush(0);
        assertEquals("1 2 3 5 satisfies small flush",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(3);
            add(2);
        }}));
    }

    @Test
    public void hasNoFlush() throws Exception {
        Condition c = new ConditionSmallFlush(0);
        assertEquals("1 2 2 2 does not satisfy small flush",false,c.isSatisfied(new ArrayList<Integer>(){{
            add(1);
            add(2);
            add(2);
            add(2);
        }}));
    }

    @Test
    public void hasFlushStartsWith2() throws Exception {
        Condition c = new ConditionSmallFlush(2);
        assertEquals("6 2 3 4 satisfies small flush 2",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(6);
            add(2);
            add(3);
            add(4);
        }}));
    }

    @Test
    public void hasFlushStartsWith3() throws Exception {
        Condition c = new ConditionSmallFlush(3);
        assertEquals("1 5 3 4 satisfies small flush 3",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(1);
            add(5);
            add(3);
            add(4);
        }}));
    }

    @Test
    public void hasOtherFlush() throws Exception {
        Condition c = new ConditionSmallFlush(0);
        assertEquals("6 5 4 3 does not satisfy small flush",false,c.isSatisfied(new ArrayList<Integer>(){{
            add(6);
            add(5);
            add(4);
            add(3);
        }}));
    }

    @Test
    public void hasBigFlush() throws Exception {
        Condition c = new ConditionBigFlush(0);
        assertEquals("1 2 3 4 satisfies big flush",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(1);
            add(3);
            add(2);
            add(4);
        }}));
    }

    @Test
    public void hasNoBigFlush() throws Exception {
        Condition c = new ConditionBigFlush(0);
        assertEquals("1 5 4 3 does not satisfy big flush",false,c.isSatisfied(new ArrayList<Integer>(){{
            add(1);
            add(5);
            add(4);
            add(3);
        }}));
    }

    @Test
    public void hasBigFlush2() throws Exception {
        Condition c = new ConditionBigFlush(2);
        assertEquals("2 5 4 3 satisfies big flush 2",true,c.isSatisfied(new ArrayList<Integer>(){{
            add(2);
            add(5);
            add(4);
            add(3);
        }}));
    }
}
