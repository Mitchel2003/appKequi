package com.wposs.appkequi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    //variables
    private TextView backgroundBlue, backgroundRed,postBackground, login;  private Button entry, createAccount;
    private AutoCompleteTextView email; private TextInputEditText password;
    private TableLayout tableRegister;

    //DataBase Firebase
    private FirebaseFirestore bd;    private CollectionReference openBD;    private DatabaseReference bdReference;
    private FirebaseAuth auth;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//fullScreen

        //View user                                             accessory                                               extras
        email=findViewById(R.id.inEmail);             login = findViewById(R.id.textLogin);                   backgroundBlue = findViewById(R.id.azulLg);
        password=findViewById(R.id.inPassword);               tableRegister = findViewById(R.id.tableRegister);       backgroundRed = findViewById(R.id.rojoLg);
        createAccount=findViewById(R.id.buttonCreateAccount);                                                           postBackground= findViewById(R.id.textViewPostBackground);
        entry=findViewById(R.id.buttonEntry);

        //dataBase on                                           animations
        bd= FirebaseFirestore.getInstance();                    animations();
        openBD=bd.collection("user");
        bdReference= FirebaseDatabase.getInstance().getReference();//read or write
        auth=FirebaseAuth.getInstance();//authentication

        //consult user active
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userConsult();
            }
        });

    }

    //buttons login
    public void entry(View see){
        //save user with sharedPreference
        SharedPreferences preset= getSharedPreferences("info", Context.MODE_PRIVATE);

            String thisEmail, thisPassword;
            thisEmail=email.getText().toString();
            thisPassword=password.getText().toString();

            if(!thisEmail.isEmpty()&&!thisPassword.isEmpty()){

                openBD.whereEqualTo("email",thisEmail).whereEqualTo("password",thisPassword).get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        QuerySnapshot consult = task.getResult();

                        if (consult != null && !consult.isEmpty()) {

                            openBD.whereEqualTo("email", thisEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    String userName = "", userLastName = "", userNumber = "", userCurrency = "";
                                    double userBalance = 0;
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        //getting date of FireStore
                                        userName = document.getString("name");
                                        userLastName = document.getString("lastName");
                                        userNumber = document.getString("numberPhone");
                                        userCurrency = document.getString("currency");
                                        userBalance = document.getDouble("balance");
                                    }

                                    //save and send info of user
                                    int newUserBalance = (int) userBalance;
                                    int way = 0;
                                    SharedPreferences.Editor edit = preset.edit();
                                    edit.putString("balance", String.valueOf(newUserBalance));
                                    edit.putString("name", userName);
                                    edit.putString("lastName", userLastName);
                                    edit.putString("numberPhone", userNumber);
                                    edit.putString("currency", userCurrency);
                                    //compile users
                                    Set<String> userCompilation = preset.getStringSet("userEmail", new HashSet<>());
                                    for (String email : userCompilation) {
                                        if (email.equals(thisEmail)) {
                                            way++;
                                        } else {
                                            //nothing
                                        }
                                    }

                                    if (way != 0) {
                                        userCompilation.add(thisEmail);
                                        edit.putStringSet("userEmail", userCompilation);
                                    }

                                    edit.commit();

                                    Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent go = new Intent(getApplicationContext(), LobbyActivity.class);
                                            startActivity(go);
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                            finish();
                                        }
                                    }, 1000);
                                }
                            });
                        } else {

                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "error task entry_LoginActivity", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                String getEmail=email.getText().toString();
                if (!getEmail.equals("")){
                    openBD.whereEqualTo("email", thisEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            int way = 0;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                String userEmail = document.getString("email");

                                if (!userEmail.isEmpty() && userEmail != null) {
                                    way++;
                                } else {

                                }
                            }
                            if (way != 0) {
                                Toast.makeText(getApplicationContext(), "Password incorrect", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this,"Enter the fields",Toast.LENGTH_SHORT).show();
                }
            }
    }


    public void goToRegister(View see){
        Intent go=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(go);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    private void animations(){
        //animations
        Animation animBlue= AnimationUtils.loadAnimation(this,R.anim.lg_rotate_blue);
        Animation animRed= AnimationUtils.loadAnimation(this,R.anim.lg_rotate_red);
        Animation animDown= AnimationUtils.loadAnimation(this,R.anim.lg_transition_down);

        //run
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postBackground.startAnimation(animDown);    backgroundBlue.startAnimation(animBlue);
                login.startAnimation(animDown);             backgroundRed.startAnimation(animRed);
                tableRegister.startAnimation(animDown);
                email.startAnimation(animDown);
                password.startAnimation(animDown);
                entry.startAnimation(animDown);
                createAccount.startAnimation(animDown);
            }
        },0);

        //run infinite
        rotateIconIzq(backgroundBlue);
        rotateIconDer(backgroundRed);
    }
    //-----------------------------------------ads------------------------------------------------------
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

    private void userConsult(){
        SharedPreferences preset=getSharedPreferences("info",Context.MODE_PRIVATE);
        Set<String> userCompilation=preset.getStringSet("userEmail",new HashSet<>());

        if(!preset.getStringSet("userEmail",new HashSet<>()).isEmpty()&&preset.getStringSet("userEmail",new HashSet<>())!=null){
            //save user
            String list[]=new String[userCompilation.size()];

            for(String email:userCompilation){
                int num=0;
                list[num]=email;
                num++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);

            //keep and view in to AutoCompleteTextView
            email.setAdapter(adapter);

            //interaction
            email.setOnItemClickListener((parent, view, position, id) -> {
                String userEmail=(String) parent.getItemAtPosition(position);

                openBD.whereEqualTo("email",userEmail).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot document: queryDocumentSnapshots){
                            String userPassword= document.getString("password");

                            password.setText(userPassword);
                        }
                    }
                });
            });
        }else{
            //userEmail empty
        }

    }
}
