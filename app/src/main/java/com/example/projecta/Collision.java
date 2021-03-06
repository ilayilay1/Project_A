package com.example.projecta;


import android.graphics.Matrix;
import android.util.Log;

import java.util.Arrays;

/**
 * The Collision class contains all types of interactions between the Player and the Enemy class
 */

public class Collision {
    public static boolean circleToRect(Circle circle, Rectangle rectangle, boolean isDeadly){
        if (!isDeadly) // If the attack isn't generated yet, cancel function
            return false;

        Matrix matrix =  new Matrix();
        matrix.setRotate(-rectangle.degree);
        float[] circlePosition = {
                (float)circle.positionX - (float)rectangle.positionX,
                (float)circle.positionY - (float)rectangle.positionY
        };
        matrix.mapPoints(circlePosition);
//        Log.d("PRINT", Arrays.toString(circlePosition));

        float[] circleDistance = {
                Math.abs(circlePosition[0]),
                Math.abs(circlePosition[1])
        };

        if (circleDistance[0] > (rectangle.width/2 + circle.radius)) { return false; }
        if (circleDistance[1] > (rectangle.height/2 + circle.radius)) { return false; }

        if (circleDistance[0] <= (rectangle.width/2)) { return true; }
        if (circleDistance[1] <= (rectangle.height/2)) { return true; }

        double cornerDistance_sq = Math.pow(circleDistance[0] - rectangle.width/2, 2) + Math.pow(circleDistance[1] - rectangle.height/2, 2);

        return cornerDistance_sq <= Math.pow(circle.radius, 2);

    }

    public static boolean circleToCircle(Circle circle1, Circle circle2, boolean isDeadly){
        if (!isDeadly) // If the attack isn't generated yet, cancel function
            return false;

        double deltaX = circle1.positionX - circle2.positionX;
        double deltaY = circle1.positionY - circle2.positionY;
        double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        if(distance < circle1.radius + circle2.radius){
            Log.e("TAG", "CIRCLE COLLISION, Player position : " + circle1.positionX + " Enemy position : " + + circle2.positionX
            + " Enemy radius : " + circle2.radius + " Player radius : " + circle1.radius + " distance " + distance);
            return true;
        }
        else
            return false;
    }

}
