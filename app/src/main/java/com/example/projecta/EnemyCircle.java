package com.example.projecta;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.renderscript.Sampler;

import androidx.core.content.ContextCompat;

/**
 * EnemyCircle is the obstacle in the game, which the user needs to avoid at all costs.
 * The enemy class is an extension of a Rectangle, which is an extension of a GameObject
 */

public class EnemyCircle extends Circle {
    private boolean isDeadly = false, isAnimationActive = false, runOnce = false;
    private float timeInMs;
    private int attackStyle, attackStatus;
    private final int ATTACK_FIRST_PHASE = 1, ATTACK_SECOND_PHASE = 2, ATTACK_THIRD_PHASE = 3;
    private double staticRadius;
    private long timeSincePhase;
    ValueAnimator enemyCircleAnimationFirst, enemyColorAnimationFirst, enemyCircleAnimationSecond, colorAnimationSecond,
            enemyCircleAnimationThird, enemyColorAnimationThird;
    ValueAnimator[] valueAnimators;

    public EnemyCircle(int color, double positionX, double positionY, double radius, float timeInMs, int attackStyle) {
        super(color, positionX, positionY, radius);
        paint.setAlpha(1);
        this.timeInMs = timeInMs;
        this.attackStyle = attackStyle;
        staticRadius = radius;
        attackStatus = ATTACK_FIRST_PHASE;
        timeSincePhase = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp;
        createAnimators();
    }

    @Override
    public void update() {
        long deltaT = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp - timeSincePhase;

        if (deltaT >= timeInMs && deltaT < timeInMs + 300) {
            attackStatus = ATTACK_SECOND_PHASE;
            if (!runOnce)
                isAnimationActive = false;
            runOnce = true;
            isDeadly = true; // Makes the enemy actually deadly
        }
        if (deltaT >= timeInMs + 300) {
            attackStatus = ATTACK_THIRD_PHASE;
            if (runOnce)
                isAnimationActive = false;
            runOnce = false;
        }
        if (deltaT >= timeInMs + 800) {
            isDeadly = false; // Makes the enemy no longer deadly
            Game.enemiesToDeleteCircles.add(this); // Adds to the enemy delete list!
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
        ((Activity)GameActivity.context).runOnUiThread(() -> {
            enemyCircleAnimationThird = ValueAnimator.ofFloat((float) staticRadius, 0); // Y value animation
            enemyCircleAnimationThird.setDuration((long) 500);
            enemyCircleAnimationThird.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimationThird.getAnimatedValue().toString()));
            enemyCircleAnimationThird.start();

            enemyColorAnimationThird = ValueAnimator.ofFloat(255, 0); // Y value animation
            enemyColorAnimationThird.setDuration((long) 500);
            enemyColorAnimationThird.addUpdateListener(valueAnimator -> paint.setAlpha((int) Double.parseDouble(enemyColorAnimationThird.getAnimatedValue().toString())));
            enemyColorAnimationThird.start();
        });
    }

    private void secondPhase() {
        isAnimationActive = true;
        Game.flashScreen.activateFlashScreen(); // Flashes the screen
        paint.setAlpha(255);
        paint.setColor(Color.WHITE);
        // Log.e("TAG", "Second Phase Running");
        switch (attackStyle) {
            case 1:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyCircleAnimationSecond = ValueAnimator.ofFloat(0, (float) staticRadius); // Y value animation
                    enemyCircleAnimationSecond.setDuration((long) 300);
                    enemyCircleAnimationSecond.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimationSecond.getAnimatedValue().toString()));
                    enemyCircleAnimationSecond.start();


                    colorAnimationSecond = ValueAnimator.ofObject(new ArgbEvaluator(), paint.getColor(),
                            ContextCompat.getColor(GameActivity.context, R.color.enemy));
                    colorAnimationSecond.setDuration(300); // milliseconds
                    colorAnimationSecond.addUpdateListener(animator -> paint.setColor((int) animator.getAnimatedValue()));
                    colorAnimationSecond.start();
                });
                break;

        }
    }

    private void firstPhase() {
        isAnimationActive = true;
        // Log.e("TAG", "First Phase Running");
        switch (attackStyle) {
            case 1:
                ((Activity) GameActivity.context).runOnUiThread(() -> {
                    enemyCircleAnimationFirst = ValueAnimator.ofFloat(0, (float) radius);
                    enemyCircleAnimationFirst.setDuration((long) timeInMs);
                    enemyCircleAnimationFirst.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimationFirst.getAnimatedValue().toString()));
                    enemyCircleAnimationFirst.start();

                    enemyColorAnimationFirst = ValueAnimator.ofFloat(1, 125);
                    enemyColorAnimationFirst.setDuration((long) timeInMs);
                    enemyColorAnimationFirst.addUpdateListener(valueAnimator -> paint.setAlpha((int) Double.parseDouble(enemyColorAnimationFirst.getAnimatedValue().toString())));
                    enemyColorAnimationFirst.start();
                });
                break;
        }
    }
    public boolean getDeadly() {
        return isDeadly;
    }

    public void pause(){
        for (ValueAnimator valueAnimator : valueAnimators) {
            if (valueAnimator.isRunning())
                valueAnimator.pause();
        }
    }

    public void resume(){
        for (ValueAnimator valueAnimator : valueAnimators) {
            if (valueAnimator.isRunning())
                valueAnimator.resume();
        }
    }

    public void createAnimators(){
        enemyColorAnimationThird = ValueAnimator.ofFloat(255, 0); // Y value animation
        enemyColorAnimationThird.setDuration((long) 500);
        enemyColorAnimationThird.addUpdateListener(valueAnimator -> paint.setAlpha((int) Double.parseDouble(enemyColorAnimationThird.getAnimatedValue().toString())));

        enemyCircleAnimationThird = ValueAnimator.ofFloat((float) staticRadius, 0); // Y value animation
        enemyCircleAnimationThird.setDuration((long) 500);
        enemyCircleAnimationThird.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimationThird.getAnimatedValue().toString()));

        colorAnimationSecond = ValueAnimator.ofObject(new ArgbEvaluator(), paint.getColor(),
                ContextCompat.getColor(GameActivity.context, R.color.enemy));
        colorAnimationSecond.setDuration(300); // milliseconds
        colorAnimationSecond.addUpdateListener(animator -> paint.setColor((int) animator.getAnimatedValue()));

        enemyCircleAnimationSecond = ValueAnimator.ofFloat(0, (float) staticRadius); // Y value animation
        enemyCircleAnimationSecond.setDuration((long) 300);
        enemyCircleAnimationSecond.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimationSecond.getAnimatedValue().toString()));

        enemyCircleAnimationFirst = ValueAnimator.ofFloat(0, (float) radius);
        enemyCircleAnimationFirst.setDuration((long) timeInMs);
        enemyCircleAnimationFirst.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimationFirst.getAnimatedValue().toString()));

        enemyColorAnimationFirst = ValueAnimator.ofFloat(1, 125);
        enemyColorAnimationFirst.setDuration((long) timeInMs);
        enemyColorAnimationFirst.addUpdateListener(valueAnimator -> paint.setAlpha((int) Double.parseDouble(enemyColorAnimationFirst.getAnimatedValue().toString())));

        valueAnimators = new ValueAnimator[] {enemyCircleAnimationFirst, enemyColorAnimationFirst, enemyCircleAnimationSecond, colorAnimationSecond,
                enemyCircleAnimationThird, enemyColorAnimationThird};
    }

}
