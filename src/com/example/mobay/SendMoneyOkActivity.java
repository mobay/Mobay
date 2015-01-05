package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SendMoneyOkActivity extends Activity{
	
private static final String TAG = "SendMoneyActivityOk";

	
	Button menuAccueil = null;
	Button deconnexion = null;
	TextView solde = null;
	double dbl = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_money_ok);
		
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		solde= (TextView) findViewById(R.id.solde);
		
		Intent i = getIntent();
		Log.d(TAG,"Solde utlisateurCourant SendMoneyOkActivity : "+ SendMoneyActivity.soldeUtilisateurCourant);
		dbl = i.getDoubleExtra("soldeUtilisateurCourant", 0.0);
		solde.setText(String.valueOf(dbl));
		
	deconnexion.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent deconnexion = new Intent(SendMoneyOkActivity.this, MainActivity.class);
			startActivity(deconnexion);
			finish();
		}

	});
	menuAccueil.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent mainMenu = new Intent(SendMoneyOkActivity.this, MainMenuActivity.class);
			startActivity(mainMenu);
		}

	});
	}
}
