package com.example.mobay;

import com.example.model.Compte;
import com.example.model.Operation;
import com.example.model.Utilisateur;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity 
{
	private static final String TAG = "MainActivity";

	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {	
		super.onCreate(savedInstanceState);

		// Parse: register subclass User
		ParseObject.registerSubclass(Utilisateur.class);
		ParseObject.registerSubclass(Compte.class);
		ParseObject.registerSubclass(Operation.class);
		
		// Parse: set application id and client key
		Parse.initialize(this, "AZ4u27xv2CXc9Y86zo9AswO2aTo70Wb0nnHVuWMj", "zUw4j02xMw3TeBjtqPLRc7hMvSYZSlA3Ndxmjk6K");

        setContentView(R.layout.activity_main);
    }
    
	public void onButtonInscription(View view)
	{
		// L'objet Intent permet de passer d'une activite a une autre
		Intent intentInsciption = new Intent(this, InscriptionActivity.class);
		startActivity(intentInsciption);
	}
	
	public void onButtonConnexion(View view)
	{
		Intent intentConnexion = new Intent(this, ConnectionActivity.class);
		startActivity(intentConnexion);
	}
}
