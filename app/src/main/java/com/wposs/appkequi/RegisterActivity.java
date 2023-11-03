package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    //regularExpressions for password
    private static final String passwordRegularExpressions = "^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z]).{4,8}$";

    //layout
    private TextView textRegister, postBackground;
    private ImageButton buttonBack;                                 private Button buttonValidate;
    private ScrollView table;
    private EditText name, lastName, confirmPassword, document;
    private TextInputLayout email, password;                        private TextInputEditText inEmail, inPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //layout                                                    components                                              colors
        name=findViewById(R.id.editName);                           textRegister=findViewById(R.id.textRegister);           int error=getResources().getColor(R.color.error);       int basicTint=getResources().getColor(R.color.colorFrom);
        lastName=findViewById(R.id.editLastName);                   buttonBack=findViewById(R.id.buttonBack);               ColorStateList red=ColorStateList.valueOf(error);       ColorStateList standTint=ColorStateList.valueOf(basicTint);
        email=findViewById(R.id.editEmail);                         buttonValidate=findViewById(R.id.buttonValidate);       int basicColorHint= getResources().getColor(R.color.colorT2);
        password=findViewById(R.id.editPassword);                   table=findViewById(R.id.scrollView);                    ColorStateList standColorHint=ColorStateList.valueOf(basicColorHint);
        inEmail=findViewById(R.id.inEmail);                         postBackground=findViewById(R.id.textPostBackground);
        inPassword=findViewById(R.id.inPassword);
        confirmPassword=findViewById(R.id.editConfirmPassword);
        document=findViewById(R.id.editDocument);

        animations();

    }

    public void validate(){
        try {

            //converting layout in string                                                                       colors
            String tempName, tempLastName, tempEmail, tempPassword, tempConfirmPassword, tempDocument;          int error=getResources().getColor(R.color.error);       int basicTint=getResources().getColor(R.color.colorFrom);
            tempName=name.getText().toString();                                                                 ColorStateList red=ColorStateList.valueOf(error);       ColorStateList standTint=ColorStateList.valueOf(basicTint);
            tempLastName=lastName.getText().toString();                                                         int basicColorHint= getResources().getColor(R.color.colorT2);
            tempEmail=email.getResources().toString();                                                               ColorStateList standColorHint=ColorStateList.valueOf(basicColorHint);
            tempPassword=password.getResources().toString();
            tempConfirmPassword=confirmPassword.getText().toString();
            tempDocument=document.getText().toString();

            if(!tempName.isEmpty()&&!tempLastName.isEmpty()&&!tempEmail.isEmpty()&&!tempPassword.isEmpty()&&!tempConfirmPassword.isEmpty()&&!tempDocument.isEmpty()){
                if (!tempConfirmPassword.contentEquals(tempPassword)) {//alternative equals ignore case()

                    confirmPassword.setBackgroundTintList(red);
                    confirmPassword.setHintTextColor(red);
                    confirmPassword.setText("");

                    Toast.makeText(this, "the password not is equals, please retry", Toast.LENGTH_SHORT).show();
                }else {
                    confirmPassword.setBackgroundTintList(standTint);
                    confirmPassword.setHintTextColor(standColorHint);
                    confirmPassword.setHint(R.string.editConfirmPassword);
                }

                if(tempEmail.contains("@")){

                }else{

                }

            }else{
                if(tempName.isEmpty()){
                    name.setBackgroundTintList(red);
                    name.setHintTextColor(red);
                }if(tempLastName.isEmpty()){
                    lastName.setBackgroundTintList(red);
                    lastName.setHintTextColor(red);
                }if(tempEmail.isEmpty()){
                    email.setBackgroundTintList(red);
                    email.setHintTextColor(red);
                }if(tempPassword.isEmpty()){
                    password.setBackgroundTintList(red);
                    password.setHintTextColor(red);
                }if(tempConfirmPassword.isEmpty()){
                    confirmPassword.setBackgroundTintList(red);
                    confirmPassword.setHintTextColor(red);
                }if(tempDocument.isEmpty()){
                    document.setBackgroundTintList(red);
                    document.setHintTextColor(red);
                }
                Toast.makeText(this,"Characters obligatory",Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e) {
            Toast.makeText(this, "Error validate_RegisterActivity", Toast.LENGTH_SHORT).show();
        }
    }



    //-------------------------------------------------ads------------------------------------------
    public void goToBack(View see){
        Intent go=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(go);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }


    //result of validations
    private void result(){
        //return through a array boolean
        boolean [] equalsValidate={validationEmail(),validationPassword()};//in "{}" the first value "index 0" is the return of method validationEmail; after the comma is the next index

        if(!equalsValidate.equals(0)||!equalsValidate.equals(1)) {

        }else {
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    //Validation email and password
    private boolean validationEmail(){
        //of textInputEditText to string                            multiChar
        String tempEmail=inEmail.getResources().toString();         String case1="This site can´t been empty";
                                                                    String case2="Please, enter a valid email";
        if(tempEmail.isEmpty()) {
            email.setError(case1);
            return false;

        }else if(!PatternsCompat.EMAIL_ADDRESS.matcher(tempEmail).matches()) {// compare and return true if there is a match with regular expressions
            email.setError(case2);
            return false;

        }else {
            email.setError(null);
            return true;
        }
    }
    private boolean validationPassword(){
        //of textInputEditText to string                                multiChar
        String tempPassword=inPassword.getResources().toString();       String case1="This site can´t been empty";
                                                                        String case2="Password is too weak";
        //class pattern
        Pattern patron=Pattern.compile(passwordRegularExpressions);

        if(tempPassword.isEmpty()) {
            password.setError(case1);
            return false;
        }else if(!patron.matcher(tempPassword).matches()/*matches return boolean; says if is equalizer or not*/) {// compare and return true if matches the regular expressions with the passwordRegularExpressions; this case is negation"!"
            password.setError(case2);
            return false;
        }else {
            password.setError(null);
            return true;
        }
    }


    //animations
    public void animations(){
        Animation down= AnimationUtils.loadAnimation(this,R.anim.rg_transition_down);

        textRegister.startAnimation(down);
        buttonBack.startAnimation(down);
        buttonValidate.startAnimation(down);
        table.startAnimation(down);
        postBackground.startAnimation(down);

    }

}