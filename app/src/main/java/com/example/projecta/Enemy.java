package com.example.projecta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

public class Enemy extends Rectangle {

    public Enemy(double positionX, double positionY, float degree, double width, double height, int color){
        super(color, positionX, positionY, degree, width, height);
    }

    public void update() {
    }
}
