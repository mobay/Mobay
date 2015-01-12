package com.example.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.*;

@ParseClassName("Compte")
public class Compte extends ParseObject
{

	private static final String TAG = "Model-Compte";
	
	public Compte()
	{
		// Default constructor required
	}
	
	public Compte(String utilisateurObjectId, double solde) 
	{		
		// On renseigne les attributs dans la table
		put("utilisateurObjectId", utilisateurObjectId);
		put("solde", solde);
	}
	
	public String getUtilisateurObjectId() 
	{
		return getString("utilisateurObjectId");
	}
	//attention à la casse
	public double getSolde() 
	{
		return getDouble("solde");
	}


	public void setUtilisateurObjectId(String utilisateurObjectId) 
	{
		put("utilisateurObjectId", utilisateurObjectId);
	}

	public void setSolde(double solde) 
	{
		put("solde", solde);
	}
	
	public static List<ParseObject> getAccountWithUserObjectId(String objectId)
	{
		List<ParseObject> listAccount = new ArrayList<ParseObject>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Compte");
		query.whereEqualTo("utilisateurObjectId", objectId);
		try
		{
			listAccount = query.find();
		}
		catch (ParseException e)
		{
			Log.e(TAG, e.getMessage());
		}
		
		return listAccount;
	}
	
	public static double arrondir(double A, int B) {
		  return (double) ( (int) (A * Math.pow(10, B) + .5)) / Math.pow(10, B);
		}
	
}
