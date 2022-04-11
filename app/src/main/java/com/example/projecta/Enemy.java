package com.example.projecta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

/**
 * Enemy is the obstacle in the game, which the user needs to avoid at all costs.
 * The enemy class is an extension of a Rectangle, which is an extension of a GameObject
 */

public class Enemy extends Rectangle {

    public Enemy(double positionX, double positionY, float degree, double width, double height, int color){
        super(color, positionX, positionY, degree, width, height);
    }

    public void update() {
    }
}
