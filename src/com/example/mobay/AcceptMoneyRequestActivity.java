package com.example.mobay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.BaseBundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.model.Compte;
import com.example.model.Mobay;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.example.model.Utilisateur;
import com.parse.ParseObject;


// faire une méthode débiter et une méthode créditer
// rebiter celui qui a fait la demande d'argent si elle est acceptée.

public class AcceptMoneyRequestActivity extends Activity {
	private static final String TAG = "AcceptMoneyRequestActivity";

	Button menuAccueil = null;
	Button deconnexion = null;
	ListView listMoneyRequestView;
	List<ParseObject> listMoneyRequest = null;
	List<HashMap<String, String>> listMoneyRequestViewListener;
	ListAdapter adapter;
	static double soldeUtilisateurCourant = 0;
	static double montantDebiter = 0;
	int position;
	HashMap<String, String> itemSelected;

	static List<ParseObject> listAccountUtilisateurCourant = null;
	static List<ParseObject> listMoneyRequestUtilisateurCourant = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accept_money_request);

		menuAccueil = (Button) findViewById(R.id.menuAccueil);
		deconnexion = (Button) findViewById(R.id.deconnexion);
		listMoneyRequestView = (ListView) findViewById(R.id.listMoneyRequestView);

		deconnexion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(AcceptMoneyRequestActivity.this, MainActivity.class);
				startActivity(deconnexion);
			}

		});
		menuAccueil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent deconnexion = new Intent(AcceptMoneyRequestActivity.this, MainMenuActivity.class);
				startActivity(deconnexion);
				finish();
			}

		});

		listMoneyRequest = Operation.getOperationWithDestinaireObjectIdAndOperationType(Mobay.utilisateurCourant.getObjectId(), TypeOperation.RECEPTION.ordinal(), false);
		Log.d(TAG, "Des demandes d'argent ont été trouvées pour cet utilisateur, nombre de demande(s) : " + listMoneyRequest.size());

		if (listMoneyRequest.isEmpty()) {
			Toast.makeText(getBaseContext(), "Vous n'avez pas de demande d'argent pour le moment", Toast.LENGTH_SHORT).show();
			return;
		}

		listMoneyRequestViewListener = AcceptMoneyRequestActivity.selectRequestSender(listMoneyRequest);

		adapter = new SimpleAdapter(this, AcceptMoneyRequestActivity.selectRequestSender(listMoneyRequest), android.R.layout.simple_list_item_2, new String[] { "Num", "Montant" }, new int[] { android.R.id.text1, android.R.id.text2 });

		listMoneyRequestView.setAdapter(adapter);

		listMoneyRequestView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int vPosition, long id) {

				position = vPosition;

				itemSelected = (HashMap<String, String>) listMoneyRequestView.getItemAtPosition(position);
				Log.d(TAG, "Text de l'item récupéré: " + itemSelected.toString());

				montantDebiter = Double.valueOf(itemSelected.get("Montant"));

				AlertDialog.Builder alert = new AlertDialog.Builder(AcceptMoneyRequestActivity.this);
				alert.setTitle("Que voulez-vous faire ?");

				alert.setPositiveButton("Valider !", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						listAccountUtilisateurCourant = Compte.getAccountWithUserObjectId(Mobay.utilisateurCourant.getObjectId());
						soldeUtilisateurCourant = (double) ((Compte) listAccountUtilisateurCourant.get(0)).getSolde();

						if ((soldeUtilisateurCourant - montantDebiter) >= 0) {
							Compte cmptUserCour = null;
							Log.d(TAG, "Solde UtilisateurCourant avant operation : " + soldeUtilisateurCourant);
							soldeUtilisateurCourant -= montantDebiter;
							Log.d(TAG, "Solde UtilisateurCourant après operation : " + soldeUtilisateurCourant);
							cmptUserCour = ((Compte) listAccountUtilisateurCourant.get(0));
							cmptUserCour.setSolde(soldeUtilisateurCourant);
							cmptUserCour.saveInBackground();

							// on met l'operation a true pour dire qu'elle a été
							// validé
							Operation op = null;
							op = (Operation) Operation.getOperationWithAttribut("objectId", itemSelected.get("OperationObjectId")).get(0);
							op.setValidationOperation(true);
							op.saveInBackground();

							listMoneyRequestViewListener.remove(position);
							((BaseAdapter) adapter).notifyDataSetChanged();

							Intent acceptMoneyRequestOk = new Intent(AcceptMoneyRequestActivity.this, AcceptMoneyRequestOkActivity.class);
							acceptMoneyRequestOk.putExtra("soldeUtilisateurCourant", soldeUtilisateurCourant);
							acceptMoneyRequestOk.putExtra("montantDebiter", montantDebiter);
							startActivity(acceptMoneyRequestOk);
						} else {
							Toast.makeText(getBaseContext(), "Veuillez créditer votre compte pour accepter cette requête", Toast.LENGTH_SHORT).show();
						}

					}
				});

				alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// On vire le question dialog
						dialog.dismiss();
					}
				});

				AlertDialog ale = alert.create();
				ale.show();

			}

		});

	}

	private static List<HashMap<String, String>> selectRequestSender(List<ParseObject> listMoneyRequest) {
		List<HashMap<String, String>> listNumSenderValueRequest = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;

		String senderObjectId = null;
		String numSender = null;
		String operationObjectId = null;
		String montantOperation = null;

		Utilisateur requestSender = null;

		if (!listMoneyRequest.isEmpty()) {
			for (int i = 0; i < listMoneyRequest.size(); i++) {
				element = new HashMap<String, String>();
				senderObjectId = (String) ((Operation) listMoneyRequest.get(i)).getUtilisateurObjectId();
				Log.d(TAG, "Sender ObjectId: " + senderObjectId);

				operationObjectId = (String) ((Operation) listMoneyRequest.get(i)).getObjectId();
				Log.d(TAG, "Sender ObjectId: " + operationObjectId);
				element.put("OperationObjectId", operationObjectId);

				requestSender = (Utilisateur) (Utilisateur.getUtilisateurWithAttribut("objectId", senderObjectId)).get(0);
				Log.d(TAG, "Sender de la demande d'argent : " + requestSender.getNumTel());

				numSender = (String) ((Utilisateur) (Utilisateur.getUtilisateurWithAttribut("objectId", senderObjectId)).get(0)).getNumTel();
				element.put("Num", numSender);

				montantOperation = String.valueOf(((Operation) listMoneyRequest.get(i)).getMontant());
				element.put("Montant", montantOperation);
				listNumSenderValueRequest.add(element);
			}
		}
		return listNumSenderValueRequest;
	}

}
