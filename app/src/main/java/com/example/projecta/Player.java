package com.example.projecta;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.content.ContextCompat;

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
    private final CooldownBar cooldownBar;
    private boolean dashCooldown = false, invincible = false, isDamaged = false; //No cooldown for dash and player isn't immune
    Handler handler = new Handler();
    private long lastDamageTaken;
    private int color;
    final MediaPlayer hitSound = MediaPlayer.create(MainActivity.context, R.raw.hit);


    public Player(CooldownBar cooldownBar, Joystick joystick, double positionX, double positionY, double radius, int color) {
        super(color, positionX, positionY, radius);
        this.joystick = joystick;
        this.cooldownBar = cooldownBar;
        this.color = color;
    }


    public void update() {
        // Update velocity based on actuator of joystick
        velocityX = joystick.getActuatorX() * MAX_SPEED;
        velocityY = joystick.getActuatorY() * MAX_SPEED;  // Keep in mind height and width are reversed here & try to get this to work

        // Update position
        if (MainActivity.size.x - radius <= positionX + velocityX) // Calculating the X boundaries
            positionX = MainActivity.size.x - radius;
        else if (0 + radius >= positionX + velocityX)
            positionX = 0 + radius;
        else
            positionX += velocityX;

        if (MainActivity.size.y - radius <= positionY + velocityY) // Calculating the Y boundaries
            positionY = MainActivity.size.y - radius;
        else if (0 + radius >= positionY + velocityY)
            positionY = 0 + radius;
        else
            positionY += velocityY;

        cooldownBar.trackPlayer(positionX, positionY); // Tracks the cooldown bar to the new Player location
        synchronized (Game.enemies){
            for (Enemy enemy : Game.enemies)
                if (Collision.circleToRect(this, enemy, enemy.getDeadly()) && !this.getDamageStatus() && !invincible) {
                    isDamaged = true;
                    invincible = true;
                    lastDamageTaken = System.currentTimeMillis();
                    hitSound.start(); // Plays damaged sound
                    break;
                }
        }
        if (isDamaged) {
            damageAnimation();
        }
    }

    public void setPosition(double positionX, double positionY) { // Irrelevant for now, was used for testing, might use in future for extra features
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public void damageAnimation() { // Activates in a scenario where the player gets hit by something resulting in life lost and damage taken

        long deltaT = System.currentTimeMillis() - lastDamageTaken;
        if (deltaT > 2500) {
            invincible = false;
            isDamaged = false;
            paint.setColor(color);
            return;
        }
        if (deltaT % 300 < 150)
            paint.setAlpha(150);
        else
            paint.setColor(color);

    }

    public void dashForward() { // Player activates dash ability
        if(joystick.getActuatorY() == 0 && joystick.getActuatorX() == 0){
            return;
        }
        dashCooldown = true;
        invincible = true;
        cooldownBar.startCountdown(); // Start cooldown bar countdown

        final int[] number = {0};
        final long[] mTimeLeftInMillis = {1500};
        CountDownTimer mCountDownTimer = new CountDownTimer(mTimeLeftInMillis[0], 300) {
            // Timer's functionality is after 1.5 seconds the cooldown wears off and after 0.3 seconds the player is no longer invincible
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis[0] = millisUntilFinished;
                number[0]++;
                if (number[0] == 2 && !isDamaged)
                    invincible = false;
               // Log.e("TAG", "" + invincible);
            }

            @Override
            public void onFinish() {
                dashCooldown = false;
                number[0] = 0;
            }
        }.start();

        velocityX = joystick.getActuatorX(); // Sets distance of the vector to a set distance
        velocityY = joystick.getActuatorY();
        double normalizer = Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)); // Gets the resultant vector size
        velocityX *= MAX_SPEED * GameLoop.MAX_UPS * 0.84 / normalizer; // MAX_SPEED depends on the UPS, so we utilize the UPS for a set distance no matter the UPS
        velocityY *= MAX_SPEED * GameLoop.MAX_UPS * 0.84 / normalizer;
        double targetPositionX = positionX, targetPositionY = positionY;

        // Copy pasting from the update method
        if (MainActivity.size.x - radius <= positionX + velocityX) // Calculating the X boundaries
            targetPositionX = MainActivity.size.x - radius;
        else if (0 + radius >= positionX + velocityX)
            targetPositionX = 0 + radius;
        else
            targetPositionX += velocityX;

        if (MainActivity.size.y - radius <= positionY + velocityY) // Calculating the Y boundaries
            targetPositionY = MainActivity.size.y - radius;
        else if (0 + radius >= positionY + velocityY)
            targetPositionY = 0 + radius;
        else
            targetPositionY += velocityY;

        final ValueAnimator positionXAnimation = ValueAnimator.ofFloat((float) positionX, (float) targetPositionX); // X value animation
        positionXAnimation.setDuration(300);
        positionXAnimation.addUpdateListener(valueAnimator -> positionX = Double.parseDouble(positionXAnimation.getAnimatedValue().toString()));
        positionXAnimation.start();

        final ValueAnimator positionYAnimation = ValueAnimator.ofFloat((float) positionY, (float) targetPositionY); // Y value animation
        positionYAnimation.setDuration(300);
        positionYAnimation.addUpdateListener(valueAnimator -> positionY = Double.parseDouble(positionYAnimation.getAnimatedValue().toString()));
        positionYAnimation.start();

        double radiusBackup = radius; //Ensures our real radius doesn't get lost in the process!
        final ValueAnimator radiusAnimationSmall = ValueAnimator.ofFloat((float) radius, (float) radius / 2); // Player gets small animation NOTE
        radiusAnimationSmall.setDuration(150);
        radiusAnimationSmall.addUpdateListener(valueAnimator -> radius = Double.parseDouble(radiusAnimationSmall.getAnimatedValue().toString()));
        radiusAnimationSmall.start();

        handler.postDelayed(() -> {
            final ValueAnimator radiusAnimationBig = ValueAnimator.ofFloat((float) radius, (float) radiusBackup); // Player grows back animation
            radiusAnimationBig.setDuration(150);
            radiusAnimationBig.addUpdateListener(valueAnimator -> radius = Double.parseDouble(radiusAnimationBig.getAnimatedValue().toString()));
            radiusAnimationBig.start();
        }, 150);
    }

    public boolean getCooldown() {
        return dashCooldown;
    }

    public boolean getDamageStatus() {
        return isDamaged;
    }
}
