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
	//view info
    private TextView welcome, numberBalance;
    
    //actions
    private ImageButton buttonSpawnOptions;
    private LinearLayout options,changeMoney, WSignOff,WChangeMoney, backgroundTouch;
    private RadioGroup optionsChangeMoney; 
    private RadioButton peso,euro,yen;

    //on BD
    private FirebaseFirestore bd;
    private CollectionReference openBD;
    private FirebaseAuth auth;
    private DocumentReference document;

    //for adapterRecyclerView
    private List<recyclerView> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        //View user                                                                                                   
        welcome=findViewById(R.id.textWelcome);                            
        numberBalance=findViewById(R.id.textNumberBalance);                          
                                                            
        //accessory 
        buttonSpawnOptions=findViewById(R.id.buttonOptions);
        options=findViewById(R.id.LayoutOptions);  
        backgroundTouch=findViewById(R.id.LayoutBackground);        
        WSignOff=findViewById(R.id.LayoutWSignOff);
        WChangeMoney= findViewById(R.id.LayoutWChangeMoney);
        changeMoney=findViewById(R.id.LayoutChangeMoney);
        optionsChangeMoney=findViewById(R.id.radioGroup);
                                                                                                                                 
        //extras                                                            
        peso=findViewById(R.id.radioButtonPeso);
        euro=findViewById(R.id.radioButtonEuro);
        yen=findViewById(R.id.radioButtonYen);
                                                                    
        //FireStore on
        bd=FirebaseFirestore.getInstance();
        openBD=bd.collection("user");
        auth=FirebaseAuth.getInstance();

        history();
        updateDataUser();
    }
    
    //------------------------------------------------------------functions principals------------------------------------------------------------
    //linearLayout "LayoutOptions" and complement with backgroundTouch
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
    
    //actions OnClick
    public void onClickOptions(View see){
        int value=see.getId();

        if(value==R.id.LayoutWSignOff){//sign off
        	
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

        }else if(value==R.id.LayoutWChangeMoney){//change money

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

                    actionOnCheckedItem(checkedId,id,userCurrency,userBalance);
                }
            });
        }
    }
    
    //go to TransferActivity "sendCash"
    public void goToTransfer(View see){
        Intent go = new Intent(getApplicationContext(), TransferActivity.class);
        startActivity(go);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }



    
    //------------------------------------------------------------tools------------------------------------------------------------
    //action changeCurrency
    public void actionOnCheckedItem(int checkedId, String userId, String userCurrency, String userBalance){//onItemClick of RadioGroup

        if (checkedId != -1) {
            if (peso.isChecked()) {//PESO
                String currencyContext = "peso";

                if (userCurrency.equals("peso")) {
                    //nothing
                } else if (userCurrency.equals("euro")) {
                    //convert EURO to PESO
                    double oldBalance = (Integer.parseInt(userBalance)) / 4;
                    int newBalance=(int)oldBalance;
                    updateCurrency(newBalance, currencyContext, userId);
                } else {
                    //convert YEN to PESO
                    double oldBalance = (Integer.parseInt(userBalance)) / 2;
                    int newBalance = (int)oldBalance;
                    updateCurrency(newBalance, currencyContext, userId);
                }
            } else if (euro.isChecked()) {//EURO
                String currencyContext = "euro";

                if (userCurrency.equals("peso")) {
                    //convert PESO to EURO
                    double oldBalance = (Integer.parseInt(userBalance)) * 4;
                    int newBalance=(int)oldBalance;
                    updateCurrency(newBalance, currencyContext, userId);
                } else if (userCurrency.equals("euro")) {
                    //nothing
                } else {
                    //convert YEN to EURO
                    double oldBalance = (Integer.parseInt(userBalance)) * 2;
                    int newBalance=(int)oldBalance;
                    updateCurrency(newBalance, currencyContext, userId);
                }
            } else {//YEN
                String currencyContext = "yen";

                if (userCurrency.equals("peso")) {
                    //convert PESO to YEN
                    double oldBalance = (Integer.parseInt(userBalance)) * 2;
                    int newBalance = (int)oldBalance;
                    updateCurrency(newBalance, currencyContext, userId);
                } else if (userCurrency.equals("euro")) {
                    //convert EURO to YEN
                    double oldBalance = (Integer.parseInt(userBalance)) / 2;
                    int newBalance=(int)oldBalance;
                    updateCurrency(newBalance, currencyContext, userId);
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
    private void updateCurrency(int balance, String currency, String id) {//set currency
        
        //update too SharedPreferences
        SharedPreferences preset=getSharedPreferences("info",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preset.edit();
        edit.putString("currency",currency);
        edit.putString("balance",String.valueOf(balance));
        edit.commit();
        
        //update too FireBase
        Map<String, Object> map=new HashMap<>();
        map.put("currency",currency);
        map.put("balance",balance);

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
    private void updateDataUser(){//action sync data user
        SharedPreferences preset= getSharedPreferences("info", Context.MODE_PRIVATE);

        String sign=signCurrency(preset.getString("currency",""));
        numberBalance.setText(sign+" "+preset.getString("balance",""));
        welcome.setText("Welcome sr. "+preset.getString("name",""));

        syncRadioButton();
    }
    private String signCurrency(String name){//method for sign currency
        if(name.equals("peso")){
            return "$";
        }else if(name.equals("euro")){
            return "€";
        }else{
            return "¥";
        }
    }
    private void syncRadioButton(){//method for sync radioButton
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
    
    //scanner and view "history"
    private void history() {//query size of history "in context" and create the cardView for each transference (debugging...)
        SharedPreferences preset=getSharedPreferences("info",Context.MODE_PRIVATE);
        String numberPhone= preset.getString("numberPhone","");
        
        //tool collection and reference
        CollectionReference openBDUserContext=openBD.document(numberPhone).collection("history"); 
        DocumentReference document=openBDUserContext.document("history");

        openBD.whereEqualTo(numberPhone,numberPhone).get().addOnCompleteListener(task -> {
        	
            if (task.isSuccessful()) {//validate sync successfully
                QuerySnapshot consult = task.getResult();

                if(consult!=null&&!consult.isEmpty()){//number found
                	
                    //get number the elements that contain this history "numberPhone"
                	int contHistory=openBDUserContext.document("history").size();
                	
                	if(contHistory!=-1) {//history with something "two way, please validate"
                		
                		int num=1;
                		
                		for(int i=0;i<contHistory;i++) {
                		
                			openBDUserContext.whereEqualTo(num,num).get().addOnSuccessListener (new OnSuccessListener<QuerySnapshot>() {
                				@Override
                				public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                					
                					String nameSend="",numberSend="",nameReceive="",numberReceive="",cash="",message="",status="";
                                    String colorGreen="#2026FF00";
                			
                					for(QueryDocumentSnapshot document:queryDocumentSnapshots) {
                					
                						nameSend=history.getString("nameUserSend");
                						numberSend=history.getString("numberPhoneUserSend");
                						nameReceive=history.getString("nameUserReceive");
                						numberReceive=history.getString("numberPhoneUserReceive");
                						cash=history.getString("cash");
                						message=history.getString("message");
                						status=history.getString("status");	
            						
                						newElement(colorGreen,nameSend,numberPhoneSend,cash,message,status);
                					}
                				
                				}
                			
                			
                			});
                			
                			num++;
                		}
                		
                		//---------------------------------or---------------------------------------------
                		
                		document.get().addOnSuccessListener(new OnSuccessListener <DocumentSnapshot>() {
                			
                			@Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                				
                    			String nameSend="",numberSend="",nameReceive="",numberReceive="",cash="",message="",status="";
                                String colorGreen="#2026FF00";
                				
                				for(QueryDocumentSnapshot history: documentSnapshot ) {
                					
                					for(int i=0;i<contHistory;i++) {
                						
                						nameSend=history.getString("nameUserSend");
                						numberSend=history.getString("numberPhoneUserSend");
                						nameReceive=history.getString("nameUserReceive");
                						numberReceive=history.getString("numberPhoneUserReceive");
                						cash=history.getString("cash");
                						message=history.getString("message");
                						status=history.getString("status");	
                                    
                						newElement(colorGreen,nameSend,numberPhoneSend,cash,message,status);
                					}
                					
                					AdapterRecyclerView list=new AdapterRecyclerView(elements,getApplicationContext());
                					RecyclerView view=findViewById(R.id.listRecyclerView);
                					view.setHasFixedSize(true);
                					view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                					view.setAdapter(list);
                				}
                			}
                		});
                		
                	}else {//history empty
                		
                	}

                }else{//number not found

                }

            }else{//task not successfully

            }
        });

    }
    public void newElement(String color, String name, String numberPhone, String cash, String message, String status){//method for create one cardView
        elements=new ArrayList<>();
        elements.add(new recyclerView(color,name,numberPhone,cash,message,status));
    }
    
    
    
    //------------------------------------------------------------ads------------------------------------------------------------  
    //action touchBackground
    public void touchBackground(View see){//viewTouchBackground for user
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


    
}