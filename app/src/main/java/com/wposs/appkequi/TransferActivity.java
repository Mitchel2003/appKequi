package com.wposs.appkequi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class TransferActivity extends AppCompatActivity {

	private TextInputEditText phoneSend, cashSend, messageSend;

	// DataBase FireBase
	private FirebaseFirestore bd;
	private CollectionReference openBD, openBDThisUser, openBDOtherUser;
	private DocumentReference document;
	private DatabaseReference bdReference;
	private FirebaseAuth auth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// fullScreen

		phoneSend = findViewById(R.id.inEditNumberPhoneSend);
		cashSend = findViewById(R.id.inEditCashSend);
		messageSend = findViewById(R.id.inEditMessageSend);

		//dataBase on
		bd = FirebaseFirestore.getInstance();
		openBD = bd.collection("user");
		bdReference = FirebaseDatabase.getInstance().getReference();// read or write
		auth = FirebaseAuth.getInstance();// authentication

	}

	//------------------------------------------------------------functions principals------------------------------------------------------------
	//button sendCash
	public void actionSendCash(View see) {
		SharedPreferences preset = getSharedPreferences("info", Context.MODE_PRIVATE);

		//get input
		String writePhone = phoneSend.getText().toString();
		String writeCash = cashSend.getText().toString();
		String writeMessage = messageSend.getText().toString();
		
		//open collections
		openBDUserContext = openBD.document(preset.getString("numberPhone", "")).collection("history");
		openBDUserOther = openBD.document(writePhone).collection("history");
		
		//tags
		String statusSend="send";
		String statusReceive="receive";
		
		if (!writePhone.isEmpty() && !writeCash.isEmpty() && !writeMessage.isEmpty()) {

			openBD.whereEqualTo("numberPhone", writePhone).get().addOnCompleteListener(task -> {

				if (task.isSuccessful()) {
					QuerySnapshot consult = task.getResult();

					if (consult != null && !consult.isEmpty()) {// exist

						// consult data of the user that receive
						final String[] nameUserOther = new String[1];

						// working with QuerySnapshot
						openBD.whereEqualTo("numberPhone", writePhone).get()
								.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
									@Override
									public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
										for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
											// getting date of FireStore
											nameUserOther[0] = document.getString("name");
										}
									}
								});

						try {
							// consult credits of the user that send
							String nameContext = preset.getString("name", "");
							String numberContext = preset.getString("numberPhone", "");
							String balanceContext = preset.getString("balance", "");
							int balance = Integer.parseInt(balanceContext);
							long sendBalance = Long.parseLong(writeCash);

							if (sendBalance <= balance) {// credits valid

								//method principal
								historyUser(openBDUserContext, nameContext, numberContext, nameUserOther[0], writePhone, Integer.valueOf(sendBalance), writeMessage, statusSend);
								historyUser(openBDUserOther, nameContext, numberContext, nameUserOther[0], writePhone, Integer.valueOf(sendBalance), writeMessage, statusReceive);
								
								Toast.makeText(this,"successfully",Toast.LENGTH_SHORT).show();
								
								phoneSend.setText="";
								cashSend.setText="";
								messageSend.setText="";
								
							} else {// credits insufficient
								Toast.makeText(getApplicationContext(), "credits insufficient", Toast.LENGTH_SHORT).show();
							}

						} catch (Exception e) {
							Toast.makeText(this, "number too long", Toast.LENGTH_SHORT).show();
						}

					} else {// not exist
						Toast.makeText(getApplicationContext(), "this number phone not exist", Toast.LENGTH_SHORT).show();
					}

				} else {// validation failure
					Toast.makeText(getApplicationContext(), "error taskPhone", Toast.LENGTH_SHORT).show();
				}

			});

		} else {// enter the fields "empty fields"
			Toast.makeText(this, "Enter the fields", Toast.LENGTH_SHORT).show();
		}

	}

	//button back
	public void backToLobby(View see) {
		Intent go = new Intent(getApplicationContext(), LobbyActivity.class);
		startActivity(go);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	
	//------------------------------------------------------------tools------------------------------------------------------------
	//for adapter "history of transactions in cardView"
	private void historyUser(CollectionReference userContext, String nameSend, String numberSend, String[] nameReceive, String numberReceive, int cash, String message, String status) {
		String folderContext = "history";
		
		int numHistory=userContext.document(folderContext).size();
		
		if(numHistory!=-1) {//contain information, will never be 0 
			
			// this for obtain the number id, and can ++
			String nextId = String.valueOf(numHistory + 1);

			DocumentReference document = userContext.document(nextId);

			// create a map for compilation of values
			Map<String, Object> map = new HashMap<>();
			map.put("nameUserSend", nameSend);
			map.put("numberPhoneUserSend", numberSend);
			map.put("nameUserReceive", nameReceive[0]);
			map.put("numberPhoneUserReceive", numberReceive);
			map.put("cash", cash);
			map.put("message", message);
			map.put("status", status);

			document.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void unused) {
					//successfully
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(getApplicationContext(), "Error #201 (work not successfully)"+ e, Toast.LENGTH_SHORT).show();
				}
			});
			
		}else{//is -1, equals to say null "create history"

			String id = "1";

			DocumentReference document = userContext.document(id);

			// create a map for compilation of values
			Map<String, Object> map = new HashMap<>();
			map.put("nameUserSend", nameSend);
			map.put("numberPhoneUserSend", numberSend);
			map.put("nameUserReceive", nameReceive[0]);
			map.put("numberPhoneUserReceive", numberReceive);
			map.put("cash", cash);
			map.put("message", message);
			map.put("status", status);

			document.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void unused) {
					//successfully
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(getApplicationContext(), "Error #201 (work not successfully)"+ e, Toast.LENGTH_SHORT).show();
				}
			});
			
		}
		
	}

	
	
	//------------------------------------------------------------ads------------------------------------------------------------
	

	
	

}