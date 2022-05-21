package com.example.projecta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Game Manages all objects in the game and is responsible for updating all states and
 * render all objects to the screen
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final CooldownBar cooldownBar;
    private final Joystick joystick;
    private final GameLoop gameLoop;
    public static final FlashScreen flashScreen = new FlashScreen();
    public static final ArrayList<Enemy> enemies = new ArrayList<>();
    public static  ArrayList<Enemy> enemiesToDelete = new ArrayList<>();
    private int firstFingerIndex;
    private FirstLevel firstLevel;


    public Game(Context context) {
        super(context);
        // Gets the surface holder and adds callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);

        // Make game objects
        joystick = new Joystick(275, 700, 100, 40); // X and Y coordinates are irrelevant after v1.5
        cooldownBar = new CooldownBar(1000, 450, 0, 0, 10, ContextCompat.getColor(context, R.color.white)); //Width is temporarily 0 because the cooldown bar is a set length of 100
        player = new Player(cooldownBar ,joystick, 1000, 500, 30, ContextCompat.getColor(context, R.color.player));

        firstLevel = new FirstLevel();

        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Handle touch event actions
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                firstFingerIndex = event.getActionIndex();
                joystick.setIsPressed(true);
                joystick.setLocation((int)event.getX(), (int)event.getY());
                joystick.setVisible(true);
                return true;

            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()){
                    joystick.setActuator(event.getX(), event.getY());
                }
                return true;

            case MotionEvent.ACTION_UP:
                joystick.setIsPressed(false);
                joystick.resetActuator();
                joystick.setVisible(false);
                return true;

            case MotionEvent.ACTION_POINTER_DOWN: // Secondary touch event mainly to trigger the dash method for the player
                if(joystick.getIsPressed() && !player.getCooldown())
                    player.dashForward();
                if(!joystick.getIsPressed()){ // In case player lifts the joystick finger and continues to place the dashing finger all whilst trying to move with the joystick
                    joystick.setIsPressed(true);
                    joystick.setLocation((int)event.getX(), (int)event.getY());
                    joystick.setVisible(true);
                }
                return true;

            case MotionEvent.ACTION_POINTER_UP: // Secondary finger lifted
                if(firstFingerIndex == event.getActionIndex()){ // In case the player removes the joystick finger while having the dashing finger on the screen, removes the joystick
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    joystick.setVisible(false);
                }
                return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        drawUPS(canvas);
        drawFPS(canvas);

        joystick.draw(canvas);
        for (Enemy enemy: enemies)
            enemy.draw(canvas);
        player.draw(canvas);
        cooldownBar.draw(canvas);
        flashScreen.draw(canvas);

    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 100, 100, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 100, 200, paint);
    }

    public void update() {
        // Update game state : the order of appearance affects the layers of the objects on the screen
        joystick.update();
        cooldownBar.update();
        player.update();
        enemiesToDelete.clear();
        for (Enemy enemy: enemies)
            enemy.update();
        removeEnemies();
        flashScreen.update();
        firstLevel.update();
    }

    public static void removeEnemies(){ // Deletes enemies which were scheduled to be removed!
        for (Enemy enemy: enemiesToDelete)
            enemies.remove(enemy);
    }
}
