package com.scribbon;

import java.util.HashMap;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
 
public class RequestActivity extends Activity {
    // All static variables
	String siteURL = "http://10.0.2.2/android_login_api/";
//  String siteURL = "http://agrimainfotech.com/test/android_login_api/";
    String[] names, emails, pps, uids, types;
    String uid;
    
    //setting the keys and tags
    private static String KEY_REQUESTS= "requests";
    private static String KEY_ID = "id";
    private static String TAG_NAME= "name";
    private static String TAG_PROFILE_PIC= "pp";
    private static String TAG_EMAIL= "email";
    private static String TAG_UID= "uid";
    private static String TAG_TYPE= "type";
 
    ListView list;
    RequestLazyAdapter adapter;
    UserFunctions userFunction = new UserFunctions();

 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_layout);
        
        // Getting user details from internal database
        DatabaseHandler db = new DatabaseHandler(getBaseContext());
    	HashMap<String, String> details = db.getUserDetails();
    	uid = details.get(KEY_ID).toString();

        try {
    		// Getting the JSON Results
    		JSONObject json = userFunction.getRequests("1"); 
    	    // Getting Array of Requests
    		if(json != null){
    			
    	    JSONArray requests = json.getJSONArray(KEY_REQUESTS);
    	    names = new String[requests.length()];
    	    pps = new String[requests.length()];
    	    emails = new String[requests.length()];
    	    uids = new String[requests.length()];
    	    types = new String[requests.length()];
    	    // looping through All Requests
    	    for(int i = 0; i < requests.length(); i++){
    	        JSONObject c = requests.getJSONObject(i);
    	        // Storing each json item in variable
    	        names[i] = c.getString(TAG_NAME);
    	        pps[i] = c.getString(TAG_PROFILE_PIC);
    	        emails[i] = c.getString(TAG_EMAIL);
    	        uids[i] = c.getString(TAG_UID);
    	        types[i] = c.getString(TAG_TYPE);
    	    	}
    		}
    	    
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	} catch (NullPointerException e){
    		//Will be caught when no Internet connectivity is present, can edit the message stored in the values/string/no_internet
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    	}
 
        //attaching the elements in the xml
        list=(ListView)findViewById(R.id.lRequests);
 
        adapter = new RequestLazyAdapter(this, names, emails, pps, uids, types);
        list.setAdapter(adapter);
        
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {
 
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    
    // will be called when the user clicks the Accept button, set using OnClick function in the xml
    public void acceptClick (View v) {
    	int position = list.getPositionForView(v);
		JSONObject json = userFunction.acceptFriend(uids[position], uid);
		// Getting Array of Result
		if(json != null){
    	    String result;
    	    try {
				result = json.getString("success");
	    	    if(result.equals("0")){
	    	    	Toast.makeText(getApplicationContext(), "IO Error - Try Again", Toast.LENGTH_SHORT).show();
	    	    } else if(result.equals("1")){
	    	    	Toast.makeText(getApplicationContext(), "Accepted", Toast.LENGTH_SHORT).show();	
	    	    	finish();
	    	    	startActivity(getIntent());
	    		}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e){
	    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
	    	}
    	}
    }
    
    // will be called when the user clicks the Reject button, set using OnClick function in the xml
    public void rejectClick (View v) {
    	int position = list.getPositionForView(v);
		JSONObject json = userFunction.rejectFriend(uids[position], uid);
		// Getting Array of Result
		if(json != null){
    	    String result;
    	    try {
				result = json.getString("success");
	    	    if(result.equals("0")){
	    	    	Toast.makeText(getApplicationContext(), "IO Error - Try Again", Toast.LENGTH_SHORT).show();
	    	    } else if(result.equals("1")){
	    	    	Toast.makeText(getApplicationContext(), "Rejected", Toast.LENGTH_SHORT).show();	
	    	    	finish();
	    	    	startActivity(getIntent());
	    		}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e){
	    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
	    	}
    	}
    }
}
