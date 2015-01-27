package com.example.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.parse.*;

@ParseClassName("Operation")
public class Operation extends ParseObject {

	private static final String TAG = "Model-Operation";

	public Operation() {
		// Default constructor required
	}

	public Operation(String utilisateurObjectId, TypeOperation type, double montant, Date date, String destinataireObjectId, boolean validationOperation) {
		// On renseigne les attributs dans la table
		put("utilisateurObjectId", utilisateurObjectId);
		put("type", type.ordinal());
		put("montant", montant);
		put("date", date);
		put("destinataireObjectId", destinataireObjectId);
		put("validationOperation", validationOperation);
	}
	

	public String getDestinataireObjectId() {
		return getString("destinataireObjectId");
	}

	public String getUtilisateurObjectId() {
		return getString("utilisateurObjectId");
	}

	public TypeOperation getType() {
		return TypeOperation.values()[getInt("type")];
	}

	public double getMontant() {
		return getDouble("montant");
	}

	public Date getDate() {
		return getDate("date");
	}

	public boolean getValidationOperation() {
		return getBoolean("validationOperation");
	}

	public void setValidationOperation(boolean validationOperation) {
		put("validationOperation", validationOperation);
	}

	public void setUtilisateurObjectIdr(String utilisateurObjectId) {
		put("utilisateurObjectId", utilisateurObjectId);
	}

	public void setDestinataireObjectIdr(String utilisateurObjectId) {
		put("destinatiareObjectId", utilisateurObjectId);
	}

	public void setType(TypeOperation type) {
		put("type", type.ordinal());
	}

	public void setMontant(double montant) {
		put("montant", montant);
	}

	public void setDate(Date date) {
		put("date", date);
	}

	// Liste les operations non validées d'un certain type par destinataireObject
	public static List<ParseObject> getOperationWithDestinaireObjectIdAndOperationType(String objectId, int reception, boolean validationOperation) {
		List<ParseObject> listOperation = new ArrayList<ParseObject>();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
		query.whereEqualTo("destinataireObjectId", objectId);
		query.whereEqualTo("type", reception);
		query.whereEqualTo("validationOperation", validationOperation);
		try {
			listOperation = query.find();
		} catch (ParseException e) {
			Log.e(TAG, e.getMessage());
		}

		return listOperation;
	}
	
	public static List<ParseObject> getOperationWithAttribut(String attribut, String valAttribut)
	{
		List<ParseObject> listeUtilisateurs = new ArrayList<ParseObject>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Operation");
		query.whereEqualTo(attribut, valAttribut);
		try
		{
			listeUtilisateurs = query.find();
		}
		catch (ParseException e)
		{
			Log.e(TAG, e.getMessage());
		}
		
		return listeUtilisateurs;
	}

}
