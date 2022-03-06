package com.example.projecta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    // Entry point for our Application
    public static Point size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay(); //Get Dimensions
        size = new Point();
        display.getSize(size);

        setContentView(new Game(this)); //Enter the game Class :)
    }
}