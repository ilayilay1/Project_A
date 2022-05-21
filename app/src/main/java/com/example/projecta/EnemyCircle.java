package com.example.projecta;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

public class EnemyCircle extends Circle {
    private boolean isDeadly = false, isAnimationActive = false, runOnce = false;
    private float timeInMs;
    private int attackStyle, attackStatus;
    private final int ATTACK_FIRST_PHASE = 1, ATTACK_SECOND_PHASE = 2, ATTACK_THIRD_PHASE = 3;
    private double staticRadius;
    private long timeSincePhase;

    public EnemyCircle(int color, double positionX, double positionY, double radius, float timeInMs, int attackStyle) {
        super(color, positionX, positionY, radius);
        paint.setAlpha(1);
        this.timeInMs = timeInMs;
        this.attackStyle = attackStyle;
        staticRadius = radius;
        // activateAttack(attackStyle);
        attackStatus = ATTACK_FIRST_PHASE;
        timeSincePhase = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long deltaT = System.currentTimeMillis() - timeSincePhase;

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
        ((Activity)MainActivity.context).runOnUiThread(() -> {
            final ValueAnimator enemyCircleAnimation = ValueAnimator.ofFloat((float) staticRadius, 0); // Y value animation
            enemyCircleAnimation.setDuration((long) 500);
            enemyCircleAnimation.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimation.getAnimatedValue().toString()));
            enemyCircleAnimation.start();

            final ValueAnimator enemyColorAnimation = ValueAnimator.ofFloat(255, 0); // Y value animation
            enemyColorAnimation.setDuration((long) 500);
            enemyColorAnimation.addUpdateListener(valueAnimator -> {
                paint.setAlpha((int) Double.parseDouble(enemyColorAnimation.getAnimatedValue().toString()));
            });
            enemyColorAnimation.start();
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
                ((Activity) MainActivity.context).runOnUiThread(() -> {
                    final ValueAnimator enemyCircleAnimation = ValueAnimator.ofFloat(0, (float) staticRadius); // Y value animation
                    enemyCircleAnimation.setDuration((long) 300);
                    enemyCircleAnimation.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimation.getAnimatedValue().toString()));
                    enemyCircleAnimation.start();

                    final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), paint.getColor(),
                            ContextCompat.getColor(MainActivity.context, R.color.enemy));
                    colorAnimation.setDuration(300); // milliseconds
                    colorAnimation.addUpdateListener(animator -> paint.setColor((int) animator.getAnimatedValue()));
                    colorAnimation.start();
                });
                break;

        }
    }

    private void firstPhase() {
        isAnimationActive = true;
        // Log.e("TAG", "First Phase Running");
        switch (attackStyle) {
            case 1:
                ((Activity) MainActivity.context).runOnUiThread(() -> {
                    final ValueAnimator enemyCircleAnimation = ValueAnimator.ofFloat(0, (float) radius);
                    enemyCircleAnimation.setDuration((long) timeInMs);
                    enemyCircleAnimation.addUpdateListener(valueAnimator -> radius = Double.parseDouble(enemyCircleAnimation.getAnimatedValue().toString()));
                    enemyCircleAnimation.start();

                    final ValueAnimator enemyColorAnimation = ValueAnimator.ofFloat(1, 125);
                    enemyColorAnimation.setDuration((long) timeInMs);
                    enemyColorAnimation.addUpdateListener(valueAnimator -> {
                        paint.setAlpha((int) Double.parseDouble(enemyColorAnimation.getAnimatedValue().toString()));
                    });
                    enemyColorAnimation.start();
                });
                break;
        }
    }
    public boolean getDeadly() {
        return isDeadly;
    }
}
