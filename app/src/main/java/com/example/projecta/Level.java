package com.example.projecta;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class Level {
 // Note : Enemy attacks that use style 5 and 4 should be 0 degrees! 2 and 3 should be 90 degrees! and 1 can be whatever
    public static MediaPlayer levelMusic;
    boolean isRunning = false, runOnce = false;
    Random rnd = new Random();
    private long previousTime = 0, previousTime2 = 0;
    private int levelStage = 1, timeMs1 = 0, timeMs2 = 0;
    public static int levelLength = 1;

    public Level(){
        switch(((GameActivity)GameActivity.context).levelNumber){
            case 1:
                levelMusic = MediaPlayer.create(GameActivity.context, R.raw.level1);
                levelMusic.setLooping(true);
                levelLength = 50000;
                break;
            case 2:
                //levelMusic = MediaPlayer.create(GameActivity.context, R.raw.level2);
                levelMusic.setLooping(true);
                break;
            case 3:
                //levelMusic = MediaPlayer.create(GameActivity.context, R.raw.level3);
                levelMusic.setLooping(true);
                break;
        }
    }

    public void updateLevel3(){

    }

    public void updateLevel2(){

    }

    public void updateLevel1() {
        long currentTime = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp;
        if(currentTime > levelLength){
            ((Activity) GameActivity.context).runOnUiThread(() -> ((GameActivity)GameActivity.context).openGameWonDialog());
        }
        switch(levelStage)
        {
            case 2:
                timeMs2 = 1619;
                if(currentTime - previousTime2 >= timeMs2) {
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            75, timeMs2, 1));
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            75, timeMs2 + 90, 1));
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            75, timeMs2 + 180, 1));
                    previousTime2 = currentTime;
                    if((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp > 26534){
                        levelStage = 3;
                        break;
                    }
                }
            case 1:
                timeMs1 = 857;
                if(currentTime - previousTime >= timeMs1){
                    Game.enemiesRectangles.add(new EnemyRectangle( GameActivity.size.x/2, GameActivity.size.y/2, rnd.nextInt(179)+10, 75, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), timeMs1, 1));
                    previousTime = currentTime;
                }
                if((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp > 13710){
                    levelStage = 2;
                    runOnce = true;
                }
                break;
            case 3:
                timeMs1 = 418;
                if(runOnce){
                    Game.enemiesRectangles.add(new EnemyRectangle(GameActivity.size.x/2, GameActivity.size.y/5, 90 , 100, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), timeMs1, 1));
                    Game.enemiesRectangles.add(new EnemyRectangle(GameActivity.size.x/2, GameActivity.size.y/2, 90 , 100, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), timeMs1 * 2, 1));
                    Game.enemiesRectangles.add(new EnemyRectangle(GameActivity.size.x/2, GameActivity.size.y/1.2, 90 , 100, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), timeMs1 * 3, 1));
                    runOnce = false;
                }
                if((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp > 27000)
                levelStage = 4;
                break;
            case 4:
                timeMs1 = 857*4;
                if(currentTime - previousTime >= timeMs1){
                    Game.enemiesRectangles.add(new EnemyRectangle( 0, 0, 90, 100, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), timeMs1, rnd.nextInt(4)+2));
                    previousTime = currentTime;
                }
                timeMs2 = 1690;
                if(currentTime - previousTime2 >= timeMs2) {
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            90, timeMs2, 1));
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            90, timeMs2 + 60, 1));
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            90, timeMs2 + 120, 1));
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            90, timeMs2 + 180, 1));
                    previousTime2 = currentTime;
                }
                break;
            case 5:

                break;
        }

    }

    public void updateLevelEndless(){

    }

}
