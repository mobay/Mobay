package com.example.mobay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class SendMoneyNokActivity extends ActionBarActivity{
private static final String TAG = "SendMoneyActivityOk";

	
	Button menuAccueil = null;
	Button deconnexion = null;
	
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_money_nok);
		
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
	
	deconnexion.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent deconnexion = new Intent(SendMoneyNokActivity.this, MainMenuActivity.class);
			startActivity(deconnexion);
		}

	});
	menuAccueil.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent mainMenu = new Intent(SendMoneyNokActivity.this, MainMenuActivity.class);
			startActivity(mainMenu);
		}

	});
	}
}