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
import android.widget.Toast;

public class ConnectionActivity extends Activity 
{
	private static final String TAG = "ConnectionActivity";
	
	// Variables pour les widgets de la vue
	EditText inputNumTel;
	EditText inputMdp;
	Button buttonConnection;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		inputNumTel = (EditText) findViewById(R.id.inputNumTelConnection);
		inputMdp = (EditText) findViewById(R.id.inputMdpConnection);
		buttonConnection = (Button) findViewById(R.id.buttonValidateConnection);
	}
	
	public void validerConnexion(View view) throws Exception
	{		
		String numTel = inputNumTel.getText().toString();
		String mdp = inputMdp.getText().toString();
		
		String telPattern = "^((\\+|00)33\\s?|0)[679](\\s?\\d{2}){4}$";
		String mdpPattern = "((?=.*\\d)(?=.*[a-z]).{6,20})";
		
		// Verifications
		// Numero de telephone absent
		if(numTel.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Numéro de téléphone manquant!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Numero de telephone invalide
		if(!numTel.matches(telPattern))
		{
			Toast.makeText(getBaseContext(), "Numéro de téléphone invalide! Veuillez entrer un numéro mobile valable.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Pas d'utilisateur avec ce numero dans la base
		if(Utilisateur.getUtilisateurWithAttribut("numTel", numTel).isEmpty())
		{
			Toast.makeText(getBaseContext(), "Aucun utilisateur lié à ce numéro de téléphone dans la base!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Il y a bien un utilisateur avec le numero indique, on le recupere 
		Utilisateur utilisateurCourant = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numTel).get(0);
		
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
		Toast.makeText(getBaseContext(), "Données correctes, connexion en cours", Toast.LENGTH_SHORT).show();
		Mobay.utilisateurCourant = utilisateurCourant;
		
		// Pour l'instant, test en allant a l'aide => conservation de la variable utilisateurCourant = youpi
		Intent intentTest = new Intent(this, HelpActivity.class);
		startActivity(intentTest);
		
	}
}
