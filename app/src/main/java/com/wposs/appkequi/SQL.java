package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SQL extends AppCompatActivity {

    private EditText id;
    private TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        id = (EditText) findViewById(R.id.editTextId);
        answer = (TextView) findViewById(R.id.textViewAnswerGestionBD);
    }

    public void search(View see) {

        AdminSQLite user = new AdminSQLite(this, "admin", null, 1);
        SQLiteDatabase bd = user.getWritableDatabase();

        String codigo = id.getText().toString();

        if (!codigo.isEmpty()) {
            //tool for select and get - rawQuery is a consult
            Cursor select = bd.rawQuery("select nombre, cedula, beneficio from usuario where id=" + codigo, null);

            if (select.moveToFirst()) {//moveToFirs evaluates if contain dates = if true=true
                String textSearch = "Nombre: "+select.getString(0) +"Cedula: "+ select.getString(1) +"\nBeneficiario: "+ select.getString(2);
                answer.setText(textSearch);
                bd.close();
                select.close();
            } else {
                Toast.makeText(this, "No existe el usuario", Toast.LENGTH_SHORT).show();
                bd.close();
                select.close();
            }
        } else {
            Toast.makeText(this, "Id empty, please write the id", Toast.LENGTH_SHORT).show();
        }
    }

    public void delete(View see) {
        //initializing SharedPreferences and getting date "numUsers"(quantity of users) && "id"(content last id)
        //initializing AdminSQLite.class and method SQLiteDatabase
        SharedPreferences preset=getSharedPreferences("history", Context.MODE_PRIVATE);
        AdminSQLite user = new AdminSQLite(this, "admin", null, 1);
        SQLiteDatabase bd = user.getWritableDatabase();
        int refreshId=preset.getInt("id",100);
        int numUsers= preset.getInt("numUsers",0);

        //get num enter on the screen
        String idUser = id.getText().toString();
        int idUserNum=Integer.parseInt(idUser);

        if (!idUser.isEmpty()) {

            if(idUserNum==refreshId) {
                int numDeleted = bd.delete("usuario", "id=" + idUser, null);//is int because it return the number of deleted indexes
                bd.close();
                id.setText("");
                answer.setText(" . . . ");

                if (numDeleted == 1) {
                    Toast.makeText(this, "Successfully Eliminated", Toast.LENGTH_SHORT).show();
                    int newNum=refreshId-1;
                    int newCant=numUsers-1;
                    SharedPreferences.Editor edit=preset.edit();
                    edit.putInt("id",newNum);
                    edit.putInt("numUsers",newCant);
                    edit.commit();
                } else {
                    Toast.makeText(this, "El usuario no existe /delete_SQLActivity", Toast.LENGTH_SHORT).show();
                }
            }else{
                int numDeleted = bd.delete("usuario", "id=" + idUser, null);//is int because it return the number of deleted indexes
                bd.close();
                id.setText("");
                answer.setText(" . . . ");

                if (numDeleted == 1) {
                    Toast.makeText(this, "Successfully Eliminated", Toast.LENGTH_SHORT).show();
                    int newCant=numUsers-1;
                    SharedPreferences.Editor edit=preset.edit();
                    edit.putInt("numUsers",newCant);
                    edit.commit();
                } else {
                    Toast.makeText(this, "El usuario no existe /delete_SQLActivity", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "ID empty", Toast.LENGTH_SHORT).show();
        }
    }

    //back to BaseDateActivity
    public void goToDBActivity(View see){
        Intent go=new Intent(this,DataBaseActivity.class);
        startActivity(go);
    }
}