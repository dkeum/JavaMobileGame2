package com.mygames.growingball;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView( new GameView(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // gameView.resume();
    }
}
