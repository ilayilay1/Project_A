package com.example.projecta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

/**
 * Rectangle is an abstract class which implements a draw method from GameObject for drawing the object
 * as a rectangle
 */

public abstract class Rectangle extends GameObject {

    protected Paint paint;
    protected float degree;
    protected double width, height;

    public Rectangle(int color, double positionX, double positionY, float degree, double width, double height) {
        super(positionX, positionY);
        this.degree = degree;
        this.width = width;
        this.height = height;

        // Set colors of rectangle
        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate((float)positionX, (float)positionY);
        canvas.rotate(degree);
        canvas.drawRect((float)(-width/2), (float)(-height/2), (float)(width/2), (float)(height/2), paint);
        canvas.restore();
    }
}
