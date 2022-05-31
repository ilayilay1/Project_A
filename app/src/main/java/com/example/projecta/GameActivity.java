package com.example.projecta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class GameActivity extends AppCompatActivity {

    public static Point size;
    public static Context context;
    public Game game;
    public ImageButton pauseButton;
    public int levelNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay(); //Get Dimensions
        size = new Point();
        display.getSize(size);
        context = this;

        Intent intent = this.getIntent();
        levelNumber = intent.getIntExtra("levelChosen", 0);
        Log.e("TAG", "" + levelNumber);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.game_layout);

        pauseButton = findViewById(R.id.button);
        pauseButton.setOnClickListener(view -> {
            game.pause();
        });
        game = findViewById(R.id.gameID);
        Log.d("lifecycle", "onCreate invoked");

    }

    public void startService(){
        Intent serviceIntent = new Intent(this, ScreenService.class);
        serviceIntent.putExtra("inputExtra", String.valueOf((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp/1000));
        ContextCompat.startForegroundService(this, serviceIntent);
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
        game.pause(); // Pauses the game
        startService();
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
