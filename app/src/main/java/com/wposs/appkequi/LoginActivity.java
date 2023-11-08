package com.wposs.appkequi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    //variables
    private TextView backgroundBlue, backgroundRed,postBackground, login;  private Button entry, createAccount;
    private EditText email, password;
    private TableLayout tableRegister;

    //DataBase Firebase
    private FirebaseFirestore bd;
    private CollectionReference openBD;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//fullScreen

        //layout
        email=findViewById(R.id.editTextEmail);             login = findViewById(R.id.textLogin);                   backgroundBlue = findViewById(R.id.azulLg);
        password=findViewById(R.id.editTextPassword);       tableRegister = findViewById(R.id.tableRegister);       backgroundRed = findViewById(R.id.rojoLg);
        createAccount=findViewById(R.id.buttonCreateAccount);                                                     postBackground= findViewById(R.id.textViewPostBackground);
        entry=findViewById(R.id.buttonEntry);

        //dataBase on                               animations
        bd= FirebaseFirestore.getInstance();        animations();
        openBD=bd.collection("user");
        auth=FirebaseAuth.getInstance();

    }

    //buttons login
    public void entry(View see){

            String thisEmail, thisPassword;
            thisEmail=email.getText().toString();
            thisPassword=password.getText().toString();

            if(!thisEmail.isEmpty()&&!thisPassword.isEmpty()){

                openBD.whereEqualTo("email",thisEmail).whereEqualTo("password",thisPassword).get().addOnCompleteListener(task -> {

                   if(task.isSuccessful()){
                       QuerySnapshot consult=task.getResult();

                       if(consult!=null&&!consult.isEmpty()){

                           Toast.makeText(LoginActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                           new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   Intent go=new Intent(getApplicationContext(),LobbyActivity.class);
                                   startActivity(go);
                                   overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                   finish();
                               }
                           },1000);

                       }else{
                           Toast.makeText(LoginActivity.this,"Sorry, you are not registered",Toast.LENGTH_SHORT).show();
                       }
                   }else{
                       Toast.makeText(LoginActivity.this,"error task entry_LoginActivity",Toast.LENGTH_SHORT).show();
                   }
                });

            }else{
                Toast.makeText(this,"Enter the fields",Toast.LENGTH_SHORT).show();
            }

    }
    public void register(View see){
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
