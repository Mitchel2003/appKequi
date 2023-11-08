package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    //type connexion xml
    private EditText name;      private String newName;
    private EditText cc;        private String ccString;    private int newCC;
    private CheckBox check1;
    private CheckBox check2;
    private TextView answer;

    //for apply benefice
    private boolean beneficio=false;
    private String confirma;

    //save
    private String update;
    private int idUser=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name=(EditText)findViewById(R.id.editTextNum1);
        cc=(EditText)findViewById(R.id.editTextNum2);
        check1=(CheckBox)findViewById(R.id.checkBox1);
        check2=(CheckBox)findViewById(R.id.checkBox2);
        answer=(TextView)findViewById(R.id.textViewAnswer);

    }

    //register user
    public void generarRegistro(View see){
        try{
            newName=name.getText().toString();
            ccString=cc.getText().toString();
            newCC=Integer.parseInt(ccString);

            Toast.makeText(this,"doing register",Toast.LENGTH_SHORT).show();

            if(check1.isChecked()==true&&check2.isChecked()==true){
                answer.setText("Has sido seleccionado para recibir los beneficios del programa de victimas, estaremos adjuntando a su cuenta bancaria unas ayudas; mantengase atento");
                onBeneficio();
            }else if(check1.isChecked()==true&&check2.isChecked()==false){
                answer.setText("Eres candidato para recibir un apoyo de vivienda; mantengase atento al telefono");
                onBeneficio();
            }else if(check1.isChecked()==false&&check2.isChecked()==true){
                answer.setText("Usted no aplica para ser beneficiaro del programa de victimas");
                offBeneficio();
            }else if(check1.isChecked()==false&&check2.isChecked()==false){
                answer.setText("Lo sentimos, usted no es potencial candidato a participar en el programa de victimas");
                offBeneficio();
            }

            //initializing AdminSQLite.class and method SQLiteDatabase
            SharedPreferences preset = getSharedPreferences("history", Context.MODE_PRIVATE);
            AdminSQLite user = new AdminSQLite(this, "admin", null, 1);
            SQLiteDatabase bd = user.getWritableDatabase();
            int id = preset.getInt("id", 100);
            int numUsers = preset.getInt("numUsers", 0);

            String nameTable="usuario";
            int cont = user.getCont(nameTable);

            if (cont==0) {//is empty

                SharedPreferences.Editor edit = preset.edit();
                edit.putInt("id", 100 + 1);
                edit.putInt("numUsers", numUsers + 1);
                edit.putBoolean("answerRegister", true);

                edit.commit();//save sync; Changes are written instantly
                //edit.apply();//save async

                //Create user in database
                    // Parse
                    int getId = preset.getInt("id", 100);

                //change to String
                String codigoStr = String.valueOf(getId);
                String getNombre = newName;
                String getCedula = ccString;
                String getBeneficio = returnConfirma();

                //turn on SQL and then send by means of .put
                ContentValues register = new ContentValues();
                register.put("id", codigoStr + "\n");
                register.put("nombre", getNombre + "\n");
                register.put("cedula", getCedula + "\n");
                register.put("beneficio", getBeneficio + "\n");

                //apply changes
                bd.insert("usuario", null, register);
                bd.close();

                Toast.makeText(this, "Register saved", Toast.LENGTH_SHORT).show();

            } else {//not is empty

                SharedPreferences.Editor edit = preset.edit();
                edit.putInt("id", id + 1);
                edit.putInt("numUsers", numUsers + 1);
                edit.putBoolean("answerRegister", true);

                edit.commit();//save sync; Changes are written instantly
                //edit.apply();//save async

                //Create user in database
                    // Parse
                    int getId = preset.getInt("id", 100);

                //change to String
                String codigoStr = String.valueOf(getId);
                String getNombre = newName;
                String getCedula = ccString;
                String getBeneficio = returnConfirma();

                //turn on SQL and then send by means of .put
                ContentValues register = new ContentValues();
                register.put("id", codigoStr + "\n");
                register.put("nombre", getNombre + "\n");
                register.put("cedula", getCedula + "\n");
                register.put("beneficio", getBeneficio + "\n");

                //apply changes
                bd.insert("usuario", null, register);
                bd.close();

                Toast.makeText(this, "Register saved", Toast.LENGTH_SHORT).show();

            }

        }catch(Exception e){
            Toast.makeText(this,"Ingresa los campos requeridos",Toast.LENGTH_SHORT).show();
        }
        name.setText("");
        cc.setText("");
    }

    //data base method
    public void goToDataBaseActivity(View see){
        try{
            SharedPreferences preset=getSharedPreferences("history",Context.MODE_PRIVATE);
            boolean turn=preset.getBoolean("answerRegister",false);

            if(turn){
                Intent go = new Intent(LoginActivity.this, DataBaseActivity.class);
                startActivity(go);
            }else{
                Toast.makeText(this,"DB empty",Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(this,"Error goToDataBase_LoginActivity",Toast.LENGTH_SHORT).show();
        }
    }

    //sources of turn
    public void offBeneficio(){
        beneficio=false;
    }
    public void onBeneficio(){
        beneficio=true;
    }
    public String returnConfirma(){
        if(beneficio){
            confirma="si";
        }else{
            confirma="no";
        }
        return confirma;
    }
}