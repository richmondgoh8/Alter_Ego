package com.rlc4u.myalterego;

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

    public Minion(String id, String name, int type, int currency, int health, int hungerLevel, int happinessLevel, int daysPassed, int level, int strengthMeter) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.currency = currency;
        this.health = health;
        this.hungerLevel = hungerLevel;
        this.happinessLevel = happinessLevel;
        this.daysPassed = daysPassed;
        this.level = level;
        this.strengthMeter = strengthMeter;
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
