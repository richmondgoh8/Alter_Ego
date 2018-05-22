package com.rlc4u.myalterego;

import android.content.Context;
import android.support.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    @Mock
    Context fakeContext;

    DBHandler db;

    @Test
    public void save() {
        Minion myMinion = new Minion("1", "NA", 1, 0, 100, 100, 100, 0, 1, 0, 0, 0);
        assertEquals(myMinion.getCurrency(), 0);
        //db = new DBHandler(fakeContext);
        //db.initMinion(myMinion);

    }
}