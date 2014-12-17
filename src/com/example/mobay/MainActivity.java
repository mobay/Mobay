package com.example.mobay;

import java.util.Date;
import java.util.List;

import com.example.model.Compte;
import com.example.model.Operation;
import com.example.model.TypeOperation;
import com.example.model.Utilisateur;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity 
{
	private static final String TAG = "MainActivity";

	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {	
		super.onCreate(savedInstanceState);

		// Parse: register sublass User
		ParseObject.registerSubclass(Utilisateur.class);
		ParseObject.registerSubclass(Compte.class);
		ParseObject.registerSubclass(Operation.class);
		
		// Parse: set application id and client key
		Parse.initialize(this, "AZ4u27xv2CXc9Y86zo9AswO2aTo70Wb0nnHVuWMj", "zUw4j02xMw3TeBjtqPLRc7hMvSYZSlA3Ndxmjk6K");
		
		// Also in this method, specify a default Activity to handle push notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);
		
		Utilisateur user = new Utilisateur("elPatrate", "02030401", "mdptest", "");
		user.saveInBackground();
				
		Compte account = new Compte("axgrHfavb8", 5000);
		account.saveInBackground();
		
		Operation ope = new Operation("axgrHfavb8", TypeOperation.VIREMENT, 1000, new Date());
		ope.saveInBackground();

		/*ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		query.whereEqualTo("username", "apautrat");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> listObjects, ParseException e) 
			{
				Log.d(TAG, Integer.toString(listObjects.size()));
				if(e == null)
				{
					for(int i = 0; i < listObjects.size(); i++)
					{
						Log.d(TAG, listObjects.get(i).getString("email"));
					}
				}
				else
				{
					Log.e(TAG, "Error retrieving data from Parse, wee why below:");
					e.printStackTrace();
				}
			}
		});*/

        setContentView(R.layout.activity_main);
    }
    
    public void onButtonHelp(View view)
    {
    	setContentView(R.layout.activity_help);
    }
}
