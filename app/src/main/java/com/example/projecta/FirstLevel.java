package com.example.projecta;

import android.media.MediaPlayer;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import java.util.Random;

public class FirstLevel {

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
        if(currentTime - previousTime >= 855){
            previousTime = currentTime;
            Game.enemies.add(new Enemy( 1000, rnd.nextInt(1000)+10, rnd.nextInt(179)+1, 100, 2500,
                    ContextCompat.getColor(MainActivity.context, R.color.enemy), 2500, 1));
        }
    }

}
