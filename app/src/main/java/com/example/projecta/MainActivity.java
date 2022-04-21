package com.example.projecta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    // Entry point for our Application
    public static Point size;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay(); //Get Dimensions
        size = new Point();
        display.getSize(size);
        context = this;
        setContentView(new Game(this)); //Enter the game Class :)
    }
}