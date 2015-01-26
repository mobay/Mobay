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

public class ManageAccountReloadOkActivity extends Activity {

	Button menuAccueil = null;
	Button deconnexion = null;
	TextView solde;
	static double soldeUtilisateurCourant = 0;
	String dbl;

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
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadOkActivity.this, MainMenuActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}

		});

	}

	public static String reloadAccount(String dbl) 
	{
		soldeUtilisateurCourant = Mobay.compteUtilisateurCourant.getSolde();
		soldeUtilisateurCourant += Double.parseDouble(dbl);
		Compte.arrondir(soldeUtilisateurCourant, 2);
		// plus arrondir et valider dans parse

		Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.RECHARGE, Double.parseDouble(dbl), new Date(), Mobay.utilisateurCourant.getObjectId(), true);
		op.saveInBackground();

		Mobay.compteUtilisateurCourant.setSolde(soldeUtilisateurCourant);
		Mobay.compteUtilisateurCourant.saveInBackground();

		return String.valueOf(soldeUtilisateurCourant);
	}

}
