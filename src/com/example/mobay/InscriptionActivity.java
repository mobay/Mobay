package com.example.mobay;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.model.Utilisateur;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InscriptionActivity extends Activity 
{
	private static final String TAG = "InscriptionActivity";
	
	// Variables de la vue
	EditText inputNumTel;
	EditText inputPseudo;
	EditText inputMdp;
	EditText inputMdpAgain;
	Button buttonInscription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		
		// Attribution des variables de la vue
		inputNumTel = (EditText) findViewById(R.id.inputNumTel);
		inputPseudo = (EditText) findViewById(R.id.inputPseudo);
		inputMdp = (EditText) findViewById(R.id.inputMdp);
		inputMdpAgain = (EditText) findViewById(R.id.inputMdpAgain);
		buttonInscription = (Button) findViewById(R.id.buttonInscription);
	}
	
	// Methode appelee lors du clic sur le bouton Inscription
	public void validerInscription(View view) throws Exception
	{
		String numTel = inputNumTel.getText().toString();
		String pseudo = inputPseudo.getText().toString();
		String mdp = inputMdp.getText().toString();
		String mdpAgain = inputMdpAgain.getText().toString();
		
		String telPattern = "^((\\+|00)33\\s?|0)[679](\\s?\\d{2}){4}$";
		String pseudoPattern = "^[a-z0-9_-]{3,15}$";
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
		
		// Doublon numero de telephone
		if(!Utilisateur.getUtilisateurWithAttribut("numTel", numTel).isEmpty())
		{
			Toast.makeText(getBaseContext(), "Ce numéro de téléphone est déjà utilisé!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Pseudo invalide
		if(!pseudo.isEmpty() && !pseudo.matches(pseudoPattern))
		{
			Toast.makeText(getBaseContext(), "Pseudonyme invalide! Les minuscules, chiffres et tirets sont autorisés et le pseudonyme doit comprendre entre 3 et 15 caractères.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Doublon pseudo
		if(!pseudo.isEmpty() && !Utilisateur.getUtilisateurWithAttribut("pseudo", pseudo).isEmpty())
		{
			Toast.makeText(getBaseContext(), "Ce pseudonyme est déjà utilisé!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Mot de passe absent
		if(mdp.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Mot de passe manquant!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Mot de passe invalide
		if(!mdp.matches(mdpPattern))
		{
			Toast.makeText(getBaseContext(), "Mot de passe invalide! Vous devez utiliser des minuscules et au moins un chiffre, le mot de passe doit être compris entre 6 et 20 caractères.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Repeter le mot de passe manquant
		if(mdpAgain.isEmpty())
		{
			Toast.makeText(getBaseContext(), "Vous devez resaisir le mot de passe!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// Repeter le mot de passe invalide
		if(!mdp.matches(mdpAgain))
		{
			Toast.makeText(getBaseContext(), "Vous n'avez pas resaisi le même mot de passe!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// On peut creer l'utilisateur dans Parse
		Utilisateur user = new Utilisateur(pseudo, numTel, crypterMdp(mdp), "");
		user.saveInBackground();
		
		Toast.makeText(getBaseContext(), "Vous avez bien créé un compte sur Mobay!", Toast.LENGTH_SHORT).show();
		
	}
	
	private String crypterMdp(String mdp) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(mdp.getBytes());
		
		byte byteData[] = md.digest();
		
		// Conversion byte en hexa
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < byteData.length; i++)
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

		Log.d(TAG, "Digest MD5 mdp en hexa: " + sb.toString());
		
		return sb.toString();
	}
}
