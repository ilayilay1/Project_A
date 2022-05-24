package com.example.projecta;

import android.graphics.Color;

import androidx.core.content.ContextCompat;

/**
 * FlashScreen is an object created to "Flash" the screen once a powerful attack is unleashed in order to add immersion
 * The FlashScreen class is an extension of a Rectangle, which is an extension of a GameObject
 */

public class FlashScreen extends Rectangle{
    private long timeOfActivation;
    private boolean flashActive = false;

    public FlashScreen() {
        super(Color.WHITE, GameActivity.size.x/2, GameActivity.size.y/2, 0, GameActivity.size.x, GameActivity.size.y);
        paint.setAlpha(0);
    }

    @Override
    public void update() {
        long deltaT = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp - timeOfActivation;
        if(deltaT > 60 && flashActive){
            flashActive = false;
            paint.setAlpha(0);
        }
        else if(flashActive)
            paint.setAlpha(30);
    }

    public void activateFlashScreen(){
        paint.setColor(Color.WHITE);
        paint.setAlpha(0);
        timeOfActivation = (long) ((GameActivity)GameActivity.context).game.gameLoop.timeInApp;
        flashActive = true;
    }
}
