package com.mygames.growingball;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;


public class Tube {
    private Bitmap image;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;


    public Tube(Bitmap bmp) {
        image = bmp;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, 0, screenHeight/3, null);

    }
}
