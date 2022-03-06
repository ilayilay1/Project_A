package com.example.projecta;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * Player is the main character of the game, which the user can control with a touch joystick.
 * The player class is an extension of a Circle, which is an extension of a GameObject
 */
public class Player extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;


    public Player(Context context, Joystick joystick, double positionX, double positionY, double radius){
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, radius);
        this.joystick = joystick;

    }


    public void update() {
        //Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;  //Keep in mind height and width are reversed here & try to get this to work

        //Update position
        if(MainActivity.size.x - radius <= positionX + velocityX) //Calculating the X boundaries
            positionX = MainActivity.size.x - radius;
        else if(0 + radius >= positionX + velocityX)
            positionX = 0 + radius;
        else
            positionX += velocityX;

        if(MainActivity.size.y - radius <= positionY + velocityY) //Calculating the Y boundaries
            positionY = MainActivity.size.y - radius;
        else if(0 + radius >= positionY + velocityY)
            positionY = 0 + radius;
        else
            positionY += velocityY;
    }

    public void setPosition(double positionX, double positionY) { //Irrelevant for now, was used for testing, might use in future for dash ability
        this.positionX = positionX;
        this.positionY = positionY;
    }
}
