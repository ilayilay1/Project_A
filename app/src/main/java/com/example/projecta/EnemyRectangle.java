package com.example.projecta;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;

/**
 * EnemyRectangle is the obstacle in the game, which the user needs to avoid at all costs.
 * The enemy class is an extension of a Rectangle, which is an extension of a GameObject
 */

public class EnemyRectangle extends Rectangle {

    private boolean isDeadly = false, isAnimationActive = false, runOnce = false;
    private float timeInMs, timeInMsExtra = 0;
    private int attackStyle, attackStatus;
    private final int ATTACK_FIRST_PHASE = 1, ATTACK_SECOND_PHASE = 2, ATTACK_THIRD_PHASE = 3;
    private double staticWidth, staticHeight, staticPositionY, staticPositionX;
    private long timeSincePhase;

    public EnemyRectangle(double positionX, double positionY, float degree, double width, double height, int color, float timeInMs, int attackStyle){
        super(color, positionX, positionY, degree, width, height);
        paint.setAlpha(1);
        this.timeInMs = timeInMs;
        this.attackStyle = attackStyle;
        staticPositionX = positionX;
        staticPositionY = positionY;
        staticWidth = width;
        staticHeight = height;
        attackStatus = ATTACK_FIRST_PHASE;
        timeSincePhase = System.currentTimeMillis();
        if(attackStyle >= 2)
            timeInMsExtra = 5000; // Extra time for the animations if the attack style is 2 or above

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
        if(deltaT >= timeInMs + 300 + timeInMsExtra){
            attackStatus = ATTACK_THIRD_PHASE;
            if(runOnce)
                isAnimationActive = false;
            runOnce = false;
        }
        if(deltaT >= timeInMs + 800 + timeInMsExtra){
            isDeadly = false; // Makes the enemy no longer deadly
            Game.enemiesToDeleteRectangles.add(this); // Adds to the enemy delete list!
        }
        if(!isAnimationActive)
            switch(attackStatus){
                case 1:
                    firstPhase();
                    break;
                case 2:
                    secondPhase();
                    break;
                case 3:
                    thirdPhase();
                    break;
            }
    }

    private void thirdPhase() {
        isAnimationActive = true;
        switch(attackStyle) {
            case 1:
                ((Activity)MainActivity.context).runOnUiThread(() -> {
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
                });
                break;
            case 2:
            case 3:
            case 4:
                return;
        }
    }

