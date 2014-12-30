package com.example.mobay;

import com.example.model.Mobay;
import com.example.model.Utilisateur;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HelpActivity extends Activity 
{
	private static final String TAG = "HelpActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
				
		Log.d(TAG, "Accès à l'aide en tant que " + Mobay.utilisateurCourant.getNumTel());
	}
}
