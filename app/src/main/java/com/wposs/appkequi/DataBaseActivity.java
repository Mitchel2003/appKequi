package com.wposs.appkequi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DataBaseActivity extends AppCompatActivity {

    private TextView table;
    private static boolean turn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        table=(TextView) findViewById(R.id.textViewBD);

        //initializing SharedPreferences and getting date "numUsers" (quantity of users)
        //initializing AdminSQLite.class and method SQLiteDatabase
        SharedPreferences preset=getSharedPreferences("history",Context.MODE_PRIVATE);
        AdminSQLite user = new AdminSQLite(this, "admin", null, 1);
        SQLiteDatabase bd = user.getWritableDatabase();
        int numUsers=preset.getInt("numUsers",0);
        int numAgree=100;


            for (int r = 0; r <= numUsers; r++) {
                //tool for select and get - rawQuery is a consult
                Cursor select = bd.rawQuery("select nombre, cedula, beneficio from usuario where id=" + numAgree, null);

                if (select.moveToFirst()) {//moveToFirs evaluates if contain dates = if true=true
                    String date = "ID: "+numAgree+"\nNombre: "+select.getString(0) +"Cedula: "+ select.getString(1) +"\nBeneficiario: "+ select.getString(2)+"\n";
                    table.setText(table.getText() + date);
                } else {

                }
                //on increment
                numAgree++;
                select.close();
            }
        bd.close();
    }

    //button back
    public void goToBack(View see){
        Intent go=new Intent(this,LoginActivity.class);
        startActivity(go);
    }
    //button GestionBD
    public void goToAdminSQLite(View see){
        try {
            Intent go = new Intent(this, SQL.class);
            startActivity(go);
        }catch(Exception e) {
            Toast.makeText(this, "Error goToAdminSQLite_DataBaseActivity", Toast.LENGTH_SHORT).show();
        }
    }
}