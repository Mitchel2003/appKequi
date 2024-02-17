package com.wposs.appkequi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //variables                                                                                                     
    private TextInputLayout name, lastName, email, password, confirmPassword, numberPhone;                          
    private TextInputEditText inName, inLastName, inEmail, inPassword, inConfirmPassword, inNumberPhone;            
    private final int money= 1000000;
    private final String currency="peso";
    
    //layout
    private TextView textRegister, postBackground;
    private ImageButton buttonBack;         
    private Button buttonValidate;
    private ScrollView table;
    
    //DataBase FireBase
    private FirebaseFirestore bd;           
    private CollectionReference openBD;     
    private FirebaseAuth auth;
    private DocumentReference document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//fullScreen

        //layout                                                    
        name=findViewById(R.id.editName);                           
        inName=findViewById(R.id.inName);                           
        lastName=findViewById(R.id.editLastName);                   
        inLastName=findViewById(R.id.inLastName);                   
        email=findViewById(R.id.editEmail);                         
        inEmail=findViewById(R.id.inEmail);
        password=findViewById(R.id.editPassword);
        inPassword=findViewById(R.id.inPassword);
        confirmPassword=findViewById(R.id.editConfirmPassword);
        inConfirmPassword=findViewById(R.id.inConfirmPassword);
        numberPhone=findViewById(R.id.editNumberPhone);
        inNumberPhone=findViewById(R.id.inNumberPhone);

        //components
        textRegister=findViewById(R.id.textRegister);
        buttonBack=findViewById(R.id.buttonOptions);
        buttonValidate=findViewById(R.id.buttonValidate);
        table=findViewById(R.id.scrollView);
        postBackground=findViewById(R.id.textPostBackground);
        
        //dataBase on                                  
        bd=FirebaseFirestore.getInstance();     
        openBD=bd.collection("user");
        auth=FirebaseAuth.getInstance();
        
        animations();
        
        onChangeText();
    }
    
    //------------------------------------------------------------functions principals------------------------------------------------------------
    //button validate
    public void validate(View see){
        try {
            //converting layout in string
            String tempName, tempLastName, tempEmail, tempPassword, tempConfirmPassword, tempNumberPhone;
            tempName=inName.getText().toString();
            tempLastName=inLastName.getText().toString();
            tempEmail=inEmail.getText().toString();
            tempPassword=inPassword.getText().toString();
            tempConfirmPassword=inConfirmPassword.getText().toString();
            tempNumberPhone=inNumberPhone.getText().toString();


            if(!tempName.isEmpty()&&!tempLastName.isEmpty()&&!tempEmail.isEmpty()&&!tempPassword.isEmpty()&&!tempConfirmPassword.isEmpty()&&!tempNumberPhone.isEmpty()){
                if (tempConfirmPassword.contentEquals(tempPassword)) {//alternative equals ignore case()

                    openBD.whereEqualTo("numberPhone",tempNumberPhone).get().addOnCompleteListener(task -> {

                        if(task.isSuccessful()){
                            QuerySnapshot consult=task.getResult();

                            if(consult!=null&&!consult.isEmpty()){
                                Toast.makeText(this, "Sorry, this numberPhone already exist", Toast.LENGTH_SHORT).show();
                            }else{
                                putDate(tempName, tempLastName, tempEmail, tempPassword, tempNumberPhone);
                                Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
                                //clean errors
                                inName.setError(null);inLastName.setError(null);inEmail.setError(null);inPassword.setError(null);inConfirmPassword.setError(null);inNumberPhone.setError(null);

                                Intent go=new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(go);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                finish();
                            }
                        }else{
                            Toast.makeText(this, "error task validate_RegisterActivity", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    //confirmPassword
                    Toast.makeText(this, "the password not is equals, please retry", Toast.LENGTH_SHORT).show();
                    confirmPassword.setError("not is equals");
                }
            }else{
                String error="empty";
                if(tempName.isEmpty()){
                    name.setError(error);
                }if(tempLastName.isEmpty()){
                    lastName.setError(error);
                }if(tempEmail.isEmpty()){
                    email.setError(error);
                }if(tempPassword.isEmpty()){
                    password.setError(error);
                }if(tempConfirmPassword.isEmpty()){
                    confirmPassword.setError(error);
                }if(tempNumberPhone.isEmpty()){
                    numberPhone.setError(error);
                }
                Toast.makeText(this,"Characters obligatory",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            Toast.makeText(this, "Error validate_RegisterActivity", Toast.LENGTH_SHORT).show();
        }
    }

    //button back
    public void goToBack(View see){
        Intent go=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(go);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    
    //------------------------------------------------------------tools------------------------------------------------------------
    //textWatcher
    public void onChangeText(){
    	inEmail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validationEmail();
                }

        });
        inPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validationPassword();
            }
        });
    }
    //Validation email and password
    private void validationEmail(){
        //of textInputEditText to string
        String tempEmail=inEmail.getText().toString();
        
        String case1="This site can´t been empty";
        String case2="Please, enter a valid email";
        
        if(tempEmail.isEmpty()) {
            email.setError(case1);
        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(tempEmail).matches()) {// compare and return true if there is a match with regular expressions
            email.setError(case2);
        }else {
            email.setError(null);
        }
    }
    private void validationPassword(){
        //of textInputEditText to string                                
        String tempPassword=inPassword.getText().toString();            

        String case1="This site can´t been empty";
        String case2="Password is too weak";
        
        if(tempPassword.isEmpty()) {
            password.setError(case1);
        }else if(tempPassword.length()<4) {
            password.setError(case2);
        }else {
            password.setError(null);
        }
    }
    
    
    //send to FireStore
    private void putDate(String name, String lastName, String email, String password, String numberPhone){
        //create documentReference with bd "user" and a id specific
        document=bd.collection("user").document(numberPhone);

        //create a map for compilation of values
        Map<String, Object> map= new HashMap<>();
        map.put("name",name);
        map.put("lastName", lastName);
        map.put("email",email);
        map.put("password",password);
        map.put("numberPhone",numberPhone);
        map.put("balance",money);
        map.put("currency",currency);

        //in "user" with id personalized, we keep the map and send with "add" to bd
        document.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {                                                    //bd.collection("user").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override                                                                                                             //    @Override
            public void onSuccess(Void unused) {//if completed                                                                    //    public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RegisterActivity.this,"User saved successfully",Toast.LENGTH_SHORT).show();            //        Toast.makeText(getApplicationContext(),"Saved successfully",Toast.LENGTH_SHORT).show();
            }                                                                                                                     //    }
        }).addOnFailureListener(new OnFailureListener() {                                                                         //}).addOnFailureListener(new OnFailureListener() {
            @Override                                                                                                             //    @Override
            public void onFailure(@NonNull Exception e) {//if failed                                                              //    public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this,"Work not completed",Toast.LENGTH_SHORT).show();                 //        Toast.makeText(getApplicationContext(),"Error Database",Toast.LENGTH_SHORT).show();
            }                                                                                                                     //    }
        });                                                                                                                       //});

    }
    
    
    //------------------------------------------------------------ads------------------------------------------------------------
    //animations spawn
    public void animations(){
        Animation down= AnimationUtils.loadAnimation(this,R.anim.rg_transition_down);

        textRegister.startAnimation(down);
        buttonBack.startAnimation(down);
        buttonValidate.startAnimation(down);
        table.startAnimation(down);
        postBackground.startAnimation(down);
    }
    
    

}