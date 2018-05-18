package com.rlc4u.myalterego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class GameScene extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        Log.d("Success", "Currently at game scene : ");
    }
}
