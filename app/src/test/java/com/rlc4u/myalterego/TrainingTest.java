package com.rlc4u.myalterego;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingTest {

    boolean deviceIsFlat = false;
    boolean deviceIsNear = false;
    int pushCounter = 0;

    @Before
    public void initTests() {
        deviceIsFlat = true;
        deviceIsNear = true;
        pushCounter = 5;
    }

    @Test
    public void updateStrength() {
        Minion myMinion = new Minion();
        assertEquals(0, myMinion.getStrengthMeter());
        if (deviceIsFlat && deviceIsNear) {
            if (pushCounter >= 5) {
                myMinion.setStrengthMeter(1);
            }
        }
        assertEquals(1, myMinion.getStrengthMeter());
    }
}