package com.example.mobay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ManageAccountTransferActivity extends Activity {

	private static final String TAG = "ManageAccountTransferActivity";

	Button valider = null;
	Button menuAccueil = null;
	Button deconnexion = null;
	EditText montant = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_account_transfer);

		valider = (Button) findViewById(R.id.valider);
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		montant = (EditText) findViewById(R.id.montant);

		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountTransferActivity.this, MainActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(ManageAccountTransferActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});
		valider.setOnClickListener(validerListener);

	}

	private OnClickListener validerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String montantText = montant.getText().toString();
			if (montantText.isEmpty()) {
				Toast.makeText(getBaseContext(), "Merci de rentrer un montant!", Toast.LENGTH_SHORT).show();
				return;
			}

			double montantDouble = Double.parseDouble(montantText);
			Log.d(TAG, "Montant parse en double : " + montant);
			// Montant < 0
			if (montantDouble <= 0) {
				Toast.makeText(getBaseContext(), "Le montant doit être supérieur à zéro!", Toast.LENGTH_SHORT).show();
				return;
			}

			// le montant ne peut comporter que deux chiffres arpès la virgule
			String montantPattern = "^\\d+(.\\d{1,2})?$";
			if (!montantText.matches(montantPattern)) {
				montant.getText().clear();
				Toast.makeText(getBaseContext(), "Le montant ne peut comporter que deux chiffres après la virgule!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Intent transferOk = new Intent(ManageAccountTransferActivity.this, ManageAccountTransferOkActivity.class);
			transferOk.putExtra("montantDebiter", montantDouble);
			startActivity(transferOk);

		}
	};

	// Montant absent

}