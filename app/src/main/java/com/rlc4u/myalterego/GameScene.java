package com.rlc4u.myalterego;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GameScene extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable anim;
    DBHandler db = new DBHandler(this);
    private Minion myMinion;

    private TextView currencyText;
    private TextView hungerText;
    private TextView strengthText;
    private TextView healthText;
    private TextView daysText;
    private TextView happinessText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

    }

    @Override
    protected void onStart() {
        super.onStart();

        currencyText = findViewById(R.id.currencyText);
        hungerText = findViewById(R.id.hungerText);
        strengthText = findViewById(R.id.strengthText);
        healthText = findViewById(R.id.healthText);
        daysText = findViewById(R.id.daysText);
        happinessText = findViewById(R.id.happinessText);

        //Log.d("Success", "Currently at game scene : ");
        imageView = findViewById(R.id.imageView);
        if (imageView == null) {
            throw new AssertionError();
        }
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        myMinion = new Minion("1", "NA", 1, 0, 100, 100, 100, 0, 1, 0);
        db.retrieveMinion(myMinion);
        Log.d("My name", myMinion.getName());
        currencyText.setText("currency: " + String.valueOf(myMinion.getCurrency()));
        hungerText.setText("hunger: " + String.valueOf(myMinion.getHungerLevel()));
        strengthText.setText("strength: " + String.valueOf(myMinion.getStrengthMeter()));
        healthText.setText("health: " + String.valueOf(myMinion.getHealth()));
        daysText.setText("days: " + String.valueOf(myMinion.getDaysPassed()));
        happinessText.setText("happiness" + String.valueOf(myMinion.getHappinessLevel()));

        //imageView.setVisibility(View.INVISIBLE);
        anim = (AnimationDrawable)imageView.getBackground();
        anim.start();

    }



    public void idle() {
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        anim = (AnimationDrawable)imageView.getBackground();
        anim.start();
    }

    public void care(View v) {
        if (myMinion.getHappinessLevel() >= 100) {
            Toast.makeText(getApplicationContext(), "Minion is happy!", Toast.LENGTH_SHORT).show();
        } else {
            increaseHappiness();
            Log.d("Success", "Caring");
            anim.stop();
            imageView.setBackgroundResource(R.drawable.avatar_jumping);
            anim = (AnimationDrawable)imageView.getBackground();
            anim.start();
            checkIfAnimationDone(anim);
        }
    }

    public void train(View v) {
        Log.d("Success", "Training");
        startActivity(new Intent(GameScene.this, Training.class));

    }

    public void feed(View v) {
        if (myMinion.getHungerLevel() >= 100) {
            Toast.makeText(getApplicationContext(), "Minion is full!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (myMinion.getCurrency() >= 1) {
            deductFunds();
            increaseHunger();
            Log.d("Success", "Feeding");
            anim.stop();
            imageView.setBackgroundResource(R.drawable.avatar_jumping);
            anim = (AnimationDrawable)imageView.getBackground();
            anim.start();
            checkIfAnimationDone(anim);
        } else {
            Toast.makeText(getApplicationContext(), "Not enough money!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deductFunds() {
        db.updateMinion("minionCurrency", myMinion.getCurrency()-1);
        db.retrieveMinion(myMinion);
        currencyText.setText("Hello" + String.valueOf(myMinion.getCurrency()));
    }

    public void increaseHunger() {
        db.updateMinion("minionHunger", myMinion.getHungerLevel() + 1);
        db.retrieveMinion(myMinion);
        hungerText.setText("Hunger: " + String.valueOf(myMinion.getHungerLevel()));
    }

    public void increaseHappiness() {
        db.updateMinion("minionHappiness", myMinion.getHappinessLevel() + 1);
        db.retrieveMinion(myMinion);
        happinessText.setText("Happiness: " + String.valueOf(myMinion.getHappinessLevel()));
    }

    private void checkIfAnimationDone(AnimationDrawable anim){
        final AnimationDrawable a = anim;
        int timeBetweenChecks = 100;
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            public void run(){
                if (a.getCurrent() != a.getFrame(a.getNumberOfFrames() - 1)){
                    checkIfAnimationDone(a);
                } else{
                    Toast.makeText(getApplicationContext(), "ANIMATION DONE!", Toast.LENGTH_SHORT).show();
                    idle();
                }
            }
        }, timeBetweenChecks);
    }
}
