package com.example.mobay;

import com.example.model.Mobay;
import com.example.model.Utilisateur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;

public class FriendActivity extends Activity 
{
	private static final String TAG = "FriendActivity";
	
	EditText inputPseudo;
	Button buttonSetParrain;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);		
		
		inputPseudo = (EditText) findViewById(R.id.editTextModifyPseudo);
		buttonSetParrain = (Button) findViewById(R.id.buttonSetParrain);
	}
	
	public void setParrain(View v) {
		
		final Context currentContext = this;
		final String pseudo = inputPseudo.getText().toString();
		
		
		Log.d(TAG, "Texte du champ inputPseudo: " + pseudo);
		
		
		// Verifications/ alias absent
		if (pseudo.isEmpty()) {
			Toast.makeText(getBaseContext(), "Merci de rentrer un ami!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		// Pas d'utilisateur avec ce pseudo ou ce num
		if (Utilisateur.getUtilisateurWithAttribut("pseudo", pseudo).isEmpty()) {
			Log.d(TAG, "User est pas dans la db !");
			Log.d(TAG, "Parrainage: NOK");
			Toast.makeText(getBaseContext(), "Cet utilisateur n'existe pas!", Toast.LENGTH_SHORT).show();	
			return;
		}

		Log.d(TAG, "User est bien dans la db !");
		
		// Verifications pas deja parrain
		if (!Utilisateur.getUtilisateurWithAttribut("parrain", pseudo).isEmpty()) {
			Toast.makeText(getBaseContext(), "Vous ne pouvez coopter qu'un ami!", Toast.LENGTH_LONG).show();
			return;
		}
		
		// QUESTION DIALOG PARRRAINAGE
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Parrainage");
			builder.setMessage("Voulez-vous vraiment parrainer cet ami? Vous ne pourrez parrainer qu'une personne!");

			// BOUTON OUI ==> retour a la 1ere activite te reinitialisation des variables de session
			builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					// On vire le question dialog
					dialog.dismiss();

					// Affectation du parrainage dans la base
					Mobay.utilisateurCourant.setParrain(pseudo);
					Mobay.utilisateurCourant.saveInBackground();
					Toast.makeText(getBaseContext(), "Parrainage réussie", Toast.LENGTH_SHORT).show();

					// Retour au menu
					Intent intentMainMenu = new Intent(currentContext, MainMenuActivity.class);
					startActivity(intentMainMenu);

					// Deco donc on met fin a l'activite courante, on ne pourra plus y acceder avec le bouton Precedent
					finish();				
				}
			});

			// BOUTON NON ==> rien ne se passe
			builder.setNegativeButton("En fait, non", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					// On vire le question dialog
					dialog.dismiss();
				}
			});

			AlertDialog alert = builder.create();
			alert.show();
		}
	}

