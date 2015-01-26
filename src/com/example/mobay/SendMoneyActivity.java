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

import com.example.model.Compte;
import com.example.model.Mobay;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.example.model.Utilisateur;


public class SendMoneyActivity extends Activity {
	private static final String TAG = "SendMoneyActivity";

	Button valider = null;
	Button menuAccueil = null;
	Button deconnexion = null;
	static EditText numOrAlias = null;
	static EditText montant = null;
	static double soldeDestinataire = 0;

	static Compte accountUtilisateurDestinataire = null;

	static Utilisateur utilisateurIndique = null; // l'utilisateur vers qui on envoie de l'argent

	@Override
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
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainActivity.class);
				deconnexion.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(deconnexion);
				finish();
			}
		});
		
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainMenuActivity.class);
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
				Toast.makeText(getBaseContext(), "Merci de rentrer un numéro de téléphone ou un alias!", Toast.LENGTH_SHORT).show();
				return;
			}

			// Montant absent
			if (montantText.isEmpty()) {
				Toast.makeText(getBaseContext(), "Merci de rentrer un montant!", Toast.LENGTH_SHORT).show();
				return;
			}

			// Num / alias < 4 caracteres et > 20 caracteres
			if (numOrAlias.length() < 4 || numOrAlias.length() > 20) {
				Toast.makeText(getBaseContext(), "Le champ \"Num. Mobile ou Alias\" doit contenir entre 4 et 20 caractères!", Toast.LENGTH_SHORT).show();
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

			// Pas d'utilisateur avec ce pseudo ou ce num
			if (Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty() && Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty()) {
				Log.d(TAG, "User est pas dans la db !");
				Log.d(TAG, "Envoi d'argent : NOK");
				SendMoneyActivity.envoiMoney(-1);// -1 permet de récuperer le
													// solde du compte de
													// l'utilisateur courant
				Intent sendMoneyNok = new Intent(SendMoneyActivity.this, SendMoneyNokActivity.class);
				sendMoneyNok.putExtra("soldeUtilisateurCourant", Mobay.compteUtilisateurCourant.getSolde());
				startActivity(sendMoneyNok);
				return;
			}

			Log.d(TAG, "User est bien dans la db !");

			// On recupere l'utilisateur avec le num ou l'alias indique
			if (!Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).get(0);

			if (!Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).get(0);

			// L'utilisateur a entre son num ou pseudo
			if (utilisateurIndique.getObjectId().matches(Mobay.utilisateurCourant.getObjectId())) {
				Toast.makeText(getBaseContext(), "Vous ne pouvez pas vous envoyer de l'argent à vous même", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Le compte de l'utilisateur contient assez d'argent
			if((Mobay.compteUtilisateurCourant.getSolde() - montantDouble) <= 0)
			{
				Toast.makeText(getBaseContext(), "Vous n'avez pas assez d'argent sur votre compte pour effectuer cet envoi.", Toast.LENGTH_LONG).show();
				return;
			}
			
			Log.d(TAG, "Envoi d'argent : OK");

			SendMoneyActivity.envoiMoney(montantDouble);
			
			Intent sendMoneyOk = new Intent(SendMoneyActivity.this, SendMoneyOkActivity.class);
			sendMoneyOk.putExtra("soldeUtilisateurCourant", Mobay.compteUtilisateurCourant.getSolde());
			startActivity(sendMoneyOk);
		}
	};

	public static void envoiMoney(double montant) 
	{
		// Ici on fait un envoi de l'utilisateur courant à l'utilisateur indiqué
		Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.ENVOI, montant, new Date(), utilisateurIndique.getObjectId(), true);
		op.saveInBackground();

		// Ici on fait une réception pour l'utilisateur indiqué de l'utilisateur courant 
		Operation opa = new Operation(utilisateurIndique.getObjectId(), TypeOperation.RECEPTION, montant, new Date(), Mobay.utilisateurCourant.getObjectId() , true);
		opa.saveInBackground();

		// On recupere le compte de l'utilisateur destinataire
		accountUtilisateurDestinataire = Compte.getAccountWithUserObjectId(utilisateurIndique.getObjectId());

		Log.d(TAG, "On procède à l'envoi !");

		// Récupération des soldes
		soldeDestinataire = accountUtilisateurDestinataire.getSolde();

		// On crédite le compte du destinataire
		Log.d(TAG, "Solde Destinataire avant operation : " + soldeDestinataire);
		soldeDestinataire = Compte.arrondir((soldeDestinataire + montant), 2);
		Log.d(TAG, "Solde Destinataire après operation : " + soldeDestinataire);
		accountUtilisateurDestinataire.setSolde(soldeDestinataire);
		accountUtilisateurDestinataire.saveInBackground();

		// on débite le compte de l'utilisateur courant
		Log.d(TAG, "Solde UtilisateurCourant avant operation : " + Mobay.compteUtilisateurCourant.getSolde());
		Mobay.compteUtilisateurCourant.setSolde(Compte.arrondir((Mobay.compteUtilisateurCourant.getSolde() - montant), 2));
		Log.d(TAG, "Solde UtilisateurCourant après operation : " + Mobay.compteUtilisateurCourant.getSolde());
		Mobay.compteUtilisateurCourant.saveInBackground();
	}
}
