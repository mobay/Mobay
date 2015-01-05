package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AskForMoneyOkActivity extends Activity{
	
private static final String TAG = "AskForMoneyOkActivity";

	
	Button menuAccueil = null;
	Button deconnexion = null;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_for_money_ok);
		
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		
		
	deconnexion.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent deconnexion = new Intent(AskForMoneyOkActivity.this, MainActivity.class);
			startActivity(deconnexion);
			finish();
		}

	});
	menuAccueil.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent mainMenu = new Intent(AskForMoneyOkActivity.this, MainMenuActivity.class);
			startActivity(mainMenu);
		}

	});
	}
}
