package com.example.mobay;

import com.example.model.Mobay;
import com.example.model.Utilisateur;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IDsActivity extends Activity 
{
	private static final String TAG = "IdsActivity";

	// Variables pour les widgets de la vue
	EditText inputPseudo;
	Button buttonModifyPseudo;
	EditText inputCurrentMdp;
	EditText inputNewMdp;
	EditText inputNewMdpAgain;
	Button buttonModifyMdp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ids);
		
		inputPseudo = (EditText) findViewById(R.id.editTextModifyPseudo);
		buttonModifyPseudo = (Button) findViewById(R.id.buttonModifyPseudo);
		inputCurrentMdp = (EditText) findViewById(R.id.editTextCurrentMdp);
		inputNewMdp = (EditText) findViewById(R.id.editTextNewMdp);
		inputNewMdpAgain = (EditText) findViewById(R.id.editTextNewMdpAgain);
		buttonModifyMdp = (Button) findViewById(R.id.buttonModifyMdp);
		
		// Si l'utilisateur courant a deja un identifiant, on le rappelle
		if(!Mobay.utilisateurCourant.getPseudo().isEmpty())
			inputPseudo.setText(Mobay.utilisateurCourant.getPseudo());
	}
	
	public void modifierPseudo(View v)
	{
		final Context currentContext = this;
		final String pseudo = inputPseudo.getText().toString();
		String pseudoPattern = "^[a-z0-9_-]{3,15}$";
		
		// Verifications
		// Pas de pseudo actuel et validation alors que pseudo absent...
		if(Mobay.utilisateurCourant.getPseudo().isEmpty() && pseudo.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Veuillez indiquer un pseudo...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Pseudo invalide
		if(!pseudo.isEmpty() && !pseudo.matches(pseudoPattern))
		{
			Toast.makeText(getBaseContext(), "Pseudonyme invalide! Les minuscules, chiffres et tirets sont autorisés et le pseudonyme doit comprendre entre 3 et 15 caractères.", Toast.LENGTH_LONG).show();
			return;
		}
		
		// Reutilisation du meme pseudo
		if(Mobay.utilisateurCourant.getPseudo().matches(pseudo))
		{
			Toast.makeText(getBaseContext(), "Veuillez choisir un pseudo différent...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Doublon pseudo
		if(!pseudo.isEmpty() && !Utilisateur.getUtilisateurWithAttribut("pseudo", pseudo).isEmpty())
		{
			Toast.makeText(getBaseContext(), "Ce pseudonyme est déjà utilisé par un autre utilisateur!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// QUESTION DIALOG MODIF PSEUDO
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Modification du pseudonyme");
		builder.setMessage("Voulez-vous vraiment modifier votre pseudonyme?");

		// BOUTON OUI ==> retour a la 1ere activite te reinitialisation des variables de session
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// On vire le question dialog
				dialog.dismiss();

				// Changement de mdp dans la base
				Mobay.utilisateurCourant.setPseudo(pseudo);
				Mobay.utilisateurCourant.saveInBackground();
				Toast.makeText(getBaseContext(), "Vous avez bien modifié votre pseudonyme!", Toast.LENGTH_SHORT).show();

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
	
	public void modifierMdp(View v) throws Exception
	{
		final Context currentContext = this;
		String currentMdp = inputCurrentMdp.getText().toString();
		String currentMdpMD5 = Utilisateur.crypterMdp(currentMdp);
		String newMdp = inputNewMdp.getText().toString();
		final String newMdpMD5 = Utilisateur.crypterMdp(newMdp);
		String newMdpAgain = inputNewMdpAgain.getText().toString();
		String mdpPattern = "((?=.*\\d)(?=.*[a-z]).{6,20})";
		
		// Verifications
		// Champs vides
		if(currentMdp.isEmpty() || newMdp.isEmpty() || newMdpAgain.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Veillez remplir tous les champs avant de valider.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Le mot de passe actuel ne correspond pas a celui indique
		if(!Mobay.utilisateurCourant.getMdp().matches(currentMdpMD5))
		{
			Toast.makeText(getBaseContext(), "Votre mot de passe actuel ne correspond pas à celui indiqué!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Mot de passe invalide
		if(!newMdp.matches(mdpPattern))
		{
			Toast.makeText(getBaseContext(), "Mot de passe invalide! Vous devez utiliser des minuscules et au moins un chiffre, le mot de passe doit être compris entre 6 et 20 caractères.", Toast.LENGTH_LONG).show();
			return;
		}
		
		// Repeter le mot de passe manquant
		if(newMdpAgain.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Vous devez resaisir le mot de passe!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Repeter le mot de passe invalide
		if(!newMdp.matches(newMdpAgain))
		{
			Toast.makeText(getBaseContext(), "Vous n'avez pas resaisi le même mot de passe!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// QUESTION DIALOG MODIF MDP
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Modification du mot de passe");
		builder.setMessage("Voulez-vous vraiment modifier votre mot de passe?");

		// BOUTON OUI ==> retour a la 1ere activite te reinitialisation des variables de session
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// On vire le question dialog
				dialog.dismiss();

				// Changement de mdp dans la base
				Mobay.utilisateurCourant.setMdp(newMdpMD5);
				Mobay.utilisateurCourant.saveInBackground();
				Toast.makeText(getBaseContext(), "Vous avez bien modifié votre mot de passe!", Toast.LENGTH_SHORT).show();

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
