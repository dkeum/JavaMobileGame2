package com.mygames.growingball;



import android.content.Context;

import android.content.SharedPreferences;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import androidx.annotation.MainThread;


public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    private temp temps;
    private CharacterSprite characterSprite,characterSprite2,characterSprite3,characterSprite4;
    private boolean gameover = false;
    int highScore=0;
    private SharedPreferences prefs;
    private SoundPool soundPool;
    private int sound,gameOverSounds;
    private Context context;
    private boolean gameoversounds = false;
    MediaPlayer mediaPlayer;
    private int level = 1;
    private boolean readytoRetry = false;
    private int isgameover;
    private boolean gamecomplete=false;
    private Ground ground;





    public GameView(Context context){

        super(context);
        this.context = context;
        getHolder().addCallback(this);
        temps = new temp(getHolder(), this);
        setFocusable(true);

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
         prefs = context.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        sound = soundPool.load(context, R.raw.growballmusic, 1);
        gameOverSounds = soundPool.load(context, R.raw.losesound, 1);



        // music
        mediaPlayer = MediaPlayer.create(context, R.raw.growballmusic);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.ball),100,200);
        characterSprite3 = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.ball),400,500);
        characterSprite2 = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.blueball),600,700);
        characterSprite4 = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.ball),800,900);
        ground = new Ground (BitmapFactory.decodeResource(getResources(),R.drawable.backgroundgame));



        temps.setRunning(true);
        temps.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        while (retry) {
            try {
                //temp.setRunning(false);
                temps.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }

    }

    public void update(){

        if(level == 1){
            characterSprite.CalculateCollision(characterSprite2); // 1 hits 2
            characterSprite.update();
            characterSprite2.update();
        }
        if(level ==2 ){

            characterSprite.CalculateCollision(characterSprite2); // 1 hits 2
            characterSprite2.CalculateCollision(characterSprite3); // 2 hits 3
            characterSprite.CalculateCollision(characterSprite3); // 1 hits 3
            characterSprite.update();
            characterSprite2.update();
            characterSprite3.update();
        }
        if(level ==3 ){

            characterSprite.CalculateCollision(characterSprite2); // 1 hits 2
            characterSprite.CalculateCollision(characterSprite3); // 1 hits 3
            characterSprite.CalculateCollision(characterSprite4); // 1 hits 4
            characterSprite2.CalculateCollision(characterSprite3); // 2 hits 3
            characterSprite2.CalculateCollision(characterSprite4); // 2 hits 4
            characterSprite3.CalculateCollision(characterSprite4); // 3 hits 4


            characterSprite.update();
            characterSprite2.update();
            characterSprite3.update();
            characterSprite4.update();

        }


    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            ground.draw(canvas);


            if(gamecomplete){
                //temps.setRunning(false);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(0, 0, 0));
                paint.setTextSize(100f);
                // canvas.drawRect(0, 150, 300, 0, paint);
                canvas.drawText("You Finished All Levels",characterSprite.screenWidth/2-400,characterSprite.screenHeight/2,paint);
                return;

            }
            if(gameover){
                mediaPlayer.setLooping(false);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(0, 0, 0));
                paint.setTextSize(100f);
               // canvas.drawRect(0, 150, 300, 0, paint);
                canvas.drawText("Try Again?",characterSprite.screenWidth/2-200,characterSprite.screenHeight/2,paint);

                if(!gameoversounds ) {
                    mediaPlayer.setLooping(false);
                    soundPool.play(gameOverSounds, 1, 1, 0, 0, 1);
                    gameoversounds = true;
                }
                try {
                    temp.sleep(3000);
                    readytoRetry = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return;
            }


            if(level == 1) {
                characterSprite.draw(canvas);
                characterSprite2.draw(canvas);
            }
            if(level ==2 ) {
                characterSprite.draw(canvas);
                characterSprite2.draw(canvas);
                characterSprite3.draw(canvas);
            }
            if(level == 3 ){
                characterSprite.draw(canvas);
                characterSprite2.draw(canvas);
                characterSprite3.draw(canvas);
                characterSprite4.draw(canvas);
            }

            Paint highscore = new Paint();
            highscore.setColor(Color.rgb(250, 250, 250));
            highscore.setTextSize(100f);

            String high_score_message = "High Score: " + highScore;
            canvas.drawText(high_score_message,100,100,highscore);
            String levelmessage = "Level: "+ level;
            canvas.drawText(levelmessage ,characterSprite.screenWidth-400,100, highscore);

        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
      //  int isgameover;// return 0 on game is continuing
        // return 1 on ball has collided
        // return 2 on level is passed

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // touch down code
                isgameover = characterSprite2.growball(characterSprite, characterSprite3, characterSprite2, characterSprite4, level);
                highScore++;
                if (isgameover == 1 ) {
                    gameover = true;
                    saveifHighScore();
                }
                if(isgameover == 2){  // return 2 on level is passed
                    if(level == 1 ) {
                        characterSprite.setbacktodefault(1);
                        characterSprite2.setbacktodefault(3);
                        characterSprite3.setbacktodefault(5);
                        characterSprite4.setbacktodefault(-1);
                    }
                    if(level == 2 ) {
                        characterSprite.setbacktodefault(1);
                        characterSprite2.setbacktodefault(3);
                        characterSprite3.setbacktodefault(5);
                        characterSprite4.setbacktodefault(9);
                    }
                    if(level ==3 ){
                        gamecomplete = true;
                    }

                    level++;
                    isgameover= 0;
                }

                if(readytoRetry == true){
                    //reset all to default setting
                    gameover = false;
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                    characterSprite.setbacktodefault(1);
                    characterSprite2.setbacktodefault(3);
                    characterSprite3.setbacktodefault(5);
                    characterSprite4.setbacktodefault(7);
                    highScore = 0;
                    readytoRetry= false;
                    gameoversounds = false;
                    // characterSprite
                }
                break;

            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                // touch up code
                break;
        }
      return true;
    }






    private void saveifHighScore() {
        if (prefs.getInt("highscore", 0) < highScore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", highScore);
            editor.apply();
        }
    }


}
