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
    ValueAnimator enemyColorAnimationFirst, enemyRectangleAnimationFirst, colorAnimationSecond1, enemyRectangleHeightAnimationSecond1,
            enemyRectangleWidthAnimationSecond1, enemyRectangleWidthAnimationSecond2, enemyRectangleWidthAnimationSecond3,
            enemyRectangleWidthAnimationSecond4, enemyRectangleWidthAnimationSecond5, enemyRectangleAnimationThird, enemyColorAnimationThird;
    ValueAnimator[] valueAnimators;

    public EnemyRectangle(double positionX, double positionY, float degree, double width, double height, int color, float timeInMs, int attackStyle) {
        super(color, positionX, positionY, degree, width, height);
        paint.setAlpha(1);
        this.timeInMs = timeInMs;
        this.attackStyle = attackStyle;
        staticPositionX = positionX;
        staticPositionY = positionY;
        staticWidth = width;
        staticHeight = height;
        attackStatus = ATTACK_FIRST_PHASE;
        timeSincePhase = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp;
        if (attackStyle >= 2)
            timeInMsExtra = 5000; // Extra time for the animations if the attack style is 2 or above

        createAnimators();
    }

    public void update() {
        long deltaT = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp - timeSincePhase;

        if (deltaT >= timeInMs && deltaT < timeInMs + 300) {
            attackStatus = ATTACK_SECOND_PHASE;
            if (!runOnce)
                isAnimationActive = false;
            runOnce = true;
            isDeadly = true; // Makes the enemy actually deadly
        }
        if (deltaT >= timeInMs + 300 + timeInMsExtra) {
            attackStatus = ATTACK_THIRD_PHASE;
            if (runOnce)
                isAnimationActive = false;
            runOnce = false;
        }
        if (deltaT >= timeInMs + 800 + timeInMsExtra) {
            isDeadly = false; // Makes the enemy no longer deadly
            Game.enemiesToDeleteRectangles.add(this); // Adds to the enemy delete list!
        }
        if (!isAnimationActive)
            switch (attackStatus) {
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
        switch (attackStyle) {
            case 1:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyRectangleAnimationThird.start();
                    enemyColorAnimationThird.start();
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
        switch (attackStyle) {
            case 2:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyRectangleWidthAnimationSecond2.start();
                });
                break;
            case 3:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyRectangleWidthAnimationSecond3.start();
                });
                break;
            case 4:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyRectangleWidthAnimationSecond4.start();
                });
                break;
            case 5:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyRectangleWidthAnimationSecond5.start();
                });
                break;

        }
        ((Activity) GameActivity.context).runOnUiThread(() -> {
            enemyRectangleWidthAnimationSecond1.start();
            enemyRectangleHeightAnimationSecond1.start();
            colorAnimationSecond1.start();
        });
    }

    private void firstPhase() {
        isAnimationActive = true;
        // Log.e("TAG", "First Phase Running");
        switch (attackStyle) {
            case 2:
                Game.arrowHeadList.add(new ArrowHead(0, 0, ContextCompat.getColor(GameActivity.context, R.color.enemy), 1, timeInMs));
                positionX = GameActivity.size.x / 2;
                positionY = staticWidth / 2;
                break;
            case 3:
                Game.arrowHeadList.add(new ArrowHead(0, 0, ContextCompat.getColor(GameActivity.context, R.color.enemy), 2, timeInMs));
                positionX = GameActivity.size.x / 2;
                positionY = GameActivity.size.y - staticWidth / 2;
                break;
            case 4:
                Game.arrowHeadList.add(new ArrowHead(0, 0, ContextCompat.getColor(GameActivity.context, R.color.enemy), 3, timeInMs));
                positionX = staticWidth / 2;
                positionY = GameActivity.size.y / 2;
                break;
            case 5:
                Game.arrowHeadList.add(new ArrowHead(0, 0, ContextCompat.getColor(GameActivity.context, R.color.enemy), 4, timeInMs));
                positionX = GameActivity.size.x - staticWidth / 2;
                positionY = GameActivity.size.y / 2;
                break;
        }
        ((Activity) GameActivity.context).runOnUiThread(() -> {
            enemyRectangleAnimationFirst.start();
        });
        ((Activity) GameActivity.context).runOnUiThread(() -> {
            enemyColorAnimationFirst.start();
        });
    }

    public boolean getDeadly() {
        return isDeadly;
    }

    public void pause() {
        for (ValueAnimator valueAnimator : valueAnimators) {
            if (valueAnimator.isRunning())
                valueAnimator.pause();
        }
    }

    public void resume() {
        for (ValueAnimator valueAnimator : valueAnimators) {
            if (valueAnimator.isRunning())
                valueAnimator.resume();
        }
    }

        public void createAnimators(){

            enemyColorAnimationThird = ValueAnimator.ofFloat(255, 0); // Y value animation
            enemyColorAnimationThird.setDuration((long) 500);
            enemyColorAnimationThird.addUpdateListener(valueAnimator -> paint.setAlpha((int) Double.parseDouble(enemyColorAnimationThird.getAnimatedValue().toString())));

            enemyRectangleAnimationThird = ValueAnimator.ofFloat((float) staticWidth, 0); // Y value animation
            enemyRectangleAnimationThird.setDuration((long) 500);
            enemyRectangleAnimationThird.addUpdateListener(valueAnimator -> this.width = Double.parseDouble(enemyRectangleAnimationThird.getAnimatedValue().toString()));

            enemyRectangleWidthAnimationSecond5 = ValueAnimator.ofFloat((float) (GameActivity.size.x - staticWidth / 2), (float) (-staticWidth / 2)); // Y value animation
            enemyRectangleWidthAnimationSecond5.setDuration((long) timeInMsExtra);
            enemyRectangleWidthAnimationSecond5.addUpdateListener(valueAnimator -> this.positionX = Double.parseDouble(enemyRectangleWidthAnimationSecond5.getAnimatedValue().toString()));

            enemyRectangleWidthAnimationSecond4 = ValueAnimator.ofFloat((float) (staticWidth / 2), (float) (GameActivity.size.x + staticWidth / 2)); // Y value animation
            enemyRectangleWidthAnimationSecond4.setDuration((long) timeInMsExtra);
            enemyRectangleWidthAnimationSecond4.addUpdateListener(valueAnimator -> this.positionX = Double.parseDouble(enemyRectangleWidthAnimationSecond4.getAnimatedValue().toString()));

            enemyRectangleWidthAnimationSecond3 = ValueAnimator.ofFloat((float) GameActivity.size.y - 100, (float) (0 - staticWidth / 2)); // Y value animation
            enemyRectangleWidthAnimationSecond3.setDuration((long) timeInMsExtra);
            enemyRectangleWidthAnimationSecond3.addUpdateListener(valueAnimator -> this.positionY = Double.parseDouble(enemyRectangleWidthAnimationSecond3.getAnimatedValue().toString()));

            enemyRectangleWidthAnimationSecond2 = ValueAnimator.ofFloat(100, (float) (GameActivity.size.y + staticWidth / 2)); // Y value animation
            enemyRectangleWidthAnimationSecond2.setDuration((long) timeInMsExtra);
            enemyRectangleWidthAnimationSecond2.addUpdateListener(valueAnimator -> this.positionY = Double.parseDouble(enemyRectangleWidthAnimationSecond2.getAnimatedValue().toString()));

            colorAnimationSecond1 = ValueAnimator.ofObject(new ArgbEvaluator(), paint.getColor(),
                    ContextCompat.getColor(GameActivity.context, R.color.enemy));
            colorAnimationSecond1.setDuration(300); // milliseconds
            colorAnimationSecond1.addUpdateListener(animator -> paint.setColor((int) animator.getAnimatedValue()));

            enemyRectangleHeightAnimationSecond1 = ValueAnimator.ofFloat(0, (float) staticHeight); // Y value animation
            enemyRectangleHeightAnimationSecond1.setDuration((long) 300);
            enemyRectangleHeightAnimationSecond1.addUpdateListener(valueAnimator -> this.height = Double.parseDouble(enemyRectangleHeightAnimationSecond1.getAnimatedValue().toString()));

            enemyRectangleWidthAnimationSecond1 = ValueAnimator.ofFloat(0, (float) staticWidth); // Y value animation
            enemyRectangleWidthAnimationSecond1.setDuration((long) 300);
            enemyRectangleWidthAnimationSecond1.addUpdateListener(valueAnimator -> this.width = Double.parseDouble(enemyRectangleWidthAnimationSecond1.getAnimatedValue().toString()));

            enemyColorAnimationFirst = ValueAnimator.ofFloat(1, 125);
            enemyColorAnimationFirst.setDuration((long) timeInMs);
            enemyColorAnimationFirst.addUpdateListener(valueAnimator -> paint.setAlpha((int) Double.parseDouble(enemyColorAnimationFirst.getAnimatedValue().toString())));

            enemyRectangleAnimationFirst = ValueAnimator.ofFloat(0, (float) staticWidth);
            enemyRectangleAnimationFirst.setDuration((long) timeInMs);
            enemyRectangleAnimationFirst.addUpdateListener(valueAnimator -> this.width = Double.parseDouble(enemyRectangleAnimationFirst.getAnimatedValue().toString()));

            valueAnimators = new ValueAnimator[]{enemyColorAnimationFirst, enemyRectangleAnimationFirst, colorAnimationSecond1, enemyRectangleHeightAnimationSecond1,
                    enemyRectangleWidthAnimationSecond1, enemyRectangleWidthAnimationSecond2, enemyRectangleWidthAnimationSecond3,
                    enemyRectangleWidthAnimationSecond4, enemyRectangleWidthAnimationSecond5, enemyRectangleAnimationThird, enemyColorAnimationThird};
        }
    }
