package com.mygames.growingball;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class CharacterSprite {
    private Bitmap image;
    public int x,y;
    private int xVelocity = 20;
    private int yVelocity = 10;
    public int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    public int image_radius;
    private Bitmap original_image;
    private int original_size;






    public CharacterSprite(Bitmap bmp, int xx, int yy) {
        image = bmp;
        int factor_reduction =3;
        image_radius = image.getWidth()/factor_reduction;
        original_size = image_radius/factor_reduction;
        image = Bitmap.createScaledBitmap(image,image_radius/factor_reduction, image_radius/factor_reduction,false);

        original_image = image;
        image_radius = image_radius/factor_reduction;


        int min = xx;
        int max = yy;

        //Generate random int value from 50 to 100
        int random_int1 = (int)Math.floor(Math.random()*(max-min+1)+min);
        int random_int2 = (int)Math.floor(Math.random()*(max-min+1)+min);

        min = 10;
        max = 20;

        xVelocity = (int)Math.floor(Math.random()*(max-min+1)+min);
        yVelocity = (int)Math.floor(Math.random()*(max-min+1)+min);


        x= random_int1;
        y= random_int2;

        if(x%2 == 0 ){
            xVelocity = xVelocity*-1;
        }
        if(y%2 ==0 ){
            yVelocity = yVelocity*-1;
        }
    }



    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        image_radius = image.getWidth()/2;
        image = Bitmap.createScaledBitmap(image,image_radius/2, image_radius/2,false);
        image_radius = image_radius/2;

        int min = 0;
        int max = 1000;

        //Generate random int value from 50 to 100
        int random_int1 = (int)Math.floor(Math.random()*(max-min+1)+min);
        int random_int2 = (int)Math.floor(Math.random()*(max-min+1)+min);

        x= random_int1;
        y= random_int2;

        if(x%2 == 0 ){
            xVelocity =xVelocity*-1;
        }
        if(y%2 ==0 ){
            yVelocity =yVelocity*-1;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void update(){


       if(x<0 && y<0){
           x= screenWidth/2;
           y=screenHeight/2;
       }else{
           x+= xVelocity;
           y+=yVelocity;
           if((x>screenWidth-image.getWidth())|| (x<0)){
               xVelocity=xVelocity*-1;
           }
           if((y>screenHeight-image.getHeight())|| (y<(0))){
               yVelocity=yVelocity*-1;
           }
       }
    }

    public Rect getCollisionShape(){
        return new Rect(x, y, x+getImageWidth() , y+getImageWidth());
    }

    public int getImageWidth(){
        return image.getWidth();
    }


    public int growball(CharacterSprite ball1, CharacterSprite ball2, CharacterSprite ball3,CharacterSprite ball4  ,int level){
        int prev_image_radius = image_radius;

        if(image_radius < screenHeight*0.50 ) {
             image_radius = (int) (image_radius * 1.02); // growthrate
        }else{
            return 2;
        }
        image = Bitmap.createScaledBitmap(image,image_radius, image_radius,false);


        if(level == 1) {
            if (Rect.intersects(ball1.getCollisionShape(), this.getCollisionShape())) {
                return 1;
            }
        }
        if(level == 2) {
        if (Rect.intersects(ball1.getCollisionShape(), this.getCollisionShape())) {
            return 1;
        }
        if (Rect.intersects(ball2.getCollisionShape(), this.getCollisionShape())) {
         return 1;
        }
    }
        if(level == 3) {
            if (Rect.intersects(ball1.getCollisionShape(), this.getCollisionShape())) {
                return 1;
            }
            if (Rect.intersects(ball2.getCollisionShape(), this.getCollisionShape())) {
                return 1;
            }
            if (Rect.intersects(ball4.getCollisionShape(), this.getCollisionShape())) {
                return 1;
            }
        }



        int Delta_radius = Math.abs(image_radius - prev_image_radius);
        //conditions that ball doesnt grow out of bounds
        int xx, yy;
        xx =x +image_radius;
        yy= y +image_radius;

        if(x<0){
            x=0;
        }

        if(x>screenWidth-image.getWidth()){
            x=screenWidth-image.getWidth();
        }
        if(y>screenHeight-image.getHeight()){
            y=screenHeight-image.getHeight();
        }

        if(y<0){
            y=0;
        }
        return 0;
    }






    public void CalculateCollision(CharacterSprite ball){

        if(Rect.intersects(ball.getCollisionShape(), this.getCollisionShape())){
            int temp1,temp2;

            temp1 = ball.xVelocity;
            temp2 = ball.yVelocity;

            ball.xVelocity = xVelocity;
            ball.yVelocity = yVelocity;

            xVelocity = temp1 ;
            yVelocity = temp2;
        }

    }


    public void setbacktodefault(int position ){
        // position of the ball
        // speed of the ball
        //size of ball;

        x = 100*position;
        y = 100*position;
        image = original_image;
        image_radius = original_size;

    }


}


