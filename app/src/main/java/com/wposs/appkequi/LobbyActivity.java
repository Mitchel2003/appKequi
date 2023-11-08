package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LobbyActivity extends AppCompatActivity {

    private ImageButton buttonSpawnOptions;
    private LinearLayout options;
    private View backgroundTouch;

    //DataBase Firebase Fire Store
    private FirebaseFirestore bd;
    private CollectionReference openBD;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        buttonSpawnOptions=findViewById(R.id.buttonOptions);
        options=findViewById(R.id.LayoutOptions);
        backgroundTouch=findViewById(R.id.viewBackground);

        bd=FirebaseFirestore.getInstance();
        openBD=bd.collection("user");
        auth=FirebaseAuth.getInstance();

    }









    //---------------------------------------ads----------------------------------------------------
    public void signOff(View see){

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