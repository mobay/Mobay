package com.example.model;

import com.parse.*;

@ParseClassName("Compte")
public class Compte extends ParseObject
{

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
}
