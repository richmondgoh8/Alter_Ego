package com.rlc4u.myalterego;

import java.util.Date;

public class Minion {
    private String id;
    private String name;
    private int type;
    private int currency;
    private int health;
    private int hungerLevel;
    private int happinessLevel;
    private int daysPassed;
    private int level;
    private int strengthMeter;
    private long lastOnline;
    private long lastFed;

    public Minion() {
        id = "1";
        name = "N.A";
        type = 1;
        currency = 100;
        health = 50;
        hungerLevel = 88;
        happinessLevel = 33;
        daysPassed = 0;
        level = 1;
        strengthMeter = 0;
        lastOnline = 0L;
        lastFed = 0L;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public long getLastFed() {
        return lastFed;
    }

    public void setLastFed(long lastFed) {
        this.lastFed = lastFed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHungerLevel() {
        return hungerLevel;
    }

    public void setHungerLevel(int hungerLevel) {
        this.hungerLevel = hungerLevel;
    }

    public int getHappinessLevel() {
        return happinessLevel;
    }

    public void setHappinessLevel(int happinessLevel) {
        this.happinessLevel = happinessLevel;
    }

    public int getDaysPassed() {
        return daysPassed;
    }

    public void setDaysPassed(int daysPassed) {
        this.daysPassed = daysPassed;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStrengthMeter() {
        return strengthMeter;
    }

    public void setStrengthMeter(int strengthMeter) {
        this.strengthMeter = strengthMeter;
    }


}
