package com.example.mobay;

import java.util.Date;
import java.util.List;

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

import com.example.model.Compte;
import com.example.model.Mobay;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.example.model.Utilisateur;
import com.parse.ParseObject;
// soit rajouter un champ à la table operation pour valider un operation quand l'autre la recoit+valide après ça on débite le premier
// operation doit avoir un champ de destinataire sinon comment on fait pour connaître qui on doit donner l'argent
	// comment on fait si un user a plusieur demande d'argent a accepter ??? une par une , on fait une liste
public class SendMoneyActivity extends Activity 
{
	private static final String TAG = "SendMoneyActivity";

	Button valider = null;
	Button menuAccueil = null;
	Button deconnexion = null;
	static EditText numOrAlias = null;
	EditText montant = null;
	
	static Utilisateur utilisateurIndique = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_money);

		valider = (Button) findViewById(R.id.valider);
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		montant = (EditText) findViewById(R.id.montant);
		numOrAlias = (EditText) findViewById(R.id.numOrAlias);

		numOrAlias.addTextChangedListener(textWatcher);
		montant.addTextChangedListener(textWatcher);

		deconnexion.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
			}

		});
		valider.setOnClickListener(validerListener);
	}

	// détecter les caracteres spéciaux avec regexs
	private TextWatcher textWatcher = new TextWatcher() 
	{
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

		}
	};

	private OnClickListener validerListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			String numOrAliasText = numOrAlias.getText().toString();
			String montantText = montant.getText().toString();

			Log.d(TAG, "Texte du champ NumOrAlias: " + numOrAliasText);
			Log.d(TAG, "Texte du champ Montant: " + montantText);

			// Verifications
			// Numero / alias absent
			if (numOrAliasText.isEmpty()) 
			{
				Toast.makeText(getBaseContext(), "Merci de rentrer un numéro de téléphone ou un alias!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Montant absent
			if (montantText.isEmpty()) 
			{
				Toast.makeText(getBaseContext(), "Merci de rentrer un montant!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Num / alias < 4 caracteres et > 20 caracteres
			if (numOrAlias.length() < 4 || numOrAlias.length() > 20) 
			{
				Toast.makeText(getBaseContext(), "Le champ \"Num. Mobile ou Alias\" doit contenir entre 4  et 20 caractères!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			double montant = Double.parseDouble(montantText);
			Log.d(TAG, "Montant parse en double : " + montant);
			// Montant < 0
			if (montant <= 0) 
			{
				Toast.makeText(getBaseContext(), "Le montant doit être supérieur à zéro!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Pas d'utilisateur avec ce pseudo ou ce num
			if(Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty()
				&& Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty())
			{
				Log.d(TAG, "User est pas dans la db !");
				Log.d(TAG, "Envoi d'argent : NOK");
				// La, c'est vraiment abusé de foutre une nouvelle activité..
				Intent sendMoneyNok = new Intent(SendMoneyActivity.this, SendMoneyNokActivity.class);
				startActivity(sendMoneyNok);
				return;
			}
			
			Log.d(TAG, "User est bien dans la db !");
			
			// On recupere l'utilisateur avec le num ou l'alias indique
			if(!Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).get(0);
			
			if(!Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).get(0);

			if(utilisateurIndique.getObjectId().matches(Mobay.utilisateurCourant.getObjectId()))
			{
				Toast.makeText(getBaseContext(), "Vous ne pouvez pas vous envoyer de l'argent à vous même", Toast.LENGTH_SHORT).show();
				return;
			}

			Log.d(TAG, "Envoi d'argent : OK");
			SendMoneyActivity.envoiMoney(montant);
			Intent sendMoneyOk = new Intent(SendMoneyActivity.this, SendMoneyOkActivity.class);
			startActivity(sendMoneyOk);
		}
	};

	private static void envoiMoney(double dbl) 
	{
		List<ParseObject> listAccount = null;
		double solde = 0;

		Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.ENVOI, dbl, new Date());
		op.saveInBackground();
		// on recupere le compte de l'utilisateur courant
		listAccount = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());

		if (listAccount.size() == 0) 
		{
			Compte cmpt = new Compte(Mobay.utilisateurCourant.getObjectId(), 0);
			Log.d(TAG, "Création d'un compte pour l'utilisateur courant");
		} else 
		{
			solde = (double) ((Compte) listAccount.get(0)).getSolde();
			Log.d(TAG, "Solde avant operation : " + solde);

			solde += dbl;
			((Compte) listAccount.get(0)).setSolde(solde);
			((Compte) listAccount.get(0)).saveInBackground();
		}
	}
}

