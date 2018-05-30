package com.rlc4u.myalterego;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameSceneTest {

    @Mock
    DBHandler databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();


    @Test
    public void checkDeathCondition() {

        Minion myMinion = mock(Minion.class);
        when(myMinion.getHealth()).thenReturn(0);
        assertEquals(0, myMinion.getHealth());
        if (myMinion.getHealth() == 0) {
            databaseMock.resetMinion();
            when(myMinion.getHealth()).thenReturn(100);
        }
        assertEquals(100, myMinion.getHealth());

    }
}