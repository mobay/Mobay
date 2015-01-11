package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AcceptMoneyRequestOkActivity extends Activity {
	private static final String TAG = "AcceptMoneyRequestOkActivity";

	static double soldeUtilisateurCourant = 0;

	Button menuAccueil = null;
	Button deconnexion = null;
	TextView solde = null;
	TextView montantDebiter = null;
	double dbl = 0;
	double montantIntent=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accept_money_request_ok);

		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		solde = (TextView) findViewById(R.id.solde);
		montantDebiter = (TextView) findViewById(R.id.montantDebiter);

		Intent i = getIntent();
		Log.d(TAG, "Solde utlisateurCourant SendMoneyOkActivity : " + AcceptMoneyRequestActivity.soldeUtilisateurCourant);
		dbl = i.getDoubleExtra("soldeUtilisateurCourant", 0.0);
		solde.setText(String.valueOf(dbl));
		montantIntent = i.getDoubleExtra("montantDebiter", 0.0);
		montantDebiter.setText(String.valueOf(montantIntent));
		
		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(AcceptMoneyRequestOkActivity.this, MainActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(AcceptMoneyRequestOkActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});

	}

}
