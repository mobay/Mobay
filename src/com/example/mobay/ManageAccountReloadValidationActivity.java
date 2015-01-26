package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ManageAccountReloadValidationActivity extends Activity{
	
	private static final String TAG = "ManageAccountReloadValidationActivity";
	
	Button menuAccueil = null;
	Button deconnexion = null;
	TextView montantRecharge = null;
	Button manageAccount = null;
	Button valider =null;
	String dbl= null;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_account_reload_validation);
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		manageAccount = (Button) findViewById(R.id.manageAccount);
		valider = (Button) findViewById(R.id.valider);
		montantRecharge = (TextView) findViewById(R.id.montantRecharge);
		
		Intent i = getIntent();
		dbl = i.getStringExtra("montantRecharge");
		Log.d(TAG,"Montant de la recharge : "+dbl);
		montantRecharge.setText(dbl);
		
			
		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadValidationActivity.this, MainActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadValidationActivity.this, MainMenuActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}

		});
		
		valider.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent validate = new Intent(ManageAccountReloadValidationActivity.this, ManageAccountReloadOkActivity.class);
				validate.putExtra("montantRecharge", dbl);
				startActivity(validate);
				finish();
			}

		});
	
		manageAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadValidationActivity.this, ManageAccountMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});
		
		
	}

}
