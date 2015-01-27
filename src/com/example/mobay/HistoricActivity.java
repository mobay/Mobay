package com.example.mobay;

import java.text.SimpleDateFormat;
import java.util.List;

import com.example.model.Mobay;
import com.example.model.Operation;
import com.parse.ParseObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class HistoricActivity extends Activity 
{
	private static final String TAG = "HistoricActivity";
	
	GridView gridView;
	final int NBCOLONNES = 3;
	
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historic);
		
		gridView = (GridView) findViewById(R.id.gridView1);

		List<ParseObject> operationsUtilisateurCourant = Operation.getOperationWithAttribut("utilisateurObjectId", Mobay.utilisateurCourant.getObjectId());
		int nbOperations = operationsUtilisateurCourant.size();
		if(nbOperations > 0)
		{
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

			for(int i = 0; i < nbOperations; i++)
			{
				Operation operationCourante = (Operation) operationsUtilisateurCourant.get(i);
				for(int j = 0; j < NBCOLONNES; j++)
				{
					// COLONNE DATE
					if(j == 0)
					{
						adapter.add(dateFormat.format(operationCourante.getDate()));
					}

					// COLONNE Type Operation
					else if(j == 1)
					{
						switch(operationCourante.getType())
						{
						case ENVOI:
							adapter.add("Envoi");
							break;
						case RECEPTION:
							adapter.add("Réception");
							break;
						case RECHARGE:
							adapter.add("Recharge");
							break;
						case VIREMENT:
							adapter.add("Virement");
							break;
						}
					}

					// COLONNE Montant
					else
					{
						adapter.add(Double.toString(operationCourante.getMontant()));
					}
				}
			}
			gridView.setAdapter(adapter);
		}
	}
}
