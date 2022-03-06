package com.example.projecta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.content.ContextCompat;

public class Enemy extends Rectangle {

    public Enemy(Context context, double positionX, double positionY, float degree, int offset){
        super(context, ContextCompat.getColor(context, R.color.enemy), positionX, positionY, degree, offset);
    }

    public void update() {
    }
}
