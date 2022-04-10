package com.example.projecta;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Player is the main character of the game, which the user can control with a touch joystick.
 * The player class is an extension of a Circle, which is an extension of a GameObject
 */
public class Player extends Circle {
    private static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private final Joystick joystick;
    private boolean dashCooldown = false, invincible = false; //No cooldown for dash and player isn't immune
    Handler handler = new Handler();


    public Player(Joystick joystick, double positionX, double positionY, double radius, int color){
        super(color, positionX, positionY, radius);
        this.joystick = joystick;

    }


    public void update() {
        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;  // Keep in mind height and width are reversed here & try to get this to work

        // Update position
        if(MainActivity.size.x - radius <= positionX + velocityX) // Calculating the X boundaries
            positionX = MainActivity.size.x - radius;
        else if(0 + radius >= positionX + velocityX)
            positionX = 0 + radius;
        else
            positionX += velocityX;

        if(MainActivity.size.y - radius <= positionY + velocityY) // Calculating the Y boundaries
            positionY = MainActivity.size.y - radius;
        else if(0 + radius >= positionY + velocityY)
            positionY = 0 + radius;
        else
            positionY += velocityY;
    }

    public void setPosition(double positionX, double positionY) { // Irrelevant for now, was used for testing, might use in future for extra features
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void dashForward() { // Player activates dash ability

        dashCooldown = true;
        invincible = true;

        new Timer().schedule(new TimerTask() { // Cooldown removed after 1.5 seconds
            @Override
            public void run() {
                dashCooldown = false;
            }
        }, 1500);

        new Timer().schedule(new TimerTask() { // Removes the invincibility given to the player during the dash
            @Override
            public void run() {
                invincible = false;
            }
        }, 300);

        velocityX = joystick.getActuatorX(); // Sets distance of the vector to a set distance
        velocityY = joystick.getActuatorY();
        double normalizer = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)); // Gets the resultant vector size
        velocityX *= MAX_SPEED*GameLoop.MAX_UPS*0.84/normalizer; // MAX_SPEED depends on the UPS, so we utilize the UPS for a set distance no matter the UPS
        velocityY *= MAX_SPEED*GameLoop.MAX_UPS*0.84/normalizer;
        double targetPositionX = positionX, targetPositionY = positionY;

        // Copy pasting from the update method
        if(MainActivity.size.x - radius <= positionX + velocityX) // Calculating the X boundaries
            targetPositionX = MainActivity.size.x - radius;
        else if(0 + radius >= positionX + velocityX)
            targetPositionX = 0 + radius;
        else
            targetPositionX += velocityX;

        if(MainActivity.size.y - radius <= positionY + velocityY) // Calculating the Y boundaries
            targetPositionY = MainActivity.size.y - radius;
        else if(0 + radius >= positionY + velocityY)
            targetPositionY = 0 + radius;
        else
            targetPositionY += velocityY;

        final ValueAnimator positionXAnimation = ValueAnimator.ofFloat((float)positionX, (float)targetPositionX); // X value animation
        positionXAnimation.setDuration(300);
        positionXAnimation.addUpdateListener(valueAnimator -> positionX = Double.parseDouble(positionXAnimation.getAnimatedValue().toString()));
        positionXAnimation.start();

        final ValueAnimator positionYAnimation = ValueAnimator.ofFloat((float)positionY, (float)targetPositionY); // Y value animation
        positionYAnimation.setDuration(300);
        positionYAnimation.addUpdateListener(valueAnimator -> positionY = Double.parseDouble(positionYAnimation.getAnimatedValue().toString()));
        positionYAnimation.start();

        double radiusBackup = radius; //Ensures our real radius doesn't get lost in the process!
        final ValueAnimator radiusAnimationSmall = ValueAnimator.ofFloat((float)radius, (float)radius/2); // Player gets small animation NOTE
        radiusAnimationSmall.setDuration(150);
        radiusAnimationSmall.addUpdateListener(valueAnimator -> radius = Double.parseDouble(radiusAnimationSmall.getAnimatedValue().toString()));
        radiusAnimationSmall.start();

        handler.postDelayed(() -> {
            final ValueAnimator radiusAnimationBig = ValueAnimator.ofFloat((float)radius, (float)radiusBackup); // Player grows back animation
            radiusAnimationBig.setDuration(150);
            radiusAnimationBig.addUpdateListener(valueAnimator -> radius = Double.parseDouble(radiusAnimationBig.getAnimatedValue().toString()));
            radiusAnimationBig.start();
        }, 150);
    }

    public boolean getCooldown() {
        return dashCooldown;
    }
}
