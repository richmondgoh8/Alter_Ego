package com.rlc4u.myalterego;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainActivityTest {

    @Mock
    DBHandler databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void save() {
        String myName = "Boo";

        Minion newMinion = new Minion();
        assertEquals(100, newMinion.getCurrency());

        databaseMock.initMinion(newMinion);
        databaseMock.retrieveMinion(newMinion);

        // create
        assertEquals(88, newMinion.getHungerLevel());

        // update
        databaseMock.updateMinion("minionHealth", 50);
        databaseMock.retrieveMinion(newMinion);
        Minion myMinion = mock(Minion.class);
        when(myMinion.getHealth()).thenReturn(43);
        assertEquals(43, myMinion.getHealth());

        // read
        newMinion.setName(myName);
        assertEquals(myName, newMinion.getName());
    }
}