    private void secondPhase() {
        isAnimationActive = true;
        Game.flashScreen.activateFlashScreen(); // Flashes the screen
        paint.setAlpha(255);
        paint.setColor(Color.WHITE);
       // Log.e("TAG", "Second Phase Running");
        switch(attackStyle){
            case 2:
                ((Activity)MainActivity.context).runOnUiThread(() -> {
                    final ValueAnimator enemyRectangleWidthAnimation = ValueAnimator.ofFloat(100, (float)(MainActivity.size.y + staticWidth/2)); // Y value animation
                    enemyRectangleWidthAnimation.setDuration((long) timeInMsExtra);
                    enemyRectangleWidthAnimation.addUpdateListener(valueAnimator -> positionY = Double.parseDouble(enemyRectangleWidthAnimation.getAnimatedValue().toString()));
                    enemyRectangleWidthAnimation.start();
                });
                break;
            case 3:
                ((Activity)MainActivity.context).runOnUiThread(() -> {
                    final ValueAnimator enemyRectangleWidthAnimation = ValueAnimator.ofFloat((float)MainActivity.size.y - 100, (float)(0 - staticWidth/2)); // Y value animation
                    enemyRectangleWidthAnimation.setDuration((long) timeInMsExtra);
                    enemyRectangleWidthAnimation.addUpdateListener(valueAnimator -> positionY = Double.parseDouble(enemyRectangleWidthAnimation.getAnimatedValue().toString()));
                    enemyRectangleWidthAnimation.start();
                });
                break;
            case 4:
                ((Activity)MainActivity.context).runOnUiThread(() -> {
                    final ValueAnimator enemyRectangleWidthAnimation = ValueAnimator.ofFloat((float)(staticWidth/2), (float)(MainActivity.size.x + staticWidth/2)); // Y value animation
                    enemyRectangleWidthAnimation.setDuration((long) timeInMsExtra);
                    enemyRectangleWidthAnimation.addUpdateListener(valueAnimator -> positionX = Double.parseDouble(enemyRectangleWidthAnimation.getAnimatedValue().toString()));
                    enemyRectangleWidthAnimation.start();
                });
                break;
            case 5:
                ((Activity)MainActivity.context).runOnUiThread(() -> {
                    final ValueAnimator enemyRectangleWidthAnimation = ValueAnimator.ofFloat((float)(MainActivity.size.x - staticWidth/2), (float)(-staticWidth/2)); // Y value animation
                    enemyRectangleWidthAnimation.setDuration((long) timeInMsExtra);
                    enemyRectangleWidthAnimation.addUpdateListener(valueAnimator -> positionX = Double.parseDouble(enemyRectangleWidthAnimation.getAnimatedValue().toString()));
                    enemyRectangleWidthAnimation.start();
                });
                break;

        }
        ((Activity)MainActivity.context).runOnUiThread(() -> {
            final ValueAnimator enemyRectangleWidthAnimation = ValueAnimator.ofFloat(0, (float) staticWidth); // Y value animation
            enemyRectangleWidthAnimation.setDuration((long) 300);
            enemyRectangleWidthAnimation.addUpdateListener(valueAnimator -> width = Double.parseDouble(enemyRectangleWidthAnimation.getAnimatedValue().toString()));
            enemyRectangleWidthAnimation.start();

            final ValueAnimator enemyRectangleHeightAnimation = ValueAnimator.ofFloat(0, (float) staticHeight); // Y value animation
            enemyRectangleHeightAnimation.setDuration((long) 300);
            enemyRectangleHeightAnimation.addUpdateListener(valueAnimator -> height = Double.parseDouble(enemyRectangleHeightAnimation.getAnimatedValue().toString()));
            enemyRectangleHeightAnimation.start();

            final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), paint.getColor(),
                    ContextCompat.getColor(MainActivity.context, R.color.enemy));
            colorAnimation.setDuration(300); // milliseconds
            colorAnimation.addUpdateListener(animator -> paint.setColor((int) animator.getAnimatedValue()));
            colorAnimation.start();
        });
    }

    private void firstPhase() {
        isAnimationActive = true;
       // Log.e("TAG", "First Phase Running");
            switch(attackStyle){
                case 2:
                    Game.arrowHeadList.add(new ArrowHead(0,0, ContextCompat.getColor(MainActivity.context, R.color.enemy), 1, timeInMs));
                    positionX = MainActivity.size.x/2;
                    positionY = staticWidth/2;
                    break;
                case 3:
                    Game.arrowHeadList.add(new ArrowHead(0,0, ContextCompat.getColor(MainActivity.context, R.color.enemy), 2, timeInMs));
                    positionX = MainActivity.size.x/2;
                    positionY = MainActivity.size.y-staticWidth/2;
                    break;
                case 4:
                    Game.arrowHeadList.add(new ArrowHead(0,0, ContextCompat.getColor(MainActivity.context, R.color.enemy), 3, timeInMs));
                    positionX = staticWidth/2;
                    positionY = MainActivity.size.y/2;
                    break;
                case 5:
                    Game.arrowHeadList.add(new ArrowHead(0,0, ContextCompat.getColor(MainActivity.context, R.color.enemy), 4, timeInMs));
                    positionX = MainActivity.size.x - staticWidth/2;
                    positionY = MainActivity.size.y/2;
                    break;
            }
        ((Activity)MainActivity.context).runOnUiThread(() -> {
            final ValueAnimator enemyRectangleAnimation = ValueAnimator.ofFloat(0, (float) staticWidth);
            enemyRectangleAnimation.setDuration((long) timeInMs);
            enemyRectangleAnimation.addUpdateListener(valueAnimator -> width = Double.parseDouble(enemyRectangleAnimation.getAnimatedValue().toString()));
            enemyRectangleAnimation.start();
        });
        ((Activity)MainActivity.context).runOnUiThread(() -> {
            final ValueAnimator enemyColorAnimation = ValueAnimator.ofFloat(1, 125);
            enemyColorAnimation.setDuration((long) timeInMs);
            enemyColorAnimation.addUpdateListener(valueAnimator -> {
                paint.setAlpha((int) Double.parseDouble(enemyColorAnimation.getAnimatedValue().toString()));
            });
            enemyColorAnimation.start();
        });
        }

    public boolean getDeadly() {
        return isDeadly;
    }
}
