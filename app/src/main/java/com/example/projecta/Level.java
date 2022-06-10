package com.example.projecta;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class Level {
 // Note : Enemy attacks that use style 5 and 4 should be 0 degrees! 2 and 3 should be 90 degrees! and 1 can be whatever
    public static MediaPlayer levelMusic;
    boolean isRunning = false, runOnce = false;
    Random rnd = new Random();
    private long previousTime = 0, previousTime2 = 0;
    private long[] previousTimes = new long[10];
    private int levelStage = 1, timeMs1 = 0, timeMs2 = 0;
    public static int levelLength = 1;

    public Level(){
        for (int i = 0; i < 10; i++)
            previousTimes[i] = 0;

        switch(((GameActivity)GameActivity.context).levelNumber){
            case 1:
                levelMusic = MediaPlayer.create(GameActivity.context, R.raw.level1);
                levelMusic.setLooping(true);
                levelLength = 55000;
                break;
            case 2:
                //levelMusic = MediaPlayer.create(GameActivity.context, R.raw.level2);
                levelMusic.setLooping(true);
                break;
            case 3:
                //levelMusic = MediaPlayer.create(GameActivity.context, R.raw.level3);
                levelMusic.setLooping(true);
                break;
            case 99:
                levelMusic = MediaPlayer.create(GameActivity.context, R.raw.endless);
                levelMusic.setLooping(true);
                break;
        }
    }

    public void updateLevel3(){

    }

    public void updateLevel2(){

    }

    public void updateLevel1() { // Use Previoustimes for perfect sync, first stages don't use it because it's too late :(
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
        long currentTime = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp;
        double difficultyProgression = (((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp)/25000) - 6;
        final int DIFFICULTY_MULTIPLIER = 1; // Changable multiplier for the difficulty progression
        double difficultyLevel = 1/(1 + Math.exp(difficultyProgression * DIFFICULTY_MULTIPLIER));
        levelStage = (int) (((long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp)/30000);
        switch(levelStage){
            case 5:
                if(currentTime - previousTimes[5] >= 1200*difficultyLevel){
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            90 + 150*(1-difficultyLevel), 1000, 1));
                    previousTimes[5] = currentTime;
                }
            case 4:
                if(currentTime - previousTimes[4] >= 1000*difficultyLevel) {
                    Game.enemiesRectangles.add(new EnemyRectangle(GameActivity.size.x / (rnd.nextDouble() + rnd.nextInt(5)), GameActivity.size.y / (rnd.nextDouble() + rnd.nextInt(5)), 90, 75 + 75 * difficultyLevel, 75 + 75 * difficultyLevel,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), 900, 1));
                    previousTimes[4] = currentTime;
                }
            case 3:
                if(currentTime - previousTimes[3] >= 3000*difficultyLevel) {
                    Game.enemiesRectangles.add(new EnemyRectangle(0, 0, 90, 100 + 50*difficultyLevel, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), 3200 + (int)(1250*difficultyLevel), rnd.nextInt(4) + 2));
                    previousTimes[3] = currentTime;
                }
            case 2:
                if(currentTime - previousTimes[2] >= 900*difficultyLevel){
                    Game.enemiesRectangles.add(new EnemyRectangle( GameActivity.size.x/2, GameActivity.size.y/(rnd.nextDouble() + rnd.nextInt(5)), rnd.nextInt(11)+85, 75, 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), 700 +(int)(400*difficultyLevel), 1));
                    previousTimes[2] = currentTime;
                }
            case 1:
                if(currentTime - previousTimes[1] >= 750*difficultyLevel){
                    Game.enemiesCircles.add(new EnemyCircle(ContextCompat.getColor(GameActivity.context, R.color.enemy), rnd.nextInt(GameActivity.size.x), rnd.nextInt(GameActivity.size.y),
                            70 + 150*(1-difficultyLevel), 250 + (int)(500*difficultyLevel), 1));
                    previousTimes[1] = currentTime;
                }
            case 0:
                if(currentTime - previousTimes[0] >= 1500*difficultyLevel){
                    Game.enemiesRectangles.add(new EnemyRectangle( GameActivity.size.x/2, GameActivity.size.y/2, rnd.nextInt(179)+10, 75 + 75*(1-difficultyLevel), 3000,
                            ContextCompat.getColor(GameActivity.context, R.color.enemy), 500 + (int)(550*difficultyLevel), 1));
                    previousTimes[0] = currentTime;
                }
                break;
        }
    }

    public String generateAttackString(){

        return "Something";
    }

}
