package com.ankushyerawar.darkmode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.tombayley.activitycircularreveal.CircularReveal;

public class MainActivity extends AppCompatActivity {

    /* Layout References*/
    ConstraintLayout mMainLayout;
    LottieAnimationView toggle;

    /* Circular Reveal library*/
    private CircularReveal mActivityCircularReveal;

    /*View object needed for Circular Reveal*/
    View rootView;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*When Activity is Recreated Set the Theme first and then setContentView()*/
        setTheme(isDarkMode() ? R.style.AppThemeDark : R.style.AppThemeLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Find the MainLayout and toggle lottie*/
        mMainLayout = findViewById(R.id.layout_main);
        toggle = findViewById(R.id.lav_toggle);

        /*here it is DarkMode set the toggle to 1f*/
        if (isDarkMode()) {
            toggle.setMinAndMaxProgress(0.5f, 1f);
        }

        handler = new Handler();

        /*Set On Click listener To lottieView*/
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDarkMode()) {
                    saveTheme(false);
                } else {
                    saveTheme(true);
                }
                changeState();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeTheme();
                    }
                }, 300);
            }
        });

        rootView = mMainLayout.getRootView();
        mActivityCircularReveal = new CircularReveal(rootView);
        mActivityCircularReveal.onActivityCreate(getIntent());

    }

    /*Create SharedPreferences to save values*/
    public SharedPreferences getPreferences() {
        return this.getSharedPreferences("DARK_MODE", Context.MODE_PRIVATE);
    }

    /*save the current theme in Pref*/
    private void saveTheme(boolean isDarkMode) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean("THEME", isDarkMode);
        editor.apply();
    }

    /*save toggle flag*/
    private void saveFlag(int flag) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putInt("FLAG", flag);
        editor.apply();
    }

    /*get set Flag for lottie toggle*/
    private int getFlag() {
        return getPreferences().getInt("FLAG", 0);
    }

    /*check if it is DarkMode*/
    private boolean isDarkMode() {
        return getPreferences().getBoolean("THEME", false);
    }

    /*Change theme Code when Clicked on toggle*/
    private void changeTheme() {

        //Code to finish the last Active Activity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 800);
        CircularReveal.presentActivity(new CircularReveal.Builder(
                this,
                toggle,
                new Intent(this, MainActivity.class),
                800
        ));
    }

    /*Change the state of toggle when pressed*/
    private void changeState() {
        if (getFlag() == 0) {
            toggle.setMinAndMaxProgress(0f, 0.43f); //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
            toggle.playAnimation();
            saveFlag(1);
            //---- Your code here------
        } else {
            toggle.setMinAndMaxProgress(0.5f, 1f);
            toggle.playAnimation();
            saveFlag(0);
            //---- Your code here------
        }
    }
}
