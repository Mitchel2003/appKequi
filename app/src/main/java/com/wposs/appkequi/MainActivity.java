package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView from,azul,azul2,rojo,rojo2;
    private ImageView logo,kequi, bancolombia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//fullScreen

        //layout
        logo=(ImageView) findViewById(R.id.imageViewLogo);                  azul=(TextView) findViewById(R.id.azul);
        kequi=(ImageView) findViewById(R.id.imageViewKequi);                rojo=(TextView) findViewById(R.id.rojo);
        from=(TextView) findViewById(R.id.textViewFrom);                    azul2=(TextView) findViewById(R.id.azul2);
        bancolombia=(ImageView) findViewById(R.id.imageViewBancolombia);    rojo2=(TextView) findViewById(R.id.rojo2);

        animations();

    }


    private void animations(){
        //transitions
        Animation animDown= AnimationUtils.loadAnimation(this,R.anim.transition_down);
        Animation animUp= AnimationUtils.loadAnimation(this,R.anim.transition_up);
        Animation rotIzq= AnimationUtils.loadAnimation(this,R.anim.ss_rotate_blue);
        Animation rotDer= AnimationUtils.loadAnimation(this,R.anim.ss_rotate_red);
        Animation rotHeadIzq= AnimationUtils.loadAnimation(this,R.anim.ss_head_rotate_blue);
        Animation rotHeadDer= AnimationUtils.loadAnimation(this,R.anim.ss_head_rotate_red);

        logo.startAnimation(animDown);
        kequi.startAnimation(animDown);
        azul2.startAnimation(rotHeadIzq);
        rojo2.startAnimation(rotHeadDer);

        //after of 2 seconds initialize anim lower
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //return transparency
                from.animate().alpha(1).setDuration(2000);
                bancolombia.animate().alpha(1).setDuration(2000);
                azul.animate().alpha(1).setDuration(2000);
                rojo.animate().alpha(1).setDuration(2000);

                from.startAnimation(animUp);
                bancolombia.startAnimation(animUp);
                azul.startAnimation(rotIzq);
                rojo.startAnimation(rotDer);
            }
        },2000);


        //Opacity for exit SplashScreen
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){

                //Opacity transparency
                from.animate().alpha(0).setDuration(1000);
                bancolombia.animate().alpha(0).setDuration(1000);
                azul.animate().alpha(0).setDuration(1000);
                rojo.animate().alpha(0).setDuration(1000);
                logo.animate().alpha(0).setDuration(1000);
                kequi.animate().alpha(0).setDuration(1000);
                azul2.animate().alpha(0).setDuration(1000);
                rojo2.animate().alpha(0).setDuration(1000);

                Intent go=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(go);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        },5000);
    }
}