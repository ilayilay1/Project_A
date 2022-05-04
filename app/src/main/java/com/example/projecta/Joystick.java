package com.example.projecta;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Joystick is a tool the user interacts with when wanting to control the Player object
 */

public class Joystick { // BIG NOTE, might want to make the Joystick able to be used anywhere within the screen, keep for later

    private Paint innerCirclePaint, outerCirclePaint;

    private int innerCircleRadius, outerCircleRadius, outerCircleCenterPositionX, outerCircleCenterPositionY,
            innerCircleCenterPositionX, innerCircleCenterPositionY;

    private double joystickCenterToTouchDistance, actuatorX, actuatorY;

    private boolean isPressed, isVisible = false;


    public Joystick(int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius){
        //  Outer and inner circles to make the joystick
        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;
        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;

        // Radius of circles
        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        //  Paint of circles
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.WHITE);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        outerCirclePaint.setAlpha(100); // Lowers the opacity of the outer joystick

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.WHITE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void draw(Canvas canvas) {
        if(isVisible){
            canvas.drawCircle( // Outer circle
                    outerCircleCenterPositionX,
                    outerCircleCenterPositionY,
                    outerCircleRadius,
                    outerCirclePaint);

            canvas.drawCircle( // Inner circle
                    innerCircleCenterPositionX,
                    innerCircleCenterPositionY,
                    innerCircleRadius,
                    innerCirclePaint);
        }
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPositionX = (int)(outerCircleCenterPositionX + actuatorX*outerCircleRadius);
        innerCircleCenterPositionY = (int)(outerCircleCenterPositionY + actuatorY*outerCircleRadius);
    }

    public boolean isPressed(double touchPositionX, double touchPositionY) {
        joystickCenterToTouchDistance = Math.sqrt(
                Math.pow(outerCircleCenterPositionX - touchPositionX, 2) +
                Math.pow(outerCircleCenterPositionY - touchPositionY, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double touchPositionX, double touchPositionY) {
        double deltaX = touchPositionX - outerCircleCenterPositionX;
        double deltaY = touchPositionY - outerCircleCenterPositionY;
        double deltaDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        if(deltaDistance < outerCircleRadius){
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        } else{
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }

    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
    }

    public void setLocation(int centerPositionX, int centerPositionY){
        outerCircleCenterPositionX = centerPositionX;
        outerCircleCenterPositionY = centerPositionY;
        innerCircleCenterPositionX = centerPositionX;
        innerCircleCenterPositionY = centerPositionY;
    }

    public void setVisible(boolean isVisible){
        this.isVisible = isVisible;
    }
    public boolean getVisible(){
        return isVisible;
    }

    public double getActuatorX() {
        return actuatorX;
    }
    public double getActuatorY() {
        return actuatorY;
    }
}
