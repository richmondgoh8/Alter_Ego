package com.rlc4u.myalterego;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText nameText;
    private boolean minionInit;
    private Minion myMinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        nameText = findViewById(R.id.nameText);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        DBHandler db = new DBHandler(this);
        myMinion = new Minion("1", "NA", 1, 0, 100, 100, 100, 0, 1, 0, 0, 0);
        minionInit = db.retrieveMinion(myMinion);
        if (minionInit) {
            nameText.setTextIsSelectable(false);
            nameText.setEnabled(false);
            nameText.setText(myMinion.getName());
            Log.d("Success", "My Current Minion Name : " + myMinion.getName());
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("Destroy", "activity gone");
        startService(new Intent(this, NotificationService.class));
    }

    public void save(View v) {
        String aName = nameText.getText().toString();
        DBHandler db = new DBHandler(this);

        if (!minionInit) {
            Minion newMinion = new Minion("1", aName, 1, 100, 50, 88, 33, 0, 1, 0, System.currentTimeMillis(), System.currentTimeMillis());
            db.initMinion(newMinion);
            Log.d("Success", "Minion Name : " + newMinion.getName());
        } else {
            Log.d("Success", "Minion Name : " + myMinion.getName());
        }

        startActivity(new Intent(MainActivity.this, GameScene.class));
    }
}

// db.updateMinion("minionHealth", 75); update
// db.retrieveMinion(); read
// db.initMinion(new Minion("1", aName, 1,0,100,100,100,0,1,0)); create