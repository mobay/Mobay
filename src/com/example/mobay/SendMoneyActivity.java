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
// soit rajouter un champ à la table operation pour valider un operation quand l'autre la recoit+valide après ça on débite le premier
// operation doit avoir un champ de destinataire sinon comment on fait pour connaître qui on doit donner l'argent
	// comment on fait si un user a plusieur demande d'argent a accepter ??? une par une , on fait une liste
public class SendMoneyActivity extends Activity {

	private static final String TAG = "SendMoneyActivity";

	Button valider = null;
	Button menuAccueil = null;
	Button deconnexion = null;

	private static EditText numOrAlias = null;
	private static EditText montant = null;

	private static Boolean numero = false;
	private static Boolean alias = false;

	String numOrAliasText = null;
	String montantText = null;

	static Boolean isUser = false;

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
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(SendMoneyActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
			}

		});

		valider.setOnClickListener(validerListener);

	}

	private TextWatcher textWatcher = new TextWatcher() {// détecter les
															// caracteres
															// spéciaux avec
															// regexs

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

			String numOrAliasText = null;
			String montantText = null;

			numOrAliasText = SendMoneyActivity.getValueField(numOrAlias.getText().toString());
			montantText = SendMoneyActivity.getValueField(montant.getText().toString());

			Log.d(TAG, "Texte du champ NumOrAlias: " + numOrAliasText);
			Log.d(TAG, "Texte du champ Montant: " + montantText);

			if (numOrAliasText != null) {
				if (montantText != null) {
					if (numOrAlias.length() < 4) {
						numOrAlias.getText().clear();
						Toast.makeText(getBaseContext(), "Le champ \"Num. Mobile ou Alias\" doit contenir 4 caractères au minimum", Toast.LENGTH_SHORT).show();
					} else if (numOrAlias.length() > 20) {
						numOrAlias.getText().clear();
						Toast.makeText(getBaseContext(), "Le champ \"Num. Mobile ou Alias\" ne doit pas dépasser 20 caractères", Toast.LENGTH_SHORT).show();
					} else {
						double dbl = Double.parseDouble(montantText);
						Log.d(TAG, "Montant parse en double : " + dbl);
						if (dbl > 0) {
							SendMoneyActivity.isUserExistsInDB(numOrAliasText);
							Log.d(TAG, "User est-il dans la db ? : " + isUser);
							if (isUser) {
								isUser = false;
								if (numero && !(((Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).get(0)).getObjectId().equals(Mobay.utilisateurCourant.getObjectId()))) {
									Log.d(TAG, "Envoi d'argent : OK");
									numero = false;
									SendMoneyActivity.envoiMoney(dbl);
									Intent sendMoneyOk = new Intent(SendMoneyActivity.this, SendMoneyOkActivity.class);
									startActivity(sendMoneyOk);
								}
								if (alias && !(((Utilisateur) Utilisateur.getUtilisateurWithAttribut("pseudo", numOrAliasText).get(0)).getObjectId().equals(Mobay.utilisateurCourant.getObjectId()))) {
									alias = false;
									Log.d(TAG, "Envoi d'argent : OK");
									SendMoneyActivity.envoiMoney(dbl);
									Intent sendMoneyOk = new Intent(SendMoneyActivity.this, SendMoneyOkActivity.class);
									startActivity(sendMoneyOk);
								} else {
									numOrAlias.getText().clear();
									montant.getText().clear();
									Toast.makeText(getBaseContext(), "Vous ne pouvez pas vous envoyer de l'argent à vous même", Toast.LENGTH_SHORT).show();
								}
							} else {
								Log.d(TAG, "Envoi d'argent : NOK");
								Intent sendMoneyNok = new Intent(SendMoneyActivity.this, SendMoneyNokActivity.class);
								startActivity(sendMoneyNok);

							}
						} else {
							montant.getText().clear();
							Toast.makeText(getBaseContext(), "Le montant doit être supérieur à zéro", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(getBaseContext(), "Merci de rentrer un montant", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getBaseContext(), "Merci de rentrer un numéro de téléphone ou un alias", Toast.LENGTH_SHORT).show();
			}

		}

	};

	private static String getValueField(String str) {

		if (str == null || str.trim().length() == 0) {
			return null;
		} else {
			return str;
		}
	}

	private static void isUserExistsInDB(String str) {

		List listUser = null;
		Log.d(TAG, "Valeur de str dans isUserExistsInDB : " + str);
		if (str.matches("^\\d+$")) {

			listUser = Utilisateur.getUtilisateurWithAttribut("numTel", str);
			Log.d(TAG, "Valeur de listeUser : " + listUser.toString());
			if (!listUser.isEmpty()) {
				numero = true;
				isUser = true;
			}
		} else {

			listUser = Utilisateur.getUtilisateurWithAttribut("pseudo", str);

			if (!listUser.isEmpty()) {
				alias = true;
				isUser = true;
			}

		}

	}

	private static void envoiMoney(double dbl) {

		List listAccount = null;
		double solde = 0;
		String numOrAliasText = SendMoneyActivity.getValueField(numOrAlias.getText().toString());
		// on ne doit pas pouvoir s'envoyer de l'argent a soi même
		Log.d(TAG, "ObjectId de l'utilisateur courant : " + Mobay.utilisateurCourant.getObjectId());
		Log.d(TAG, "ObjectId de l'utilisateur specifie par le champ : " + ((Utilisateur) Utilisateur.getUtilisateurWithAttribut("numTel", numOrAliasText).get(0)).getObjectId());

		Operation op = new Operation(Mobay.utilisateurCourant.getObjectId(), TypeOperation.ENVOI, dbl, new Date());
		op.saveInBackground();
		// on recupere le compte de l'utilisateur courant
		listAccount = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());

		if (listAccount.size() == 0) {

			Compte cmpt = new Compte(Mobay.utilisateurCourant.getObjectId(), 0);
			Log.d(TAG, "Création d'un compte pour l'utilisateur courant");

		} else {
			solde = (double) ((Compte) listAccount.get(0)).getSolde();
			Log.d(TAG, "Solde avant operation : " + solde);

			solde += dbl;
			((Compte) listAccount.get(0)).setSolde(solde);

			((Compte) listAccount.get(0)).saveInBackground();

		}

	}
}
