package com.example.mobay;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.model.Compte;
import com.example.model.Mobay;
import com.parse.ParseObject;

public class ManageAccountMenuActivity extends Activity {
	
	Button reload = null;
	Button menuAccueil = null;
	Button deconnexion = null;
	Button transfer = null;
	static double soldeUtilisateurCourant = 0;
	TextView soldeView = null;
	static List<ParseObject> listAccountUtilisateurCourant = null;


		private static final String TAG = "ManageAccountAcitivity";

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_manage_account_menu);
		
		
			reload = (Button) findViewById(R.id.reload);
			menuAccueil = (Button) findViewById(R.id.menuAccueil);
			deconnexion = (Button) findViewById(R.id.deconnexion);
			soldeView = (TextView) findViewById(R.id.solde);
			transfer = (Button) findViewById(R.id.transfer);
			
			
			deconnexion.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent deconnexion = new Intent(ManageAccountMenuActivity.this, MainActivity.class);
					startActivity(deconnexion);
				}

			});
			menuAccueil.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent deconnexion = new Intent(ManageAccountMenuActivity.this, MainMenuActivity.class);
					startActivity(deconnexion);
					finish();
				}

			});
			
			reload.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent deconnexion = new Intent(ManageAccountMenuActivity.this, ManageAccountReloadActivity.class);
					startActivity(deconnexion);
					finish();
				}

			});
		
			transfer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent deconnexion = new Intent(ManageAccountMenuActivity.this, ManageAccountTransferActivity.class);
					startActivity(deconnexion);
					finish();
				}

			});
			
			
		soldeView.setText(ManageAccountMenuActivity.whatIsMyBalance(Mobay.utilisateurCourant.getObjectId()));
		
		
		}
		
		public static String whatIsMyBalance (String userObjectId){
			
			listAccountUtilisateurCourant = Compte.getAccountWithUserObjectId(userObjectId);
			soldeUtilisateurCourant = (double) ((Compte) listAccountUtilisateurCourant.get(0)).getSolde();
			
			return String.valueOf(soldeUtilisateurCourant);
		}
}
