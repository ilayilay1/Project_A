package com.example.projecta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LevelMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_manu_layout);

        Intent intentBack = new Intent(LevelMenuActivity.this, MainMenu.class);
        Intent intentLevel = new Intent(LevelMenuActivity.this, GameActivity.class);
        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();

        findViewById(R.id.back).setOnClickListener(view -> startActivity(intentBack, b));

        findViewById(R.id.level1).setOnClickListener(view -> {
            intentLevel.putExtra("levelChosen", 1);
            startActivity(intentLevel);
        });

        findViewById(R.id.level2).setOnClickListener(view -> {
            intentLevel.putExtra("levelChosen", 2);
            //startActivity(intentLevel);
        });

        findViewById(R.id.level3).setOnClickListener(view -> {
            intentLevel.putExtra("levelChosen", 3);
            //startActivity(intentLevel);
        });

        LinearLayout mainLayout = findViewById(R.id.menuLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }
}
