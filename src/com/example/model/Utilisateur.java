package com.example.model;

import com.parse.*;

@ParseClassName("Utilisateur")
public class Utilisateur extends ParseObject
{

	public Utilisateur()
	{
		// Default constructor required
	}
	
	public Utilisateur(String pseudo, String numTel, String mdp, String parrain) 
	{		
		// On renseigne les attributs dans la table
		put("pseudo", pseudo);
		put("numTel", numTel);
		put("mdp", mdp);
		put("parrain", parrain);
	}
	
	public String getPseudo() 
	{
		return getString("pseudo");
	}
	
	public String getNumTel() 
	{
		return getString("numTel");
	}
	
	public String getMdp() 
	{
		return getString("mdp");
	}
	
	public String getParrain() 
	{
		return getString("parrain");
	}
	

	public void setPseudo(String pseudo) 
	{
		put("pseudo", pseudo);
	}

	public void setNumTel(String numTel) 
	{
		put("numTel", numTel);
	}

	public void setMdp(String mdp) 
	{
		put("mdp", mdp);
	}

	public void setParrain(String parrain) 
	{
		put("parrain", parrain);
	}
}
