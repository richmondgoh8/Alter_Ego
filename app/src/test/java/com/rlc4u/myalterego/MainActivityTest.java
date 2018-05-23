package com.rlc4u.myalterego;

import android.content.Context;
import android.support.test.filters.SmallTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class MainActivityTest {
    @Mock
    Context fakeContext;

    @Mock
    DBHandler databaseMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void databaseOperations() {

        String myName = "Boo";

        Minion newMinion = new Minion("1", myName, 1, 0, 100, 100, 100, 0, 1, 0, 0, 0);
        assertEquals(newMinion.getCurrency(), 0);

        databaseMock.initMinion(newMinion);
        databaseMock.retrieveMinion(newMinion);

        assertEquals(100, newMinion.getHungerLevel());
        assertEquals(myName, newMinion.getName());

        databaseMock.updateMinion("minionHealth", 43);
        databaseMock.retrieveMinion(newMinion);

        Minion myMinion = mock(Minion.class);
        when(myMinion.getHealth()).thenReturn(43);
        assertEquals(43, myMinion.getHealth());
    }
}