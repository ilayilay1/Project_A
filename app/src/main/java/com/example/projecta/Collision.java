package com.example.projecta;


import android.graphics.Matrix;
import android.util.Log;

import java.util.Arrays;

public class Collision {
    public static boolean circleToRect(Circle circle, Rectangle rectangle){
        Matrix matrix =  new Matrix();
        matrix.setRotate(-rectangle.degree);
        float[] circlePosition = {
                (float)circle.positionX - (float)rectangle.positionX,
                (float)circle.positionY - (float)rectangle.positionY
        };
        matrix.mapPoints(circlePosition);
        Log.d("PRINT", Arrays.toString(circlePosition));

        float[] circleDistance = {
                (float) Math.abs(circlePosition[0]),
                (float) Math.abs(circlePosition[1])
        };

        if (circleDistance[0] > (rectangle.width/2 + circle.radius)) { return false; }
        if (circleDistance[1] > (rectangle.height/2 + circle.radius)) { return false; }

        if (circleDistance[0] <= (rectangle.width/2)) { return true; }
        if (circleDistance[1] <= (rectangle.height/2)) { return true; }

        double cornerDistance_sq = Math.pow(circleDistance[0] - rectangle.width/2, 2) + Math.pow(circleDistance[1] - rectangle.height/2, 2);

        return cornerDistance_sq <= Math.pow(circle.radius, 2);

    }

}
