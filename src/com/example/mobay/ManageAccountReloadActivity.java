package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ManageAccountReloadActivity extends Activity{
	
	private static final String TAG = "ManageAccountReloadActivity";

	Button menuAccueil = null;
	Button deconnexion = null;
	Button manageAccount = null;
	Button vingt = null;
	Button quarante= null;
	Button soixante= null;
	Button quatreVingt = null;
	Button cent = null;
	String montantRecharge;
	String pattern = "[^0-9]+";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_account_reload);
		
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		manageAccount = (Button) findViewById(R.id.manageAccount);
		vingt = (Button) findViewById(R.id.vingt);
		quarante = (Button) findViewById(R.id.quarante);
		soixante= (Button) findViewById(R.id.soixante);
		quatreVingt= (Button) findViewById(R.id.quatreVingt);
		cent= (Button) findViewById(R.id.cent);
		
		
		manageAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent manageAccount = new Intent(ManageAccountReloadActivity.this, ManageAccountMenuActivity.class);
				startActivity(manageAccount);
			}

		});
		
		
		vingt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				montantRecharge = (String) vingt.getText();
				montantRecharge = montantRecharge.replaceAll(pattern,"");
				Log.d(TAG,"Montant de la recharge depuis Reload : "+montantRecharge);
				Intent manageAccount = new Intent(ManageAccountReloadActivity.this, ManageAccountReloadValidationActivity.class);
				manageAccount.putExtra("montantRecharge", montantRecharge);
				startActivity(manageAccount);
			}

		});
		
		quarante.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				montantRecharge = (String) quarante.getText();
				montantRecharge = montantRecharge.replaceAll(pattern,"");
				Log.d(TAG,"Montant de la recharge depuis Reload : "+montantRecharge);
				Intent manageAccount = new Intent(ManageAccountReloadActivity.this, ManageAccountReloadValidationActivity.class);
				manageAccount.putExtra("montantRecharge", montantRecharge);
				startActivity(manageAccount);
			}

		});
		soixante.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				montantRecharge = (String) soixante.getText();
				montantRecharge = montantRecharge.replaceAll(pattern,"");
				Log.d(TAG,"Montant de la recharge depuis Reload : "+montantRecharge);
				Intent manageAccount = new Intent(ManageAccountReloadActivity.this, ManageAccountReloadValidationActivity.class);
				manageAccount.putExtra("montantRecharge", montantRecharge);
				startActivity(manageAccount);
			}

		});
		quatreVingt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				montantRecharge = (String) quatreVingt.getText();
				montantRecharge = montantRecharge.replaceAll(pattern,"");
				Log.d(TAG,"Montant de la recharge depuis Reload : "+montantRecharge);
				Intent manageAccount = new Intent(ManageAccountReloadActivity.this, ManageAccountReloadValidationActivity.class);
				manageAccount.putExtra("montantRecharge", montantRecharge);
				startActivity(manageAccount);
			}

		});
		cent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				montantRecharge = (String) cent.getText();
				montantRecharge = montantRecharge.replaceAll(pattern,"");
				Log.d(TAG,"Montant de la recharge depuis Reload : "+montantRecharge);
				Intent manageAccount = new Intent(ManageAccountReloadActivity.this, ManageAccountReloadValidationActivity.class);
				manageAccount.putExtra("montantRecharge", montantRecharge);
								startActivity(manageAccount);
			}

		});
		
		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadActivity.this, MainActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountReloadActivity.this, MainMenuActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}

		});
		
		
		
}
	
	
	
	
	
	
	
	
}
