package com.example.projecta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Rectangle extends GameObject {

    protected Paint paint;
    protected float degree;
    protected int offset;

    public Rectangle(Context context, int color, double positionX, double positionY, float degree, int offset) {
        super(positionX, positionY);
        this.degree = degree;
        this.offset = offset;

        // Set colors of rectangle
        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(degree); //insert random angle
        canvas.drawRect(0, offset, 3000, offset+65, paint); //switch 20's later with random height
        canvas.restore();
    }
}
