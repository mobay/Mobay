package com.example.mobay;

import com.example.model.Mobay;
import com.example.model.Utilisateur;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ConnectionActivity extends Activity 
{
	private static final String TAG = "ConnectionActivity";
	
	// Variables pour les widgets de la vue
	EditText inputNumPseudo;
	EditText inputMdp;
	ImageButton buttonConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		inputNumPseudo = (EditText) findViewById(R.id.loginNum);
		inputMdp = (EditText) findViewById(R.id.inputMdpConnection);
		buttonConnection = (ImageButton) findViewById(R.id.buttonValidateConnection);
	}
	
	public void validerConnexion(View view) throws Exception
	{
		Utilisateur utilisateurCourant = null;
		
		String numPseudo = inputNumPseudo.getText().toString().trim();
		String mdp = inputMdp.getText().toString();
		
		String telPattern = "^((\\+|00)33\\s?|0)[679](\\s?\\d{2}){4}$";
		
		// Verifications
		// Numero de telephone ou pseudo absent
		if(numPseudo.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Numéro de téléphone ou pseudo manquant!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Pas d'utilisateur avec ce pseudo ou ce num
		if (Utilisateur.getUtilisateurWithAttribut("numTel", numPseudo).isEmpty() && Utilisateur.getUtilisateurWithAttribut("pseudo", numPseudo).isEmpty()) 
		{
			Toast.makeText(getBaseContext(), "Pas d'utilisateur avec ce numéro ou pseudo!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		Log.d(TAG, "L'utilisateur indiqué est bien dans la base.");
		
		// On recupere l'utilisateur avec le num ou l'alias indique
		if (!Utilisateur.getUtilisateurWithAttribut("numTel", numPseudo).isEmpty())
			utilisateurCourant = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numPseudo).get(0);

		if (!Utilisateur.getUtilisateurWithAttribut("pseudo", numPseudo).isEmpty())
			utilisateurCourant = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("pseudo", numPseudo).get(0);
		
		// Mot de passe absent
		if(mdp.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Mot de passe manquant!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Ici, on va comparer le mdp de la base et celui de l'utilisateur qu'on a recupere
		String mdpMD5 = Utilisateur.crypterMdp(mdp);
		if(!utilisateurCourant.getMdp().matches(mdpMD5))
		{
			Toast.makeText(getBaseContext(), "Mot de passe erroné! Réessayez.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Passage au menu principal et attribution 'variable de session'
		Log.d(TAG, "Connexion de: " + utilisateurCourant.getNumTel());
		Toast.makeText(getBaseContext(), "Données correctes, connexion réussie!", Toast.LENGTH_SHORT).show();
		
		// Attribution de la variable de session pour l'utilisateur courant
		Mobay.utilisateurCourant = utilisateurCourant;
		
		// Acces au menu principal
		Intent intentMainMenu = new Intent(this, MainMenuActivity.class);
		startActivity(intentMainMenu);
		
		// La connexion etant effectuee, on met fin a l'activite (on ne pourra plus y revenir avec le bouton Precedent)
		finish();
	}
}
