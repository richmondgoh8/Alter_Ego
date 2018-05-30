package com.rlc4u.myalterego;

import org.junit.Test;

import static org.junit.Assert.*;

public class MinionTest {

    Minion myMinion = new Minion();

    @Test
    public void getStats() {
        assertEquals("1", myMinion.getId());
        assertEquals("N.A", myMinion.getName());
        assertEquals(1, myMinion.getType());
        assertEquals(100, myMinion.getCurrency());
        assertEquals(50, myMinion.getHealth());
        assertEquals(88, myMinion.getHungerLevel());
        assertEquals(33, myMinion.getHappinessLevel());
        assertEquals(0, myMinion.getDaysPassed());
        assertEquals(1, myMinion.getLevel());
        assertEquals(0, myMinion.getStrengthMeter());
        assertEquals(0L, myMinion.getLastOnline());
        assertEquals(0L, myMinion.getLastFed());
    }

    @Test
    public void updateAllStats() {

        String name = "TestMinion";
        int currency = 44;
        int health = 9;
        int hunger = 33;
        int happiness = 77;
        int daysPassed = 311;
        int level = 15;
        int strength = 12;
        long sampleDate = 1234567L;

        myMinion.setName(name);
        myMinion.setCurrency(currency);
        myMinion.setHealth(health);
        myMinion.setHungerLevel(hunger);
        myMinion.setHappinessLevel(happiness);
        myMinion.setDaysPassed(daysPassed);
        myMinion.setLevel(level);
        myMinion.setStrengthMeter(strength);
        myMinion.setLastOnline(sampleDate);
        myMinion.setLastFed(sampleDate);

        assertEquals(name, myMinion.getName());
        assertEquals(currency, myMinion.getCurrency());
        assertEquals(health, myMinion.getHealth());
        assertEquals(hunger, myMinion.getHungerLevel());
        assertEquals(happiness, myMinion.getHappinessLevel());
        assertEquals(daysPassed, myMinion.getDaysPassed());
        assertEquals(level, myMinion.getLevel());
        assertEquals(strength, myMinion.getStrengthMeter());
        assertEquals(sampleDate, myMinion.getLastOnline());
        assertEquals(sampleDate, myMinion.getLastFed());
    }
}