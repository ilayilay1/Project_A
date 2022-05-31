package com.example.projecta;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class GameActivity extends AppCompatActivity {

    public static Point size;
    public static Context context;
    public Game game;
    public ImageButton pauseButton;
    public int levelNumber;
    public boolean isGameRunning;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lifecycle", "onCreate invoked");

        Display display = getWindowManager().getDefaultDisplay(); //Get Dimensions
        size = new Point();
        display.getSize(size);
        context = this;
        isGameRunning = true;

        Intent intent = this.getIntent();
        levelNumber = intent.getIntExtra("levelChosen", 0);
        Log.e("TAG", "" + levelNumber);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_layout);

        pauseButton = findViewById(R.id.button);
        pauseButton.setOnClickListener(view -> {
            game.pause();
            openPauseDialog();
        });

        game = findViewById(R.id.gameID);

        dialog = new Dialog(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new ScreenReceiver(), filter);
    }

    public void openGameOverDialog(){
        game.isDialogRunning = true;
        dialog.setContentView(R.layout.gameover_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button btnAgain = dialog.findViewById(R.id.buttonAgain);
        Button btnLeave = dialog.findViewById(R.id.buttonLeave);

        btnAgain.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        });

        btnLeave.setOnClickListener(view -> {
            dialog.dismiss();
            stopService();
            isGameRunning = false;
            finish();
            Log.e("TAG", "Activity should be removed");
            startActivity(new Intent(GameActivity.this, MainMenu.class));
        });

        dialog.show();
    }

    public void openPauseDialog(){
        game.isDialogRunning = true;
        dialog.setContentView(R.layout.pause_layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button btnResume = dialog.findViewById(R.id.buttonResume);
        Button btnExit = dialog.findViewById(R.id.buttonExit);

        btnResume.setOnClickListener(view -> {
            game.isDialogRunning = false;
            game.resume();
            game.resumeObjects();
            dialog.dismiss();
            ScreenReceiver.runOnce = true;
        });
        btnExit.setOnClickListener(view -> {
            dialog.dismiss();
            game.pause();
            stopService();
            isGameRunning = false;
            finish();
            startActivity(new Intent(GameActivity.this, MainMenu.class),
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        });

        dialog.show();
    }

    public void startService(){
        if(isGameRunning){
            Intent serviceIntent = new Intent(this, ScreenService.class);
            serviceIntent.putExtra("inputExtra", String.valueOf((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp/1000));
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public void stopService(){
        Intent serviceIntent = new Intent(this, ScreenService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService();
        Log.d("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        if(!game.isDead){
            game.pause(); // Pauses the game
            openPauseDialog();
            startService();
        }
        super.onPause();
        Log.d("lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");

    }

    @Override
    public void onBackPressed() {
        // Comment out super to prevent any back press action
        // super.onBackPressed();
    }
}
