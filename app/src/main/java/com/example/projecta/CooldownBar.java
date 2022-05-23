package com.example.projecta;

import android.animation.ValueAnimator;
import android.graphics.Color;

/**
 * CooldownBar is an object created for the player to predict when they would be able to use the dash ability again which is derived from the Player class
 * The CooldownBar class is an extension of a Rectangle, which is an extension of a GameObject
 */

public class CooldownBar extends Rectangle {
    ValueAnimator barAnimation;

    public CooldownBar(double positionX, double positionY, float degree, double width, double height, int color){
        super(color, positionX, positionY, degree, width, height);

        barAnimation = ValueAnimator.ofFloat(100, 0); // X value animation
        barAnimation.setDuration(1500);
        barAnimation.addUpdateListener(valueAnimator -> this.width = Double.parseDouble(barAnimation.getAnimatedValue().toString()));
    }

    public void update() {

    }

    public void trackPlayer(double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY - 50;
    }

    public void startCountdown(){
        barAnimation.start();
    }
    public void pause(){
        if(barAnimation.isRunning())
            barAnimation.pause();
    }
    public void resume(){
        if(barAnimation.isRunning())
            barAnimation.resume();
    }

}
