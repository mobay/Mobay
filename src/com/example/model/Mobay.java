package com.example.model;

import android.app.Application;

public class Mobay extends Application
{
	// Ici, equivalent vairables de session = variables application qui ne seront identiques quelle que soit l'activite
	public static Utilisateur utilisateurCourant = new Utilisateur();
}
