package com.example.mobay;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Compte;
import com.example.model.Mobay;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.parse.ParseObject;

public class ManageAccountTransferOkActivity extends Activity {

	private static final String TAG = "ManageAccountTransferOkActivity";

	Button menuAccueil = null;
	Button deconnexion = null;
	TextView montant = null;
	double dbl;
	static double soldeUtilisateurCourant = 0;
	static List<ParseObject> listAccountUtilisateurCourant = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_account_transfer_ok);

		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		montant = (TextView) findViewById(R.id.solde);

		Intent i = getIntent();
		dbl = i.getDoubleExtra("montantDebiter", 0.0);
		Log.d(TAG,"Montant a débiter : "+dbl);
		
		listAccountUtilisateurCourant = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());
		soldeUtilisateurCourant = (double) ((Compte) listAccountUtilisateurCourant.get(0)).getSolde();
		
	
	
		
			montant.setText(ManageAccountTransferOkActivity.discardAccount(dbl,getApplicationContext()));

		
		


		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountTransferOkActivity.this, MainActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountTransferOkActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});

	}

	public static String discardAccount(double dbl,Context context) {
		
		if ((soldeUtilisateurCourant - dbl) < 0) {
			Toast.makeText(context, "Vous ne pouvez virer au maximum que le solde actuel de votre compte", Toast.LENGTH_SHORT).show();
			return String.valueOf(soldeUtilisateurCourant);
		}
		soldeUtilisateurCourant-= dbl;
		soldeUtilisateurCourant= Compte.arrondir(soldeUtilisateurCourant,2);
		Operation opa = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.VIREMENT, dbl, new Date(), Mobay.utilisateurCourant.getObjectId(), true);
		opa.saveInBackground();
		
		Compte cmptUserCour = null;
		cmptUserCour = ((Compte) listAccountUtilisateurCourant.get(0));
		cmptUserCour.setSolde(soldeUtilisateurCourant);
		cmptUserCour.saveInBackground();

		return String.valueOf(soldeUtilisateurCourant);

	}
}