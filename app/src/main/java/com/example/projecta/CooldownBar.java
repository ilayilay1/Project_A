package com.example.projecta;

import android.animation.ValueAnimator;

public class CooldownBar extends Rectangle {

    public CooldownBar(double positionX, double positionY, float degree, double width, double height, int color){
        super(color, positionX, positionY, degree, width, height);
    }

    public void update() {

    }

    public void trackPlayer(double positionX, double positionY){
        this.positionX = positionX;
        this.positionY = positionY - 50;
    }

    public void startCountdown(){
        final ValueAnimator barAnimation = ValueAnimator.ofFloat(100, 0); // X value animation
        barAnimation.setDuration(1500);
        barAnimation.addUpdateListener(valueAnimator -> width = Double.parseDouble(barAnimation.getAnimatedValue().toString()));
        barAnimation.start();
    }

}
