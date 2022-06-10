package com.example.projecta;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_layout);

        Intent intentLevel = new Intent(MainMenu.this, GameActivity.class);

        Bundle b = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();

        findViewById(R.id.endless).setOnClickListener(view -> {
            intentLevel.putExtra("levelChosen", 99);
            startActivity(intentLevel);
        });

        findViewById(R.id.levelmenu).setOnClickListener(view -> startActivity(new Intent(MainMenu.this, LevelMenuActivity.class), b));

        findViewById(R.id.tutorial).setOnClickListener(view -> startActivity(new Intent(MainMenu.this, TutorialActivity.class), b));

        findViewById(R.id.exit).setOnClickListener(view -> exitApplicationDialog());


        LinearLayout mainLayout = findViewById(R.id.menuLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }

    @Override
    public void onBackPressed(){
        exitApplicationDialog();
    }

    public void exitApplicationDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        (dialog, id) -> {
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        })

                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
