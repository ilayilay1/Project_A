package com.example.projecta;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Game Manages all objects in the game and is responsible for updating all states and
 * render all objects to the screen
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Player player;
    private final CooldownBar cooldownBar;
    private final Joystick joystick;
    public GameLoop gameLoop;
    public static FlashScreen flashScreen;
    public static ArrayList<EnemyRectangle> enemiesRectangles;
    public static  ArrayList<EnemyRectangle> enemiesToDeleteRectangles;
    public static ArrayList<EnemyCircle> enemiesCircles;
    public static  ArrayList<EnemyCircle> enemiesToDeleteCircles;
    public static ArrayList<ArrowHead> arrowHeadList;
    public static  ArrayList<ArrowHead> arrowHeadToDelete;
    private int firstFingerIndex;
    private Level level;
    public boolean isDialogRunning = false, isDead = false;
    Dialog dialog;

    public Game(Context context) {
        this(context, null);
    }

    public Game(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // Gets the surface holder and adds callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        SharedPreferences sp = GameActivity.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timeInApplication", 0); // Restarts the internal app clock
        editor.commit();

        gameLoop = new GameLoop(this, surfaceHolder);

        flashScreen = new FlashScreen();
        enemiesRectangles = new ArrayList<>();
        enemiesToDeleteRectangles = new ArrayList<>();
        enemiesCircles = new ArrayList<>();
        enemiesToDeleteCircles = new ArrayList<>();
        arrowHeadList = new ArrayList<>();
        arrowHeadToDelete = new ArrayList<>();

        // Make game objects
        joystick = new Joystick(275, 700, 100, 40); // X and Y coordinates are irrelevant after v1.5
        cooldownBar = new CooldownBar(1000, 450, 0, 0, 10, ContextCompat.getColor(context, R.color.white)); //Width is temporarily 0 because the cooldown bar is a set length of 100
        player = new Player(cooldownBar ,joystick, 1000, 500, 30, ContextCompat.getColor(context, R.color.player));

        dialog = new Dialog(GameActivity.context);

        level = new Level();

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
        if(!isDialogRunning){
            if(gameLoop.getState().equals(Thread.State.TERMINATED)){
                gameLoop = new GameLoop(this, surfaceHolder);
            }
            resume();
            resumeObjects();
        }
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
        //drawUPS(canvas);
        //drawFPS(canvas);
        drawHP(canvas);

        joystick.draw(canvas);
        for (EnemyRectangle enemy: enemiesRectangles)
            enemy.draw(canvas);
        for (EnemyCircle enemy: enemiesCircles)
            enemy.draw(canvas);
        for (ArrowHead arrowHead: arrowHeadList)
            arrowHead.draw(canvas);
        player.draw(canvas);
        cooldownBar.draw(canvas);
        flashScreen.draw(canvas);
        if(player.getHitPoints() <= 0 && !isDead){
            level.levelMusic.pause();
            isDead = true;
            pause();
            ((Activity) GameActivity.context).runOnUiThread(() -> ((GameActivity)GameActivity.context).openGameOverDialog());
            //GameActivity.context.startActivity(new Intent(GameActivity.context, MainMenu.class));
        }
    }

    public void drawHP(Canvas canvas){
        String hp = Integer.toString(player.getHitPoints());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(50);
        canvas.drawText("Remaining Health : " + hp, 50, 75, paint);
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("UPS: " + averageUPS, 50, 200, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        int color = ContextCompat.getColor(getContext(), R.color.magenta);
        paint.setColor(color);
        paint.setTextSize(50);
        canvas.drawText("FPS: " + averageFPS, 50, 300, paint);
    }

    public void update() {
        // Update game state : the order of appearance affects the layers of the objects on the screen
        joystick.update();
        cooldownBar.update();
        player.update();
        for (ArrowHead arrowHead: arrowHeadList)
            arrowHead.update();
        enemiesToDeleteRectangles.clear();
        for (EnemyRectangle enemy: enemiesRectangles)
            enemy.update();
        for (EnemyCircle enemy: enemiesCircles)
            enemy.update();
        removeEnemies();
        flashScreen.update();

        switch(((GameActivity)GameActivity.context).levelNumber){
            case 1:
                level.updateLevel1();
                break;
            case 2:
                level.updateLevel2();
                break;
            case 3:
                level.updateLevel3();
        }

        // Log.e("TAG", "" + gameLoop.timeInApp);
    }

    public static void removeEnemies(){ // Deletes enemies which were scheduled to be removed!
        for (EnemyRectangle enemy: enemiesToDeleteRectangles)
            enemiesRectangles.remove(enemy);

        for (EnemyCircle enemy: enemiesToDeleteCircles)
            enemiesCircles.remove(enemy);

        for (ArrowHead arrowHead: arrowHeadToDelete)
            arrowHeadList.remove(arrowHead);
    }

    public void pause() {
        if(!isDead)
            gameLoop.stopLoop();
        else
            gameLoop.stopLoopASynchronized();

        SharedPreferences sp = GameActivity.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("timeInApplication", Double.doubleToRawLongBits(gameLoop.timeInApp));
        editor.apply();

        level.levelMusic.pause();
        cooldownBar.pause();
        player.pause();
        for (EnemyRectangle enemy: enemiesRectangles)
            enemy.pause();
        for (EnemyCircle enemy: enemiesCircles)
            enemy.pause();
    }

    public void resume(){
        SurfaceHolder surfaceHolder = getHolder();
        gameLoop = new GameLoop(this, surfaceHolder);
        gameLoop.startLoop(); // Resumes the gameLoop
    }

    public void resumeObjects(){
        if(player.getHitPoints() > 0){
            SharedPreferences sp = GameActivity.context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
            gameLoop.timeInApp = Double.longBitsToDouble(sp.getLong("timeInApplication", 0));
            level.levelMusic.start();
            cooldownBar.resume();
            player.resume();
            for (EnemyRectangle enemy: enemiesRectangles)
                enemy.resume();
            for (EnemyCircle enemy: enemiesCircles)
                enemy.resume();
        }
    }
}
