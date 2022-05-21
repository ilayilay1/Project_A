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
        super(Color.WHITE, MainActivity.size.x/2, MainActivity.size.y/2, 0, MainActivity.size.x, MainActivity.size.y);
        paint.setAlpha(0);
    }

    @Override
    public void update() {
        long deltaT = System.currentTimeMillis() - timeOfActivation;
        if(deltaT > 40 && flashActive){
            flashActive = false;
            paint.setAlpha(0);
        }
        else if(flashActive)
            paint.setAlpha(25);
    }

    public void activateFlashScreen(){
        paint.setColor(Color.WHITE);
        paint.setAlpha(0);
        timeOfActivation = System.currentTimeMillis();
        flashActive = true;
    }
}
