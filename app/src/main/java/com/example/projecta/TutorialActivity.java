package com.example.projecta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class TutorialActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_layout);

        Intent intentBack = new Intent(TutorialActivity.this, MainMenu.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();

        findViewById(R.id.back).setOnClickListener(view -> startActivity(intentBack, b));

        LinearLayout mainLayout = findViewById(R.id.menuLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }
}
