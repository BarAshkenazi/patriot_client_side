package oref.gcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.android.List.MainActivity;
import com.example.android.Network.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class RegisterGcm {
	
	 public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	 public static final String PROPERTY_REG_ID = "registration_id";
	 private static final String PROPERTY_APP_VERSION = "appVersion";
	 private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	 
	 String SENDER_ID = "82205491864";
	 static final String TAG = "GCM";
	    GoogleCloudMessaging gcm;
	    AtomicInteger msgId = new AtomicInteger();
	    SharedPreferences prefs;
	    Context context;
	    String regid;
	    MainActivity activity;
	    
	    public RegisterGcm(MainActivity activity)
	    {
	    	this.activity = activity;
	    }

	    
	    
	    public String getRegId()
	    {
	        if (checkPlayServices()) {
	            gcm = GoogleCloudMessaging.getInstance(this.activity);
	            regid = getRegistrationId(context);

	            if (regid.isEmpty()) {
	                registerInBackground();
	                
	                while(regid.isEmpty())
	                {
	                	regid = getRegistrationId(context);
	                }
	                
	            }
	        } else {
	            Log.i(TAG, "No valid Google Play Services APK found.");
	        }
	    	return regid;
	    }
	    
	    
	    private String getRegistrationId(Context context) {
	        final SharedPreferences prefs = getGCMPreferences(context);
	        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	        if (registrationId.isEmpty()) {
	            Log.i(TAG, "Registration not found.");
	            return "";
	        }

	       return registrationId;
	    }
	 
	    
	    private SharedPreferences getGCMPreferences(Context context) {
	        // This sample app persists the registration ID in shared preferences, but
	        // how you store the regID in your app is up to you.
	        return this.activity.getSharedPreferences(MainActivity.class.getSimpleName(),
	                Context.MODE_PRIVATE);
	    }
	 
	    private boolean checkPlayServices() {
	        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.activity);
	        if (resultCode != ConnectionResult.SUCCESS) {
	            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	                GooglePlayServicesUtil.getErrorDialog(resultCode, this.activity,
	                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
	            } else {
	               // Log.i(TAG, "This device is not supported.");
	                this.activity.finish();
	            }
	            return false;
	        }
	        return true;
	    }
	    
	    private void storeRegistrationId(Context context, String regId) {
	        final SharedPreferences prefs = getGCMPreferences(context);
	       // int appVersion = getAppVersion(context);
	      //  Log.i(TAG, "Saving regId on app version " + appVersion);
	        SharedPreferences.Editor editor = prefs.edit();
	        editor.putString(PROPERTY_REG_ID, regId);
	        //editor.putInt(PROPERTY_APP_VERSION, appVersion);
	        editor.commit();
	    }
	    
	    private void registerInBackground() {
	        new AsyncTask<Void,Void,Void>() {
	            @Override
	            protected Void doInBackground(Void... parameters) {
	           // 	android.os.Debug.waitForDebugger();
	                String msg = "";
	                try {
	                    if (gcm == null) {
	                        gcm = GoogleCloudMessaging.getInstance(context);
	                    }
	                    regid = gcm.register(SENDER_ID);
	                    
	                    //	android.os.Debug.waitForDebugger();
                        // test sending POST request
                        Map<String, String> params = new HashMap<String, String>();
                        String requestURL = "http://172.20.10.7:80/registerUser";
                        params.put("regid", regid);
                         
                        try {
                            HttpUtility.sendPostRequest(requestURL, params);
                            System.out.println("request sent to:" + requestURL);
                            String[] response = HttpUtility.readMultipleLinesRespone();
                            for (String line : response) {
                                System.out.println(line);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            System.out.println("failed");
                        }
                        HttpUtility.disconnect();

	                    // Persist the regID - no need to register again.
	                    storeRegistrationId(context, regid);
	                } catch (IOException ex) {
	                	System.out.println("failed");
	                }
	                return null;
	            }
	        }.execute();
	    }

}
