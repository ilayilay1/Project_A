package com.example.projecta;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    public static Point size;
    public static Context context;
    public Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay(); //Get Dimensions
        size = new Point();
        display.getSize(size);
        context = this;
        //game = findViewById(R.id.gameID);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        game = new Game(this);
        setContentView(game);
        //setContentView(R.layout.game_layout);

        //game = findViewById(R.id.gameID);
        Log.d("lifecycle", "onCreate invoked");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        game.pause(); // Pauses the game
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
