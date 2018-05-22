package com.rlc4u.myalterego;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameScene extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable anim;
    DBHandler db = new DBHandler(this);
    private Minion myMinion;

    GridView androidGridView;
    CustomGridViewActivity adapterViewAndroid;

    String[] gridViewString = {
            "Currency", "Health", "Hunger", "Strength", "Days", "Happiness",
    };

    String[] helperText = {
            "Currency", "Health", "Hunger", "Strength", "Days", "Happiness",
    };

    int[] gridViewImageId = {
            R.drawable.ui_coin, R.drawable.ui_heart, R.drawable.ui_food, R.drawable.ui_bolt, R.drawable.ui_star, R.drawable.ui_plus,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_grid_view);

        adapterViewAndroid = new CustomGridViewActivity(GameScene.this, gridViewString, gridViewImageId);
        androidGridView = findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Toast.makeText(GameScene.this, "GridView Item: " + helperText[+i], Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Log.d("Success", "Currently at game scene : ");
        imageView = findViewById(R.id.charImg);
        if (imageView == null) {
            throw new AssertionError();
        }
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        myMinion = new Minion("1", "NA", 1, 0, 100, 100, 100, 0, 1, 0, 2131,2131);
        updateText();
        manageEvents();
        updateText();

        //imageView.setVisibility(View.INVISIBLE);
        anim = (AnimationDrawable) imageView.getBackground();
        anim.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.updateTime("lastOnline", System.currentTimeMillis());
        Log.d("OnPause", String.valueOf(System.currentTimeMillis()));
        //String.valueOf(System.currentTimeMillis()
    }

    public void manageEvents() {
        db.retrieveMinion(myMinion);
        long timeLastFed = myMinion.getLastFed() / 1000; //in seconds
        long timeNow = System.currentTimeMillis() / 1000;
        long lastOnline = myMinion.getLastOnline() / 1000;

        long multiplier1 = 0;
        long multiplier2 = 0;


        if (timeNow - timeLastFed > 7200) {
            multiplier1 = (timeNow - timeLastFed) / 7200;
            Toast.makeText(getApplicationContext(), String.valueOf(timeLastFed) + "Seperator" +String.valueOf(timeNow), Toast.LENGTH_LONG).show();
            db.updateMinion("minionHunger", myMinion.getHungerLevel() - (int)multiplier1);
            db.updateTime("lastFed", System.currentTimeMillis());
        }

        if (timeNow - lastOnline > 10800) {
            multiplier2 = (timeNow - timeLastFed) / 10800;
            db.updateMinion("minionHappiness", myMinion.getHappinessLevel() - (int)multiplier2);
        }

        if (multiplier1 + multiplier2 >= 3) {
            long healthReduction = (multiplier1+multiplier2)/3;
            db.updateMinion("minionHealth", myMinion.getHealth() - (int)healthReduction);
        }

        db.updateTime("lastOnline", System.currentTimeMillis());

    }

    public void idle() {
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
    }

    public void care(View v) {
        if (myMinion.getHappinessLevel() >= 100) {
            Toast.makeText(getApplicationContext(), "Minion is happy!", Toast.LENGTH_SHORT).show();
        } else {
            increaseHappiness();
            updateText();
            Log.d("Success", "Caring");
            anim.stop();
            imageView.setBackgroundResource(R.drawable.avatar_jumping);
            anim = (AnimationDrawable) imageView.getBackground();
            anim.start();
            checkIfAnimationDone(anim);
        }
    }

    public void train(View v) {
        Log.d("Success", "Training");
        startActivity(new Intent(GameScene.this, Training.class));

    }

    public void feed(View v) {
        if (myMinion.getHungerLevel() >= 100 && myMinion.getHealth() >= 100) {
            Toast.makeText(getApplicationContext(), "Minion is happy & healthy", Toast.LENGTH_SHORT).show();
            return;
        }

        if (myMinion.getCurrency() >= 1) {
            deductFunds();
            if (myMinion.getHungerLevel() >= 100) {
                db.updateMinion("minionHealth", myMinion.getHealth() + 1);
            } else {
                increaseHunger();
            }
            updateText();
            Log.d("Success", "Feeding");
            anim.stop();
            imageView.setBackgroundResource(R.drawable.avatar_jumping);
            anim = (AnimationDrawable) imageView.getBackground();
            anim.start();
            checkIfAnimationDone(anim);
        } else {
            Toast.makeText(getApplicationContext(), "Not enough money!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateText() {
        db.retrieveMinion(myMinion);
        gridViewString[0] = String.valueOf(myMinion.getCurrency());
        gridViewString[1] = String.valueOf(myMinion.getHealth());
        gridViewString[2] = String.valueOf(myMinion.getHungerLevel());
        gridViewString[3] = String.valueOf(myMinion.getStrengthMeter());
        gridViewString[4] = String.valueOf(myMinion.getDaysPassed());
        gridViewString[5] = String.valueOf(myMinion.getHappinessLevel());
        androidGridView.setAdapter(adapterViewAndroid);
    }

    public void deductFunds() {
        db.updateMinion("minionCurrency", myMinion.getCurrency() - 1);
    }

    public void increaseHunger() {
        db.updateMinion("minionHunger", myMinion.getHungerLevel() + 1);
        db.updateMinion("minionHealth", myMinion.getHealth() + 1);
        db.updateTime("lastFed", System.currentTimeMillis());
    }

    public void increaseHappiness() {
        db.updateMinion("minionHappiness", myMinion.getHappinessLevel() + 1);
        //happinessText.setText("Happiness: " + String.valueOf(myMinion.getHappinessLevel()));
    }

    private void checkIfAnimationDone(AnimationDrawable anim) {
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 100;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)) {
                    checkIfAnimationDone(a);
                } else {
                    //Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
                    idle();
                }
            }
        }, timeBetweenChecks);
    }
}
