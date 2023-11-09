package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyActivity extends AppCompatActivity {

    private TextView welcome, numberBalance;    private ImageButton buttonSpawnOptions;
                                                private LinearLayout options,changeMoney, WSignOff;
                                                private View backgroundTouch;

    //DataBase Firebase Fire Store
    private FirebaseFirestore bd;
    private CollectionReference openBD;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        welcome=findViewById(R.id.textWelcome);                     buttonSpawnOptions=findViewById(R.id.buttonOptions);
        numberBalance=findViewById(R.id.textNumberBalance);         options=findViewById(R.id.LayoutOptions);
                                                                    backgroundTouch=findViewById(R.id.viewBackground);
                                                                    WSignOff=findViewById(R.id.LayoutWSignOff);
                                                                    changeMoney=findViewById(R.id.LayoutChangeMoney);

        //received "info"                                                                           FireStore on
        SharedPreferences preset= getSharedPreferences("info", Context.MODE_PRIVATE);         bd=FirebaseFirestore.getInstance();
                                                                                                    openBD=bd.collection("user");
                                                                                                    auth=FirebaseAuth.getInstance();
        welcome.setText(welcome.getText()+" "+preset.getString("name",""));
        numberBalance.setText(preset.getString("balance",""));
        //homework: change type money; euro, peso and yen in to options; custom te logic with switch instead of methods

    }

    //---------------------------------------ads----------------------------------------------------
    public void onClickOptions(View see){
        int value=see.getId();

        if(value==R.id.LayoutWSignOff){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent go = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(go);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    finish();

                    //reset session
                    SharedPreferences preset= getSharedPreferences("info",Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit= preset.edit();
                    edit.putString("balance","");
                    edit.putString("name","");
                    edit.putString("lastName","");
                    edit.putString("numberPhone","");
                    edit.commit();

                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                }
            },1000);
        }else if(value==R.id.LayoutWChangeMoney){
            //options
            changeMoney.setVisibility(View.VISIBLE);
            changeMoney.animate().alpha(1).setDuration(700);
            options.setVisibility(View.VISIBLE);
            backgroundTouch.setVisibility(View.VISIBLE);
            options.animate().alpha(1).setDuration(700);
            backgroundTouch.animate().alpha(1).setDuration(700);
        }
    }
    //view linearLayout "options" and complement exit
    public void options(View see){
        buttonSpawnOptions.animate().alpha(0).setDuration(700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonSpawnOptions.setVisibility(View.GONE);
            }
        },700);

        //options
        options.setVisibility(View.VISIBLE);
        backgroundTouch.setVisibility(View.VISIBLE);
        options.animate().alpha(1).setDuration(700);
        backgroundTouch.animate().alpha(1).setDuration(700);
    }
    public void touchBackground(View see){
        buttonSpawnOptions.setVisibility(View.VISIBLE);
        buttonSpawnOptions.animate().alpha(1).setDuration(700);

        //options
        options.animate().alpha(0).setDuration(700);
        backgroundTouch.animate().alpha(0).setDuration(700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                options.setVisibility(View.GONE);
                backgroundTouch.setVisibility(View.GONE);
            }
        },700);
    }
}