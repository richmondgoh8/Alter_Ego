package com.rlc4u.myalterego;
/*
Reference Code
https://inthecheesefactory.com/blog/how-to-share-access-to-file-with-fileprovider-on-android-nougat/en
https://www.viralandroid.com/2016/04/android-gridview-with-image-and-text.html
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.rlc4u.myalterego.R.id.careBtn;

public class GameScene extends AppCompatActivity {

    ImageView imageView;
    private TextView minionName;
    AnimationDrawable anim;
    DBHandler db = new DBHandler(this);
    private Minion myMinion;
    private LinearLayout rootContent;

    private Button cameraIcon;
    private Button careBtn;
    private Button feedBtn;
    private Button trainBtn;

    GridView androidGridView;
    CustomGridViewActivity adapterViewAndroid;

    String[] gridViewString = {
            "Currency", "Health", "Hunger", "Strength", "Level", "Happiness",
    };

    String[] helperText = {
            "Currency", "Health", "Hunger", "Strength", "Level", "Happiness",
    };

    int[] gridViewImageId = {
            R.drawable.ui_coin, R.drawable.ui_heart, R.drawable.ui_food, R.drawable.ui_bolt, R.drawable.ui_star, R.drawable.ui_plus,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_grid_view);

        careBtn = findViewById(R.id.careBtn);
        feedBtn = findViewById(R.id.feedBtn);
        trainBtn = findViewById(R.id.trainBtn);
        cameraIcon = findViewById(R.id.cameraBtn);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

        minionName = findViewById(R.id.minionName);

        rootContent = findViewById(R.id.android_gridview_example);

        adapterViewAndroid = new CustomGridViewActivity(GameScene.this, gridViewString, gridViewImageId);
        androidGridView = findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                Toast.makeText(GameScene.this, helperText[+i], Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Success", "Currently at game scene : ");
        imageView = findViewById(R.id.charImg);
        if (imageView == null) {
            throw new AssertionError();
        }
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        myMinion = new Minion();
        anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
        updateText();

        manageEvents();
        updateText();
        checkDeathCondition();
        //imageView.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        db.updateTime("lastOnline", System.currentTimeMillis());
        anim.stop();
        Log.d("OnPause", String.valueOf(System.currentTimeMillis()));
        //String.valueOf(System.currentTimeMillis()
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Log.d("Destroy", "activity gone");
        startService(new Intent(this, NotificationService.class));
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
            //Toast.makeText(getApplicationContext(), String.valueOf(timeLastFed) + "Seperator" + String.valueOf(timeNow), Toast.LENGTH_LONG).show();
            db.updateMinion("minionHunger", myMinion.getHungerLevel() - (int) multiplier1);
            db.updateTime("lastFed", System.currentTimeMillis());
        }

        if (timeNow - lastOnline > 10800) {
            multiplier2 = (timeNow - timeLastFed) / 10800;
            db.updateMinion("minionHappiness", myMinion.getHappinessLevel() - (int) multiplier2);
        }

        if (multiplier1 + multiplier2 >= 3) {
            long healthReduction = (multiplier1 + multiplier2) / 3;
            db.updateMinion("minionHealth", myMinion.getHealth() - (int) healthReduction);
        }

        db.updateTime("lastOnline", System.currentTimeMillis());

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
        minionName.setText(getString(R.string.Name, myMinion.getName()));
        gridViewString[0] = String.valueOf(myMinion.getCurrency());
        gridViewString[1] = String.valueOf(myMinion.getHealth());
        gridViewString[2] = String.valueOf(myMinion.getHungerLevel());
        gridViewString[3] = String.valueOf(myMinion.getStrengthMeter());
        gridViewString[4] = String.valueOf(myMinion.getLevel());
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
    }

    public void checkDeathCondition() {
        if (myMinion.getHungerLevel() <= 0) {
            anim.stop();
            imageView.setBackgroundResource(R.drawable.avatar_death);
            anim = (AnimationDrawable) imageView.getBackground();
            anim.start();
            checkIfAnimationDone(anim);
            db.resetMinion();
            db.retrieveMinion(myMinion);
            updateText();
            Toast.makeText(getApplicationContext(), "Your Minion has fallen, resetting parameters", Toast.LENGTH_LONG).show();
        }
    }

    public void idle() {
        imageView.setBackgroundResource(R.drawable.avatar_animation);
        anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
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

    public void share(View v) {
        shareScreenshot();
    }

    /*  Method which will take screenshot on Basis of Screenshot Type ENUM  */
    private File takeScreenshot() {
        careBtn.setVisibility(View.INVISIBLE);
        feedBtn.setVisibility(View.INVISIBLE);
        trainBtn.setVisibility(View.INVISIBLE);
        cameraIcon.setVisibility(View.INVISIBLE);

        Bitmap b = ScreenshotUtils.getScreenShot(rootContent);

        careBtn.setVisibility(View.VISIBLE);
        feedBtn.setVisibility(View.VISIBLE);
        trainBtn.setVisibility(View.VISIBLE);
        cameraIcon.setVisibility(View.VISIBLE);

        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        String timeStamp = new SimpleDateFormat("ddMMyyyy", Locale.US).format(new Date());
        File file = ScreenshotUtils.store(b, timeStamp + "_My_Minion" + ".jpg", storageDir);//save the screenshot to selected path
        return file;


    }

    /*  Share Screenshot  */
    private void shareScreenshot() {
        File photoFile = takeScreenshot();

        Uri photoURI = FileProvider.getUriForFile(GameScene.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = "MY Favourite Minion, Isnt it cool?";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Join My Alter Ego!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, photoURI);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}