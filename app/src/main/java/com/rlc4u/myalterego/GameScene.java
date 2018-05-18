package com.rlc4u.myalterego;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

public class GameScene extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable anim;
    private Minion myMinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        //Log.d("Success", "Currently at game scene : ");
        imageView = findViewById(R.id.imageView);
        if (imageView == null) {
            throw new AssertionError();
        }
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        DBHandler db = new DBHandler(this);
        myMinion = new Minion("1", "NA", 1, 0, 100, 100, 100, 0, 1, 0);
        db.retrieveMinion(myMinion);
        Log.d("My name", myMinion.getName());

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
        Log.d("Success", "Caring");
        anim.stop();
        imageView.setBackgroundResource(R.drawable.avatar_jumping);
        anim = (AnimationDrawable)imageView.getBackground();
        anim.start();
        checkIfAnimationDone(anim);
    }

    public void train(View v) {
        Log.d("Success", "Training");

    }

    public void feed(View v) {
        Log.d("Success", "Feeding");
        anim.stop();
        imageView.setBackgroundResource(R.drawable.avatar_jumping);
        anim = (AnimationDrawable)imageView.getBackground();
        anim.start();
        checkIfAnimationDone(anim);
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
