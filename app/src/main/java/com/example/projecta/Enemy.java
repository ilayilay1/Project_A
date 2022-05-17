package com.example.projecta;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * Enemy is the obstacle in the game, which the user needs to avoid at all costs.
 * The enemy class is an extension of a Rectangle, which is an extension of a GameObject
 */

public class Enemy extends Rectangle {

    private boolean isDeadly = false, isAnimationActive = false, runOnce = false;
    private float timeInMs;
    private int attackStyle, attackStatus;
    private final int ATTACK_FIRST_PHASE = 1, ATTACK_SECOND_PHASE = 2, ATTACK_THIRD_PHASE = 3;
    private double staticWidth, staticHeight;
    private long timeSincePhase;
    Handler handler = new Handler();

    public Enemy(double positionX, double positionY, float degree, double width, double height, int color, float timeInMs, int attackStyle){
        super(color, positionX, positionY, degree, width, height);
        paint.setColor(ContextCompat.getColor(MainActivity.context, R.color.enemyTransparent));
        paint.setAlpha(120);
        this.timeInMs = timeInMs;
        this.attackStyle = attackStyle;
        staticWidth = width;
        staticHeight = height;
        // activateAttack(attackStyle);
        attackStatus = ATTACK_FIRST_PHASE;
        timeSincePhase = System.currentTimeMillis();
        //firstPhase();

    }

    public void update() {
        long deltaT = System.currentTimeMillis() - timeSincePhase;

        if (deltaT >= timeInMs && deltaT < timeInMs + 300) {
            attackStatus = ATTACK_SECOND_PHASE;
            if(!runOnce)
                isAnimationActive = false;
            runOnce = true;
            isDeadly = true; // Makes the enemy actually deadly
        }
        if(deltaT >= timeInMs + 300){
            attackStatus = ATTACK_THIRD_PHASE;
            if(runOnce)
                isAnimationActive = false;
            runOnce = false;
        }
        if(deltaT >= timeInMs + 800){
            isDeadly = false; // Makes the enemy no longer deadly
            Game.enemiesToDelete.add(this); // Adds to the enemy delete list!
        }
        if(!isAnimationActive)
            switch(attackStatus){
                case 1:
                    handler.post(() -> { firstPhase(); });
                    break;
                case 2:
                    handler.post(() -> { secondPhase(); });
                    break;
                case 3:
                    handler.post(() -> { thirdPhase(); });
                    break;
            }
    }

    private void thirdPhase() {
        isAnimationActive = true;

        final ValueAnimator enemyRectangleAnimation = ValueAnimator.ofFloat((float) staticWidth, 0); // Y value animation
        enemyRectangleAnimation.setDuration((long) 500);
        enemyRectangleAnimation.addUpdateListener(valueAnimator -> width = Double.parseDouble(enemyRectangleAnimation.getAnimatedValue().toString()));
        enemyRectangleAnimation.start();

        final ValueAnimator enemyColorAnimation = ValueAnimator.ofFloat(255, 0); // Y value animation
        enemyColorAnimation.setDuration((long) 500);
        enemyColorAnimation.addUpdateListener(valueAnimator -> {
            paint.setAlpha((int) Double.parseDouble(enemyColorAnimation.getAnimatedValue().toString()));
        });
        enemyColorAnimation.start();

    }

    private void secondPhase() {
        isAnimationActive = true;
        Game.flashScreen.activateFlashScreen(); // Flashes the screen
        paint.setAlpha(255);
        paint.setColor(Color.WHITE);
        Log.e("TAG", "Second Phase Running");
        switch(attackStyle){
            case 1:
                final ValueAnimator enemyRectangleWidthAnimation = ValueAnimator.ofFloat(0, (float) staticWidth); // Y value animation
                enemyRectangleWidthAnimation.setDuration((long) 300);
                enemyRectangleWidthAnimation.addUpdateListener(valueAnimator -> width = Double.parseDouble(enemyRectangleWidthAnimation.getAnimatedValue().toString()));
                enemyRectangleWidthAnimation.start();

                final ValueAnimator enemyRectangleHeightAnimation = ValueAnimator.ofFloat(0, (float) staticHeight); // Y value animation
                enemyRectangleHeightAnimation.setDuration((long) 300);
                enemyRectangleHeightAnimation.addUpdateListener(valueAnimator -> height = Double.parseDouble(enemyRectangleHeightAnimation.getAnimatedValue().toString()));
                enemyRectangleHeightAnimation.start();

                final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), paint.getColor(),
                        ContextCompat.getColor(MainActivity.context, R.color.enemyTransparent));
                colorAnimation.setDuration(300); // milliseconds
                colorAnimation.addUpdateListener(animator -> paint.setColor((int) animator.getAnimatedValue()));
                colorAnimation.start();

                break;

        }
    }

    private void firstPhase() {
        isAnimationActive = true;
        Log.e("TAG", "First Phase Running");
            switch(attackStyle){
            case 1:
                final ValueAnimator enemyRectangleAnimation = ValueAnimator.ofFloat(0, (float) staticWidth);
                enemyRectangleAnimation.setDuration((long) timeInMs);
                enemyRectangleAnimation.addUpdateListener(valueAnimator -> width = Double.parseDouble(enemyRectangleAnimation.getAnimatedValue().toString()));
                enemyRectangleAnimation.start();

                final ValueAnimator enemyColorAnimation = ValueAnimator.ofFloat(1, 125);
                enemyColorAnimation.setDuration((long) timeInMs);
                enemyColorAnimation.addUpdateListener(valueAnimator -> {
                    paint.setAlpha((int) Double.parseDouble(enemyColorAnimation.getAnimatedValue().toString()));
                });
                enemyColorAnimation.start();
                break;
        }
    }

    public boolean getDeadly() {
        return isDeadly;
    }
}
