package com.example.model;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.parse.*;

@ParseClassName("Utilisateur")
public class Utilisateur extends ParseObject
{
	private static final String TAG = "Model-Utilisateur";
	
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
	
	public static List<ParseObject> getUtilisateurWithAttribut(String attribut, String valAttribut)
	{
		List<ParseObject> listeUtilisateurs = new ArrayList<ParseObject>();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Utilisateur");
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
	

	
	public static String crypterMdp(String mdp) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(mdp.getBytes());
		
		byte byteData[] = md.digest();
		
		// Conversion byte en hexa
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < byteData.length; i++)
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

		Log.d(TAG, "Digest MD5 mdp en hexa: " + sb.toString());
		
		return sb.toString();
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
