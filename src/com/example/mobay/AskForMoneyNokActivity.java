package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AskForMoneyNokActivity extends Activity{
	
private static final String TAG = "AskForMoneyNokActivity";

	
	Button menuAccueil = null;
	Button deconnexion = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_for_money_nok);
		
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		
		
	deconnexion.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent deconnexion = new Intent(AskForMoneyNokActivity.this, MainActivity.class);
			deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(deconnexion);
			finish();
		}

	});
	menuAccueil.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent mainMenu = new Intent(AskForMoneyNokActivity.this, MainMenuActivity.class);
			mainMenu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(mainMenu);
			finish();
		}

	});
	}
}
