package com.example.projecta;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

public class ArrowHead extends GameObject{

    protected int staticDirection, direction = 0; // Down - 1, Up - 2, Right - 3, Left - 4,
    protected Paint paint;
    private long timeOfCreation;
    protected double timeInMs;

    public ArrowHead(double positionX, double positionY, int color, int direction, double timeInMs) {
        super(positionX, positionY);

        this.direction = direction;
        this.timeInMs = timeInMs;

        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(0);
        staticDirection = direction;
        timeOfCreation = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp;
    }

    @Override
    public void draw(Canvas canvas) {
        switch(direction){
        case 1:{
            canvas.drawRect((float)positionX+25, (float)positionY, (float)positionX + 75, (float)positionY + 100, paint);
            Path path = new Path();
            path.lineTo((float)positionX, (float)positionY + 100);
            path.lineTo((float)positionX + 50, (float)positionY + 150);
            path.lineTo((float)positionX + 100, (float)positionY + 100);
            path.lineTo((float)positionX, (float)positionY + 100);
            path.close();

            canvas.drawPath(path, paint);
            break;}
        case 2:{
            canvas.drawRect((float)positionX+25, (float)positionY, (float)positionX + 75, (float)positionY - 100, paint);
            Path path = new Path();
            path.lineTo((float)positionX, (float)positionY - 100);
            path.lineTo((float)positionX + 50, (float)positionY - 150);
            path.lineTo((float)positionX + 100, (float)positionY - 100);
            path.lineTo((float)positionX, (float)positionY - 100);
            path.close();

            canvas.drawPath(path, paint);
            break;}
        case 3:{
            canvas.drawRect((float)positionX, (float)positionY - 25, (float)positionX + 100, (float)positionY + 25, paint);
            Path path = new Path();
            path.lineTo((float)positionX + 100, (float)positionY - 50);
            path.lineTo((float)positionX + 150, (float)positionY);
            path.lineTo((float)positionX + 100, (float)positionY + 50);
            path.lineTo((float)positionX + 100, (float)positionY - 50);
            path.close();

            canvas.drawPath(path, paint);
            break;}
        case 4:{
            canvas.drawRect((float)positionX - 100, (float)positionY - 25, (float)positionX, (float)positionY + 25, paint);
            Path path = new Path();
            path.lineTo((float)positionX - 100, (float)positionY - 50);
            path.lineTo((float)positionX - 150, (float)positionY);
            path.lineTo((float)positionX - 100, (float)positionY + 50);
            path.lineTo((float)positionX - 100, (float)positionY - 50);
            path.close();

            canvas.drawPath(path, paint);
            break;}
        }
    }

    @Override
    public void update() {

        switch(direction){
            case 1:
                positionX = GameActivity.size.x/2;
                positionY = 20;
                paint.setAlpha(255);
                break;
            case 2:
                positionX = GameActivity.size.x/2;
                positionY = GameActivity.size.y - 20;
                paint.setAlpha(255);
                break;
            case 3:
                positionX = 20;
                positionY = GameActivity.size.y/2;
                paint.setAlpha(255);
                break;
            case 4:
                positionX = GameActivity.size.x - 20;
                positionY = GameActivity.size.y/2;
                paint.setAlpha(255);
                break;
            default:
                paint.setAlpha(0);
                break;
        }
        double deltaT = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp - timeOfCreation;
        if (deltaT > timeInMs) { // Removes arrow after the enemy finally spawns
            Game.arrowHeadToDelete.add(this);
            return;
        }
        if (deltaT % 300 < 150)
            direction = 0;
        else
            direction = staticDirection;

    }
}
