package com.example.mobay;

import com.example.model.Mobay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainMenuActivity extends Activity 
{
	private static final String TAG = "MainMenuActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
	}
	
	
	public void envoiArgent(View v)
	{
		
	}
	
	public void demandeArgent(View v)
	{
		
	}
	
	public void accepterDemande(View v)
	{
		
	}
	
	public void gererCompte(View v)
	{
		
	}
	
	public void historique(View v)
	{
		
	}
	
	public void gererIdentifiants(View v)
	{
		Intent intentIDs = new Intent(this, IDsActivity.class);
		startActivity(intentIDs);
	}
	
	public void coopterAmi(View v)
	{
		
	}
	
	public void aide(View v)
	{
		Intent intentHelp = new Intent(this, HelpActivity.class);
		startActivity(intentHelp);
	}
	
	public void deconnexion(View v)
	{	
		final Context currentContext = this;
		
		// QUESTION DIALOG DECO
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Déconnexion");
		builder.setMessage("Voulez-vous vraiment vous déconnecter?");
		
		// BOUTON OUI ==> retour a la 1ere activite te reinitialisation des variables de session
		builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				// On vire le question dialog
				dialog.dismiss();
				
				// Variable de session reinitialisee
				Mobay.utilisateurCourant = null;
				
				// Retour a la 1ere activite
				Intent intentMain = new Intent(currentContext, MainActivity.class);
				startActivity(intentMain);
				
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
