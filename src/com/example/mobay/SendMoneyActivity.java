package com.example.mobay;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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

//  pour valider un operation quand l'autre la recoit+valide après ça on débite le premier ==> Boolean

public class SendMoneyActivity extends Activity {
	private static final String TAG = "SendMoneyActivity";

	Button valider = null;
	Button menuAccueil = null;
	Button deconnexion = null;
	static EditText numOrAlias = null;
	static EditText montant = null;
	static double soldeUtilisateurCourant = 0;
	static double soldeDestinataire = 0;

	static List<ParseObject> listAccountUtilisateurCourant = null;
	static List<ParseObject> listAccountUtilisateurDestinataire = null;

	static Utilisateur utilisateurIndique = null; // l'utilisateur vers qui on
													// envoie de l'argent

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_money);

		valider = (Button) findViewById(R.id.valider);
		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		montant = (EditText) findViewById(R.id.montant);
		numOrAlias = (EditText) findViewById(R.id.numOrAlias);

		numOrAlias.addTextChangedListener(textWatcher);
		montant.addTextChangedListener(textWatcher);

		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});
		valider.setOnClickListener(validerListener);
	}

	// détecter les caracteres spéciaux avec regexs
	private TextWatcher textWatcher = new TextWatcher() {
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
				sendMoneyNok.putExtra("soldeUtilisateurCourant", soldeUtilisateurCourant);
				startActivity(sendMoneyNok);
				return;
			}

			Log.d(TAG, "User est bien dans la db !");

			// On recupere l'utilisateur avec le num ou l'alias indique
			if (!Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).get(0);

			if (!Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).isEmpty())
				utilisateurIndique = (Utilisateur) Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).get(0);

			if (utilisateurIndique.getObjectId().matches(Mobay.utilisateurCourant.getObjectId())) {
				Toast.makeText(getBaseContext(), "Vous ne pouvez pas vous envoyer de l'argent à vous même", Toast.LENGTH_SHORT).show();
				return;
			}

			Log.d(TAG, "Envoi d'argent : OK");
			SendMoneyActivity.envoiMoney(montantDouble);
			if ((soldeUtilisateurCourant - montantDouble) >= 0) {
				Intent sendMoneyOk = new Intent(SendMoneyActivity.this, SendMoneyOkActivity.class);
				sendMoneyOk.putExtra("soldeUtilisateurCourant", soldeUtilisateurCourant);
				startActivity(sendMoneyOk);
			} else {
				Toast.makeText(getBaseContext(), "Vous ne pouvez enovyer au maximum que le solde actuel de votre compte", Toast.LENGTH_SHORT).show();
			}

		}
	};

	public static void envoiMoney(double dbl) {
		Compte cmptDest = null;
		Compte cmptUserCour = null;

		if (dbl != -1) {
			// ici on fait un envoi de l'utilisateur courant à l'utilisateur
			// indiqué
			Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.ENVOI, dbl, new Date(), utilisateurIndique.getObjectId(), true);
			op.saveInBackground();
			// ici on fait une reception pour l'utilisateur indique de l'utilisateur courant 
			Operation opa = new Operation(utilisateurIndique.getObjectId(), TypeOperation.RECEPTION, dbl, new Date(), Mobay.utilisateurCourant.getObjectId() , true);
			opa.saveInBackground();

			// on recupere le compte de l'utilisateur destinataire
			listAccountUtilisateurDestinataire = Compte.getAccountWithUserObjectId(utilisateurIndique.getObjectId());
		}
		// on recupere le compte de l'utilisateur courant
		listAccountUtilisateurCourant = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());

		if (listAccountUtilisateurCourant.isEmpty()) {
			cmptUserCour = new Compte(Mobay.utilisateurCourant.getObjectId(), 0);
			cmptUserCour.saveInBackground();
			Log.d(TAG, "Création d'un compte pour l'utilisateur courant");
			// on actualise la liste
			listAccountUtilisateurCourant = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());
		} else if (dbl != -1 && listAccountUtilisateurDestinataire.isEmpty()) {
			cmptDest = new Compte(utilisateurIndique.getObjectId(), 0);
			cmptDest.saveInBackground();
			Log.d(TAG, "Création d'un compte pour l'utilisateur destinataire");
			// on actualise la liste
			listAccountUtilisateurDestinataire = Compte.getAccountWithUserObjectId(utilisateurIndique.getObjectId());
		} else {
			Log.d(TAG, "On procède à l'envoi !");
		}

		// recupération des soldes
		if (dbl != -1) {
			soldeDestinataire = (double) ((Compte) listAccountUtilisateurDestinataire.get(0)).getSolde();
		}
		soldeUtilisateurCourant = (double) ((Compte) listAccountUtilisateurCourant.get(0)).getSolde();

		if (dbl != -1 && (soldeUtilisateurCourant - dbl) >= 0) {

			// on crédite le compte du destinataire
			Log.d(TAG, "Solde Destinataire avant operation : " + soldeDestinataire);
			soldeDestinataire = Compte.arrondir((soldeDestinataire + dbl), 2);
			Log.d(TAG, "Solde Destinataire après operation : " + soldeDestinataire);
			cmptDest = ((Compte) listAccountUtilisateurDestinataire.get(0));
			cmptDest.setSolde(soldeDestinataire);
			cmptDest.saveInBackground();

			// on débite le compte de l'utilisateur courant
			Log.d(TAG, "Solde UtilisateurCourant avant operation : " + soldeUtilisateurCourant);
			soldeUtilisateurCourant = Compte.arrondir((soldeUtilisateurCourant - dbl), 2);
			Log.d(TAG, "Solde UtilisateurCourant après operation : " + soldeUtilisateurCourant);
			cmptUserCour = ((Compte) listAccountUtilisateurCourant.get(0));
			cmptUserCour.setSolde(soldeUtilisateurCourant);
			cmptUserCour.saveInBackground();
		}
	}
}
