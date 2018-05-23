package com.rlc4u.myalterego;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    public DBHandler(Context context) {
        super(context, "testDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE minionTable (colID, minionName, minionLevel, minionType, minionCurrency, minionHealth, minionHunger, minionHappiness, minionStrength, minionLifeSpan, lastOnline, lastFed)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initMinion(Minion minion) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("colID", minion.getId());
        contentValues.put("minionName", minion.getName());
        contentValues.put("minionLevel", minion.getLevel());
        contentValues.put("minionType", minion.getType());
        contentValues.put("minionCurrency", minion.getCurrency());
        contentValues.put("minionHealth", minion.getHealth());
        contentValues.put("minionHunger", minion.getHungerLevel());
        contentValues.put("minionHappiness", minion.getHappinessLevel());
        contentValues.put("minionStrength", minion.getStrengthMeter());
        contentValues.put("minionLifeSpan", minion.getDaysPassed());
        contentValues.put("lastOnline", minion.getLastOnline());
        contentValues.put("lastFed", minion.getLastFed());

        long result = sqLiteDatabase.insert("minionTable", null, contentValues);
        if (result > 0) {
            Log.d("dbhelper", "inserted successfully");
        } else {
            Log.d("dbhelper", "failed to insert");
        }
        sqLiteDatabase.close();
    }

    public boolean retrieveMinion(Minion minion) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] args = {Integer.toString(1)};
        Cursor cursor = sqLiteDatabase.query("minionTable", null, "colID = ?", args, null, null, null);
        Log.d("Success", "Trying to get data");

        boolean existingResult = false;

        while (cursor.moveToNext()) {
            //String minionName = cursor.getString(cursor.getColumnIndexOrThrow("minionName"));
            //long minionHealth = cursor.getLong(cursor.getColumnIndexOrThrow("minionHealth"));
            minion.setId("1");
            minion.setName(cursor.getString(cursor.getColumnIndexOrThrow("minionName")));
            minion.setLevel((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionLevel")));
            minion.setType((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionType")));
            minion.setCurrency((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionCurrency")));
            minion.setHealth((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionHealth")));
            minion.setHungerLevel((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionHunger")));
            minion.setHappinessLevel((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionHappiness")));
            minion.setStrengthMeter((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionStrength")));
            minion.setDaysPassed((int)cursor.getLong(cursor.getColumnIndexOrThrow("minionLifeSpan")));
            minion.setLastOnline(cursor.getLong(cursor.getColumnIndexOrThrow("lastOnline")));
            minion.setLastFed(cursor.getLong(cursor.getColumnIndexOrThrow("lastFed")));
            existingResult = true;

            Log.d("Success", "data retrieved");
        }
        cursor.close();
        return existingResult;
    }

    public void updateMinion(String colName, int newValue) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(colName, newValue);

        String[] args = {Integer.toString(1)};
        int count = sqLiteDatabase.update("minionTable", values, "colID LIKE ?", args);
        Log.d("Success", "Updated Sucessfully, rows affected = " + Integer.toString(count));

    }

    public void resetMinion() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put("minionHealth", 100);
        values.put("minionHunger", 100);
        values.put("minionHappiness", 100);
        values.put("minionStrength", 10);
        values.put("minionLevel", 1);
        values.put("minionCurrency", 100);
        values.put("minionHunger", 100);
        values.put("lastOnline", System.currentTimeMillis());
        values.put("lastFed", System.currentTimeMillis());
        String[] args = {Integer.toString(1)};
        int count = sqLiteDatabase.update("minionTable", values, "colID LIKE ?", args);
        Log.d("Success", "Updated Sucessfully, rows affected = " + Integer.toString(count));
    }

    public void updateTime(String colName, long newValue) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(colName, newValue);

        String[] args = {Integer.toString(1)};
        int count = sqLiteDatabase.update("minionTable", values, "colID LIKE ?", args);
        Log.d("Success", "Updated Sucessfully, rows affected = " + Integer.toString(count));

    }

}


