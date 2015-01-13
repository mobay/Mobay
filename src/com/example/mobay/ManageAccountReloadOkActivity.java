package com.example.mobay;

import java.util.Date;
import java.util.List;

import com.example.model.Compte;
import com.example.model.Mobay;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ManageAccountReloadOkActivity extends Activity{

	Button menuAccueil = null;
	Button deconnexion = null;
	TextView solde;
	static double soldeUtilisateurCourant = 0;
	static List<ParseObject> listAccountUtilisateurCourant = null;
	String dbl ;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_account_reload_ok);
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		solde = (TextView) findViewById(R.id.solde);
		
		Intent i = getIntent();
		dbl = i.getStringExtra("montantRecharge");
				
		solde.setText(ManageAccountReloadOkActivity.reloadAccount(dbl));
		
		
		
		
			
		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadOkActivity.this, MainActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadOkActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});
		
	
	}
	
	public static String reloadAccount (String dbl){
		
		listAccountUtilisateurCourant = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());
		soldeUtilisateurCourant = (double) ((Compte) listAccountUtilisateurCourant.get(0)).getSolde();
		
		soldeUtilisateurCourant+= Compte.arrondir(Double.parseDouble(dbl), 2);
		//  plus arrondir et valider dans parse
		
		Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.RECHARGE, Double.parseDouble(dbl), new Date(),Mobay.utilisateurCourant.getObjectId(), true);
		op.saveInBackground();
		
		Compte cmptUserCour=null;
		cmptUserCour = ((Compte) listAccountUtilisateurCourant.get(0));
		cmptUserCour.setSolde(soldeUtilisateurCourant);
		cmptUserCour.saveInBackground();
		
		return String.valueOf(soldeUtilisateurCourant);
	}
	
}
