package com.example.android.List;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.android.City.City;
import com.example.android.City.CityOperations;
import com.example.android.DBConn.DataBaseWrapper;
import com.example.android.Network.HttpUtility;
import com.example.android.Network.Utils;
import com.example.android.map.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;

import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends Activity {
	  // more efficient than HashMap for mapping integers to objects
	  SparseArray<Group> groups = new SparseArray<Group>();
	  private CityOperations cityDBoperation;
	  
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
	    		.getBoolean("isfirstrun", true);
	    
	    if (cityDBoperation == null)
	    {
	    	cityDBoperation = new CityOperations(this);
	        cityDBoperation.open();
	    }
	    
	    // Checking if there is a need to load the date from the db.
	    if (isFirstRun)
	    {
	    	 Toast.makeText(MainActivity.this, "���� ������...",
	 	            Toast.LENGTH_LONG).show();
	    	 
	    	 getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
	    		.putBoolean("isfirstrun", false).commit();
	    	 
	         /// getting cities from the server
	    	 getDataFromServer();
	    	 
	    }
	    else
	    {
	    	// Getting all of the city from the local db.
		    createData(null);
	    }
	    
	    // inittialize save chosen cities button.
	    initSaveDefaultButton();
	    
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
	    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this, groups);
	    listView.setAdapter(adapter);
	  }

	  

	  private void createData(ArrayList<City> areas) {
		  
		  Group chosenCity = new Group("����� ������ ������");
		  
		  Group allCities = new Group("��� �����");
		 
		  // Checking if this is the first time.
		  // is this is not the first time - the areas is null
		  if (areas == null)
		  {
			  ArrayList<City> cities = cityDBoperation.getAllCities();
			  
			  
			// Displaying all of the cities.
			  for (City currentCity : cities)
			  {
				  if (currentCity.getIsChosen().equals("Y"))
				  {
					  chosenCity.cities.add(currentCity);
				  }
				  else
				  {
					  allCities.cities.add(currentCity);
				  }
			  }
		  }
		  else
		  {
			  // Displaying all of the cities.
			  for (City currentCity : areas)
			  {
				  allCities.cities.add(currentCity);
			  }
		  }
		  
		  MyExpandableListAdapter.restCities = 
				  (ArrayList<City>)allCities.cities.clone();
		  MyExpandableListAdapter.citiesToAlert = 
				  (ArrayList<City>)chosenCity.cities.clone(); 
		  
		  groups.append(0, chosenCity);
		  groups.append(1, allCities);
	  }
	  
	  private void getDataFromServer()
	  {
		  new AsyncTask<Void, Void, String>()
		  {

			@Override
			protected String doInBackground(Void... params) {
				String requestURL = "http://testoref.herokuapp.com/getAreas";
				String[] areas = null;
				try {
					HttpUtility.sendGetRequest(requestURL);
					areas = HttpUtility.readMultipleLinesRespone();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
				
				HttpUtility.disconnect();
				
				return Utils.convertStringArrayToString(areas);
			}
		  		 

		@Override
		  protected void onPostExecute(String areas)
		  {
			  // inserting all of the cities to the local db, saving the data.
			 ArrayList<City> areasList = addAreasToDB(areas);
			
			createData(areasList);
			
			ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
		    MyExpandableListAdapter adapter = 
		    		new MyExpandableListAdapter(MainActivity.this, groups);
		    listView.setAdapter(adapter);
			 
		  }
		}.execute();
	  }
	  
	  
	// The method aads the area from the servers to the dB 
	// in the first time the app is working.
	private ArrayList<City> addAreasToDB(String areas) {
			
			ArrayList<City> retrievedAreas = new ArrayList<City>();
			
			try {
				
				JSONObject obj = new JSONObject(areas);
				
				JSONArray areasArray = obj.getJSONArray("areas");
				
				for(int areaIter = 0; areaIter < areasArray.length(); areaIter++)
				{
					String currArea = (String)areasArray.get(areaIter);
					City cityObj = cityDBoperation.addCity(currArea,"N");
					retrievedAreas.add(cityObj);
				}
				
				
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		  	
			return retrievedAreas;
		}


	
	// Init the chosen cities button.
	private void initSaveDefaultButton() {
		
		ImageButton saveBtn = (ImageButton)findViewById(R.id.saveBtn);
		
		// set up a click listener.
		saveBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// save the data in the server. 
				saveChosenCiteisInServer();

			}
		});
		
	}


	// save the changes that the user did in the alerted cities.
	// the function updates in the server the cities to alert 
	// the user.
	protected void saveChosenCiteisInServer() {
		
		new AsyncTask<Void, Void, String>()
		  {

			@Override
			protected String doInBackground(Void... params) {
				String requestURL = "http://172.20.10.7:80/setUserAreas";
				String answer = null;
				Map<String, String> parms = new HashMap<String, String>();
				
				String citiesJson = 
						Utils.convertArrayToJson(MyExpandableListAdapter.citiesToAlert);
				try {
					
					parms.put("areas",citiesJson);
					parms.put("regId", "1234");
					
					HttpUtility.sendPostRequest
					(requestURL, parms);
					
					answer = HttpUtility.readSingleLineRespone();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
				
				HttpUtility.disconnect();
				
				return answer;
			}
		  		 

		@Override
		  protected void onPostExecute(String answer)
		  {
			 
			 if (answer.contains("200"))
			 {
				// save the data in our DB.
				saveChosenCiteisInDB();
				
		    	 Toast.makeText(MainActivity.this, "������� ����� ������",
			 	            Toast.LENGTH_LONG).show();
			 }
			 else
			 {
				 Toast.makeText(MainActivity.this, "������ ����� ��� ����� �������..",
			 	            Toast.LENGTH_LONG).show();
			 }
			 
		  }
		}.execute();

		
		
	}
	
	
	protected void saveChosenCiteisInDB() {
		
		boolean isUpdated = true;
		
		// Updateing the relevant cities.
		for (City curr : MyExpandableListAdapter.changes)
		{
			 isUpdated = cityDBoperation.updateCity(curr);
			 
			 if (!isUpdated)
			 {
				 break;
			 }
		}
		
	}
	
	
} 

/*public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
   
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.main, menu);
      
      return true;
    }
    
    public void show(View view)
    {
    	Intent intent = new Intent(this,ShowListActivity.class);
    	
    	startActivity(intent);
    }
}*/
