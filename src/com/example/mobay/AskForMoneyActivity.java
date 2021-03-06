package com.example.mobay;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.Mobay;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.example.model.Utilisateur;

public class AskForMoneyActivity extends Activity {
	private static final String TAG = "SendMoneyActivity";

	Button valider = null;
	Button menuAccueil = null;
	Button deconnexion = null;
	static EditText numOrAlias = null;
	static EditText montant = null;

	static Utilisateur utilisateurIndique = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_money);

		valider = (Button) findViewById(R.id.valider);
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		montant = (EditText) findViewById(R.id.montant);
		numOrAlias = (EditText) findViewById(R.id.numOrAlias);

		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(AskForMoneyActivity.this, MainActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}
		});
		
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(AskForMoneyActivity.this, MainMenuActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}
		});
		valider.setOnClickListener(validerListener);
	}

	private OnClickListener validerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String numOrAliasText = numOrAlias.getText().toString().trim();
			String montantText = montant.getText().toString();

			Log.d(TAG, "Texte du champ NumOrAlias: " + numOrAliasText);
			Log.d(TAG, "Texte du champ Montant: " + montantText);

			// Verifications
			// Numero / alias absent
			if (numOrAliasText.isEmpty()) {
				Toast.makeText(getBaseContext(), "Merci de rentrer un num�ro de t�l�phone ou un alias!", Toast.LENGTH_SHORT).show();
				return;
			}

			// Montant absent
			if (montantText.isEmpty()) {
				Toast.makeText(getBaseContext(), "Merci de rentrer un montant!", Toast.LENGTH_SHORT).show();
				return;
			}

			// Num / alias < 4 caracteres et > 20 caracteres
			if (numOrAlias.length() < 4 || numOrAlias.length() > 20) {
				Toast.makeText(getBaseContext(), "Le champ \"Num. Mobile ou Alias\" doit contenir entre 4  et 20 caract�res!", Toast.LENGTH_SHORT).show();
				return;
			}

			double montantDouble = Double.parseDouble(montantText);
			Log.d(TAG, "Montant parse en double : " + montantDouble);
			// Montant < 0
			if (montantDouble <= 0) {
				Toast.makeText(getBaseContext(), "Le montant doit �tre sup�rieur � z�ro!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// le montant ne peut comporter que deux chiffres arp�s la virgule
			String montantPattern = "^\\d+(.\\d{1,2})?$";
			if (!montantText.matches(montantPattern)) {
				montant.getText().clear();
				Toast.makeText(getBaseContext(), "Le montant ne peut comporter que deux chiffres apr�s la virgule!", Toast.LENGTH_SHORT).show();
				return;
			}

			// Pas d'utilisateur avec ce pseudo ou ce num
			if (Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty() && Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty()) {
				Log.d(TAG, "User est pas dans la db !");
				Log.d(TAG, "Envoi d'argent : NOK");
				Intent askForMoneyNok = new Intent(AskForMoneyActivity.this, AskForMoneyNokActivity.class);
				startActivity(askForMoneyNok);
				return;
			}

			Log.d(TAG, "User est bien dans la db !");

			// On recupere l'utilisateur avec le num ou l'alias indique
			if (!Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).get(0);

			if (!Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).get(0);

			if (utilisateurIndique.getObjectId().matches(Mobay.utilisateurCourant.getObjectId())) {
				Toast.makeText(getBaseContext(), "Vous ne pouvez pas vous demander de l'argent � vous m�me", Toast.LENGTH_SHORT).show();
				return;
			}

			Log.d(TAG, "Envoi d'argent : OK");
			AskForMoneyActivity.demandeMoney(montantDouble);
			Intent askForMoneyOk = new Intent(AskForMoneyActivity.this, AskForMoneyOkActivity.class);
			startActivity(askForMoneyOk);

		}
	};

	// G�rer le cas on ne peut pas se demander de l'argent � soi-m�me
	private static void demandeMoney(double dbl) {
		Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.RECEPTION, dbl, new Date(), utilisateurIndique.getObjectId(), false);
		op.saveInBackground();
	}
}
