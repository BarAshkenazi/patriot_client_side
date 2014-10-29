package com.example.android.Network;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.City.City;

public class Utils {

	public static String convertStringArrayToString(String[] areas) {
		StringBuilder strBuilder = new StringBuilder();
		
		for (String value : areas)
		{
			strBuilder.append(value);
		}
		
		return strBuilder.toString();
	}
	
	public static String convertArrayToJson (ArrayList<City> cities)
	{
		JSONObject obj = null;
		
		try {
			JSONArray citiesArray = new JSONArray(cities);
			obj = new JSONObject();
			
				obj.put("areas", citiesArray);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		return (obj.toString());
	}
}
