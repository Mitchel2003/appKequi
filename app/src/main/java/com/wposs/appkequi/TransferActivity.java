package com.wposs.appkequi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

    private TextInputEditText phoneSend, cashSend, messageSend;

    //DataBase Firebase
    private FirebaseFirestore bd;
    private CollectionReference openBDUser, openBDHistory;
    private DocumentReference document;
    private DatabaseReference bdReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//fullScreen

        //dataBase on
        bd= FirebaseFirestore.getInstance();
        openBDUser=bd.collection("user");
        openBDHistory=bd.collection("history");
        bdReference= FirebaseDatabase.getInstance().getReference();//read or write
        auth=FirebaseAuth.getInstance();//authentication
    }

    //first funtion
    public void actionSendCash(View see) {
        SharedPreferences preset=getSharedPreferences("info", Context.MODE_PRIVATE);

        String writePhone=phoneSend.getText().toString();
        String writeCash=cashSend.getText().toString();
        String writeMessage=messageSend.getText().toString();

        if(!writePhone.isEmpty()&&!writeCash.isEmpty()&&!writeMessage.isEmpty()){

            openBDUser.whereEqualTo("numberPhone",writePhone).get().addOnCompleteListener(task -> {

                if(task.isSuccessful()) {
                    QuerySnapshot consult=task.getResult();

                    if(consult!=null&&!consult.isEmpty()) {//exist

                        //consult data of the user that receive
                        final String [] otherName=new String[1];

                        openBDUser.whereEqualTo("numberPhone",writePhone).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                for(QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    //getting date of FireStore
                                    otherName[0] = document.getString("name");
                                }
                            }
                        });

                        //consult credits of the user that send
                        String thisName=preset.getString("name","");
                        String thisNumberPhone=preset.getString("numberPhone","");
                        String thisBalanceStr=preset.getString("balance","");
                        int thisBalance=Integer.parseInt(thisBalanceStr);
                        int sendBalance=Integer.parseInt(writeCash);

                        if(sendBalance<=thisBalance) {//credits valid

                            //need create history for this and the other user

                            //first user that send
                            openBDHistory.whereEqualTo(thisNumberPhone,thisNumberPhone).get().addOnCompleteListener(task2 -> {

                                if(task2.isSuccessful()) {//validation successfully
                                    QuerySnapshot consult2=task2.getResult();

                                    if(consult2!=null&&!consult2.isEmpty()) {//is exist



                                    }else {//not exist "create"
                                        String id = "001";

                                        //this is equals a say "document=bd.collection("history").document(thisNumberPhone).document(id);"
                                        DocumentReference documentInitial,thisDocument;
                                        documentInitial=openBDHistory.document(thisNumberPhone);//create history_numberPhone

                                        CollectionReference openSubFolder= documentInitial.collection(thisNumberPhone);//access to reference
                                        thisDocument=openSubFolder.document(id);//create in numberPhone the folder id "001" (history)

                                        //create a map for compilation of values
                                        Map<String, Object> map= new HashMap<>();
                                        map.put("nameUserSend",thisName);
                                        map.put("numberPhoneUserSend",thisNumberPhone);
                                        map.put("nameUserReceive",otherName[0]);
                                        map.put("numberPhoneUserReceive",writePhone);
                                        map.put("cashSend",sendBalance);
                                        map.put("messageSend",writeMessage);
                                        map.put("status","send");

                                        //in "history" with id personalized, we keep the map and send with "add" to bd
                                        thisDocument.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {                                                    //bd.collection("user").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override                                                                                                             //    @Override
                                            public void onSuccess(Void unused) {//if completed                                                                    //    public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getApplicationContext(),"create this history completed",Toast.LENGTH_SHORT).show();            //        Toast.makeText(getApplicationContext(),"Saved successfully",Toast.LENGTH_SHORT).show();
                                            }                                                                                                                     //    }
                                        }).addOnFailureListener(new OnFailureListener() {                                                                         //}).addOnFailureListener(new OnFailureListener() {
                                            @Override                                                                                                             //    @Override
                                            public void onFailure(@NonNull Exception e) {//if failed                                                              //    public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"error create this history",Toast.LENGTH_SHORT).show();                 //        Toast.makeText(getApplicationContext(),"Error Database",Toast.LENGTH_SHORT).show();
                                            }                                                                                                                     //    }
                                        });


                                        //now user that receive
                                        openBDHistory.whereEqualTo(writePhone,writePhone).get().addOnCompleteListener(task3 -> {

                                            if(task3.isSuccessful()) {//validation successfully
                                                QuerySnapshot consult3=task3.getResult();

                                                if(consult3!=null&&!consult3.isEmpty()) {//is exist



                                                }else {//not exist "create"

                                                    //this is equals a say "document=bd.collection("history").document(thisNumberPhone).document(id);"
                                                    DocumentReference documentInitialOther, otherDocument;
                                                    documentInitialOther=openBDHistory.document(writePhone);//create history_numberPhone

                                                    CollectionReference openSubFolderOther= documentInitialOther.collection(writePhone);//access to reference
                                                    otherDocument=openSubFolderOther.document(id);//create in numberPhone the folder id "001" (history)

                                                    //create a map for compilation of values
                                                    Map<String, Object> otherMap= new HashMap<>();
                                                    otherMap.put("nameUserSend",thisName);
                                                    otherMap.put("numberPhoneUserSend",thisNumberPhone);
                                                    otherMap.put("nameUserReceive",otherName[0]);
                                                    otherMap.put("numberPhoneUserReceive",writePhone);
                                                    otherMap.put("cashReceiver",sendBalance);
                                                    otherMap.put("messageReceive",writeMessage);
                                                    otherMap.put("status","receive");

                                                    //in "history" "numberPhone" "id(001)" we keep the map and send with "add" to bd
                                                    otherDocument.set(otherMap).addOnSuccessListener(new OnSuccessListener<Void>() {                                                    //bd.collection("user").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override                                                                                                             //    @Override
                                                        public void onSuccess(Void unused) {//if completed                                                                    //    public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(),"create other history completed",Toast.LENGTH_SHORT).show();            //        Toast.makeText(getApplicationContext(),"Saved successfully",Toast.LENGTH_SHORT).show();
                                                        }                                                                                                                     //    }
                                                    }).addOnFailureListener(new OnFailureListener() {                                                                         //}).addOnFailureListener(new OnFailureListener() {
                                                        @Override                                                                                                             //    @Override
                                                        public void onFailure(@NonNull Exception e) {//if failed                                                              //    public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(),"error create other history",Toast.LENGTH_SHORT).show();                 //        Toast.makeText(getApplicationContext(),"Error Database",Toast.LENGTH_SHORT).show();
                                                        }                                                                                                                     //    }
                                                    });
                                                }

                                            }else {//not validate
                                                Toast.makeText(getApplicationContext(), "error taskOtherNumberPhone", Toast.LENGTH_SHORT).show();
                                            }

                                        });
                                    }



                                }else {//validation not successfully
                                    Toast.makeText(getApplicationContext(), "error taskThisNumberPhone", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else {//credits insufficient
                            Toast.makeText(getApplicationContext(), "credits insufficient", Toast.LENGTH_SHORT).show();
                        }
                    }else{//not exist
                        Toast.makeText(getApplicationContext(), "this number phone not exist", Toast.LENGTH_SHORT).show();
                    }


                }else {//validation failure
                    Toast.makeText(getApplicationContext(), "error taskPhone", Toast.LENGTH_SHORT).show();
                }

            });


        }else {//enter the fields "empty fields"
            Toast.makeText(this,"Enter the fields",Toast.LENGTH_SHORT).show();
        }

    }



    //ads------------------------------------------------------

    public void createHistoryThis() {

    }
    public void createHistoryOther() {

    }
}