package com.example.projecta;

import android.media.MediaPlayer;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class FirstLevel {
 // Note : Enemy attacks that use style 5 and 4 should be 0 degrees! 2 and 3 should be 90 degrees! and 1 can be whatever
    public static MediaPlayer levelMusic;
    boolean isRunning = false;
    Random rnd = new Random();
    private long previousTime = 0;

    public FirstLevel(){
        levelMusic = MediaPlayer.create(MainActivity.context, R.raw.level1);
        levelMusic.setLooping(true);
        levelMusic.start();
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - previousTime >= 8550){
            previousTime = currentTime;
            Game.enemiesRectangles.add(new EnemyRectangle( 0, 0, 0, 100, 2500,
                    ContextCompat.getColor(MainActivity.context, R.color.enemy), 2500, 5));
            Game.enemiesRectangles.add(new EnemyRectangle( 0, 0, 0, 100, 2500,
                    ContextCompat.getColor(MainActivity.context, R.color.enemy), 2500, 4)); /*
            Game.enemiesRectangles.add(new EnemyRectangle( 1000, rnd.nextInt(1000)+10, rnd.nextInt(179)+1, 100, 2500,
                    ContextCompat.getColor(MainActivity.context, R.color.enemy), 2500, 1));
            Game.enemiesCircles.add(new EnemyCircle( ContextCompat.getColor(MainActivity.context, R.color.enemy),
                    rnd.nextInt(MainActivity.size.x), rnd.nextInt(MainActivity.size.y), 100, 2500, 1));*/
        }
    }

}
