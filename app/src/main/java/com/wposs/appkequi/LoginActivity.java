package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView backgroundBlue, backgroundRed, login, createAccount;
    private EditText email, password;
    private TableLayout tableRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //declaration variables
        email=findViewById(R.id.editTextEmail);             login = findViewById(R.id.textRegister);                backgroundBlue = findViewById(R.id.azulLg);
        password=findViewById(R.id.editTextPassword);       tableRegister = findViewById(R.id.tableRegister);       backgroundRed = findViewById(R.id.rojoLg);
        createAccount = findViewById(R.id.textCreateAccount);

        //animations
        Animation animBlue= AnimationUtils.loadAnimation(this,R.anim.lg_rotate_blue);
        Animation animRed= AnimationUtils.loadAnimation(this,R.anim.lg_rotate_red);
        Animation animDown= AnimationUtils.loadAnimation(this,R.anim.lg_transition_down);

        //run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backgroundBlue.animate().alpha(1).setDuration(4000);
                backgroundRed.animate().alpha(1).setDuration(4000);
                backgroundBlue.startAnimation(animBlue);
                backgroundRed.startAnimation(animRed);
                login.startAnimation(animDown);
                tableRegister.startAnimation(animDown);
                createAccount.startAnimation(animDown);
            }
        },0);

        //run infinite
        rotateIconIzq(backgroundBlue);
        rotateIconDer(backgroundRed);
    }

    public void entry(){
    }















    //-----------------------------------------ads------------------------------------------------------
    //rotation infinity turn right
    private void rotateIconDer (View see){
        RotateAnimation rotationDer = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotationDer.setDuration(4000);
        see.startAnimation(rotationDer);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotateIconDer(see);
            }
        }, 10000);
    }
    //rotation infinity turn left
    private void rotateIconIzq (View see){
        RotateAnimation rotationIzq = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotationIzq.setDuration(4000);
        see.startAnimation(rotationIzq);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rotateIconIzq(see);
            }
        }, 10000);
    }
}
