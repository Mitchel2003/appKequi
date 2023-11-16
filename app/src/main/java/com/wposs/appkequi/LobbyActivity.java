package com.wposs.appkequi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LobbyActivity extends AppCompatActivity {

    private TextView welcome, numberBalance;    private ImageButton buttonSpawnOptions;
                                                private LinearLayout options,changeMoney, WSignOff,WChangeMoney, backgroundTouch;
                                                private RadioGroup optionsChangeMoney; private RadioButton peso,euro,yen;

    //DataBase Firebase Fire Store
    private FirebaseFirestore bd;
    private CollectionReference openBDUser, openBDHistory;
    private FirebaseAuth auth;
    private DocumentReference document;

    //for adapterRecyclerView
    private List<recyclerView> elements;

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
        openBDUser=bd.collection("user");
        openBDHistory=bd.collection("history");
        auth=FirebaseAuth.getInstance();

        history();
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
                    String empty="";
                    edit.putString("balance",empty);
                    edit.putString("name",empty);
                    edit.putString("lastName",empty);
                    edit.putString("numberPhone",empty);
                    edit.putString("currency",empty);
                    //compile user
                    Set<String> userCompilation=preset.getStringSet("userEmail",new HashSet<>());
                    Set<String> newUserCompilation=preset.getStringSet("userEmail",new HashSet<>());
                    for(String email: userCompilation){
                        if(email.equals(preset.getString("email",""))){
                            //nothing
                        }else{
                            newUserCompilation.add(email);
                        }
                    }
                    edit.putStringSet("userEmail",newUserCompilation);
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
    //goToTransfer
    public void goToTransfer(View see){
        Intent go = new Intent(getApplicationContext(), TransferActivity.class);
        startActivity(go);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
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
    //sync sign currency
    private String updateSignCurrency(String name){
        if(name.equals("peso")){
            return "$";
        }else if(name.equals("euro")){
            return "€";
        }else{
            return "¥";
        }
    }
    //sync radioButton
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


    private void history() {
        SharedPreferences preset=getSharedPreferences("info",Context.MODE_PRIVATE);
        String numberPhone= preset.getString("numberPhone","");

        openBDHistory.whereEqualTo(numberPhone,numberPhone).get().addOnCompleteListener(task -> {//search this email in BD, and return the validation
            if (task.isSuccessful()) {//validate sync successfully
                QuerySnapshot consultNumber = task.getResult();

                if(consultNumber!=null&&!consultNumber.isEmpty()){//found

                    DocumentReference documentInitial;
                    documentInitial=openBDHistory.document(numberPhone);//initialize

                    documentInitial.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            final int newId[]=new int[1];
                            DocumentReference documentInitial;
                            documentInitial=openBDHistory.document(numberPhone);//in history_numberPhone

                            openBDHistory.whereEqualTo(numberPhone, numberPhone).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {//get number of elements in to this user(idNumberPhone)
                                int id=0;

                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(QueryDocumentSnapshot history:queryDocumentSnapshots){
                                        if(history.get("status")!=null){
                                            id++;
                                        }
                                    }
                                    newId[0]=id;
                                }
                            });

                            int cont=newId[0];

                                    String nameSend="",numberPhoneSend="",nameReceive="",numberPhoneReceive="",numberSend="",cash="",message="",status="";
                                    String colorGreen="#2026FF00";
                            for(int i=0;i<cont;i++){
                                nameSend=documentSnapshot.getString("nameUserSend");
                                numberPhoneSend=documentSnapshot.getString("numberPhoneUserSend");
                                nameReceive=documentSnapshot.getString("nameUserReceive");
                                numberPhoneReceive=documentSnapshot.getString("numberPhoneUserSend");
                                numberSend=documentSnapshot.getString("numberPhoneUserSend");
                                cash=documentSnapshot.getString("cash");
                                message=documentSnapshot.getString("message");
                                status=documentSnapshot.getString("status");
                            }
                            newElement(colorGreen,nameSend,numberPhoneSend,cash,message,status);

                            AdapterRecyclerView list=new AdapterRecyclerView(elements,getApplicationContext());
                            RecyclerView view=findViewById(R.id.listRecyclerView);
                            view.setHasFixedSize(true);
                            view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            view.setAdapter(list);
                        }
                    });

                }else{//not found

                }

            }else{//task not successfully

            }
        });




    }


    public void newElement(String color, String name, String numberPhone, String cash, String message, String status){
        elements=new ArrayList<>();
        elements.add(new recyclerView(color,name,numberPhone,cash,message,status));


    }
}