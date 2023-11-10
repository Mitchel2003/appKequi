package com.wposs.appkequi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class LobbyActivity extends AppCompatActivity {

    private TextView welcome, numberBalance;    private ImageButton buttonSpawnOptions;
                                                private LinearLayout options,changeMoney, WSignOff,WChangeMoney, backgroundTouch;
                                                private RadioGroup optionsChangeMoney; private RadioButton peso,euro,yen;

    //DataBase Firebase Fire Store
    private FirebaseFirestore bd;
    private CollectionReference openBD;
    private FirebaseAuth auth;
    private DocumentReference document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //View user                                                 accessory                                                   extras
        welcome=findViewById(R.id.textWelcome);                     buttonSpawnOptions=findViewById(R.id.buttonOptions);        peso=findViewById(R.id.radioButtonPeso);
        numberBalance=findViewById(R.id.textNumberBalance);         options=findViewById(R.id.LayoutOptions);                   euro=findViewById(R.id.radioButtonEuro);
                                                                    backgroundTouch=findViewById(R.id.LayoutBackground);        yen=findViewById(R.id.radioButtonYen);
                                                                    WSignOff=findViewById(R.id.LayoutWSignOff);
                                                                    WChangeMoney= findViewById(R.id.LayoutWChangeMoney);
                                                                    changeMoney=findViewById(R.id.LayoutChangeMoney);
                                                                    optionsChangeMoney=findViewById(R.id.radioGroup);

        //FireStore on
        bd=FirebaseFirestore.getInstance();
        openBD=bd.collection("user");
        auth=FirebaseAuth.getInstance();

        updateDataUser();
    }

    //linearLayout "LayoutOptions" and complement with backgroundTouch exit
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
    //OnClick for LayoutLayout "Horizontal" that contain the text
    public void onClickOptions(View see){
        int value=see.getId();

        /*sign off*/
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

        }/*change money*/else if(value==R.id.LayoutWChangeMoney){

            //spawn
            changeMoney.setVisibility(View.VISIBLE);
            changeMoney.animate().alpha(1).setDuration(700);
            buttonSpawnOptions.setVisibility(View.VISIBLE);
            buttonSpawnOptions.animate().alpha(1).setDuration(700);

            //remove view options
            options.animate().alpha(0).setDuration(700);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    options.setVisibility(View.GONE);
                }
            }, 700);

            //action radioButtons
            optionsChangeMoney.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    SharedPreferences preset = getSharedPreferences("info", Context.MODE_PRIVATE);
                    String id = preset.getString("numberPhone", "");
                    String userCurrency = preset.getString("currency", "");
                    String userBalance = preset.getString("balance", "");

                    if (checkedId != -1) {
                        if (peso.isChecked()) {
                            String currency = "peso";

                            if (userCurrency.equals("peso")) {
                                //nothing
                            } else if (userCurrency.equals("euro")) {
                                //convert euro to peso
                                double oldBalance = (Integer.parseInt(userBalance)) / 4;
                                int newBalance=(int)oldBalance;
                                updateCurrency(newBalance, currency, id);
                            } else {
                                //convert yen to peso
                                double oldBalance = (Integer.parseInt(userBalance)) / 2;
                                int newBalance = (int)oldBalance;
                                updateCurrency(newBalance, currency, id);
                            }
                        } else if (euro.isChecked()) {
                            String currency = "euro";

                            if (userCurrency.equals("peso")) {
                                //convert peso to euro
                                double oldBalance = (Integer.parseInt(userBalance)) * 4;
                                int newBalance=(int)oldBalance;
                                updateCurrency(newBalance, currency, id);
                            } else if (userCurrency.equals("euro")) {
                                //nothing
                            } else {
                                //convert yen to euro
                                double oldBalance = (Integer.parseInt(userBalance)) * 2;
                                int newBalance=(int)oldBalance;
                                updateCurrency(newBalance, currency, id);
                            }
                        } else {//yen
                            String currency = "yen";

                            if (userCurrency.equals("peso")) {
                                //convert peso to yen
                                double oldBalance = (Integer.parseInt(userBalance)) * 2;
                                int newBalance = (int)oldBalance;
                                updateCurrency(newBalance, currency, id);
                            } else if (userCurrency.equals("euro")) {
                                //convert euro to yen
                                double oldBalance = (Integer.parseInt(userBalance)) / 2;
                                int newBalance=(int)oldBalance;
                                updateCurrency(newBalance, currency, id);
                            } else {
                                //nothing
                            }
                        }
                        //remove view LayoutChangeMoney
                        changeMoney.animate().alpha(0).setDuration(700);
                        backgroundTouch.animate().alpha(0).setDuration(700);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                changeMoney.setVisibility(View.GONE);
                                backgroundTouch.setVisibility(View.GONE);
                            }
                        }, 700);
                        updateDataUser();

                    }
                }
            });
        }
    }





    //---------------------------------------ads----------------------------------------------------
    //set currency
    private void updateCurrency(int balance, String currency, String id) {
        Map<String, Object> map=new HashMap<>();
        map.put("currency",currency);
        map.put("balance",balance);

        //update too SharedPreferences
        SharedPreferences preset=getSharedPreferences("info",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preset.edit();
        edit.putString("currency",currency);
        edit.putString("balance",String.valueOf(balance));
        edit.commit();

        bd.collection("user").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Update failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //action touchBackground
    public void touchBackground(View see){
        buttonSpawnOptions.setVisibility(View.VISIBLE);
        buttonSpawnOptions.animate().alpha(1).setDuration(700);

        //options
        options.animate().alpha(0).setDuration(700);
        backgroundTouch.animate().alpha(0).setDuration(700);
        changeMoney.animate().alpha(0).setDuration(700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                options.setVisibility(View.GONE);
                backgroundTouch.setVisibility(View.GONE);
                changeMoney.setVisibility(View.GONE);
            }
        },700);
    }

    //sync data user
    private void updateDataUser(){
        //get info
        SharedPreferences preset= getSharedPreferences("info", Context.MODE_PRIVATE);

        String sign=updateSignCurrency(preset.getString("currency",""));
        numberBalance.setText(sign+" "+preset.getString("balance",""));
        welcome.setText("Welcome sr. "+preset.getString("name",""));

        updateRadioButton();
    }
    //set sign currency
    private String updateSignCurrency(String name){
        if(name.equals("peso")){
            return "$";
        }else if(name.equals("euro")){
            return "€";
        }else{
            return "¥";
        }
    }
    //update radioButton for sync
    private void updateRadioButton(){
        SharedPreferences preset=getSharedPreferences("info",Context.MODE_PRIVATE);

        String data=preset.getString("currency","");
        if(data.equals("peso")){
            peso.setChecked(true);
        }else if(data.equals("euro")){
            euro.setChecked(true);
        }else{
            yen.setChecked(true);
        }
    }
}