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

    private TextView from;
    private ImageView bbva, meta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Animation anim1= AnimationUtils.loadAnimation(this,R.anim.desplaza_arriba);
        Animation anim2= AnimationUtils.loadAnimation(this,R.anim.desplaza_abajo);

        bbva=findViewById(R.id.imageViewBBVA);
        from=findViewById(R.id.textViewFrom);
        meta=findViewById(R.id.imageViewMeta);

        bbva.setAnimation(anim1);
        from.setAnimation(anim2);
        meta.setAnimation(anim2);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intenta=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intenta);
                finish();
            }

        },2000);
    }
}