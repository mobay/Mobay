package com.example.model;

import java.util.Date;

import com.parse.*;

@ParseClassName("Operation")
public class Operation extends ParseObject
{

	public Operation()
	{
		// Default constructor required
	}
	
	public Operation(String utilisateurObjectId, TypeOperation type, double montant, Date date, String destinataireObjectId) 
	{		
		// On renseigne les attributs dans la table
		put("utilisateurObjectId", utilisateurObjectId); 
		put("type", type.ordinal());
		put("montant", montant);
		put("date", date);
		put("destinataireObjectId",destinataireObjectId);
	}
	
	public String getDestinataireObjectId() 
	{
		return getString("destinataireObjectId");
	}
	
	public String getUtilisateurObjectId() 
	{
		return getString("utilisateurObjectId");
	}
	
	public TypeOperation getType()
	{
		return TypeOperation.values()[getInt("type")];
	}
	
	public double getMontant() 
	{
		return getDouble("montant");
	}
	
	public Date getDate()
	{
		return getDate("date");
	}


	public void setUtilisateurObjectIdr(String utilisateurObjectId) 
	{
		put("utilisateurObjectId", utilisateurObjectId);
	}
	
	public void setDestinataireObjectIdr(String utilisateurObjectId) 
	{
		put("destinatiareObjectId", utilisateurObjectId);
	}

	public void setType(TypeOperation type)
	{
		put("type", type.ordinal());
	}
	
	public void setMontant(double montant) 
	{
		put("montant", montant);
	}
	
	public void setDate(Date date)
	{
		put("date", date);
	}
}
