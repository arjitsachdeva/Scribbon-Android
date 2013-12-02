package com.scribbon;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MessagesActivity extends Activity {
	
	// declarations
	ListView people;
	Button compose;
	UserFunctions userFunction = new UserFunctions();
	String[] names, uids;
	String uid, fid;
	int iSize = 0;
	
    //setting the keys and tags
	private static String KEY_CONVERSATION_PEOPLE = "conversationists";
	private static String TAG_NAME = "name";
	private static String TAG_UID = "uid";
	private static String TAG_FID = "fid";
	ArrayAdapter<String> adapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_layout);
        
        //attaching the elements in the xml
        people = (ListView)findViewById(R.id.lvPeople);
        compose = (Button)findViewById(R.id.bCompose);
        
        
        DatabaseHandler db = new DatabaseHandler(getBaseContext());
        // getting user details from internal db
    	HashMap<String, String> details = db.getUserDetails();
    	uid = details.get("id").toString();
        
        try {
    		// Getting the JSON Results
    		JSONObject json = userFunction.getConversationists(uid); 
    	    // Getting Array of People in Conversation
    		if(json != null){
    			
    	    JSONArray conversationists = json.getJSONArray(KEY_CONVERSATION_PEOPLE);
    	    names = new String[conversationists.length()];
    	    uids = new String[conversationists.length()];
    	    iSize = conversationists.length();
    	    // looping through All Requests
    	    for(int i = 0; i < conversationists.length(); i++){
    	        JSONObject c = conversationists.getJSONObject(i);
    	        // Storing each json item in variable
    	        names[i] = c.getString(TAG_NAME);
    	        uids[i] = c.getString(TAG_UID);
    	    	}
    		}
    	    
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	} catch (NullPointerException e){
    		//Will be caught when no Internet connectivity is present, can edit the message stored in the values/string/no_internet
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    	}
        
        compose.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), SelectFriendActivity.class);
                startActivityForResult(i, 1);
			}
		});
        
        // checks whether at least one entry is available
        if(iSize>0){
	        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
	        people.setAdapter(adapter);
	        people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
				public void onItemClick(AdapterView<?> arg0, View view, int position,
						long arg3) {
					// TODO Auto-generated method stub
	                 //describing the elements to be sent along with the new activity call
					Intent conversation = new Intent(getApplicationContext(), ConversationActivity.class);
					conversation.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					conversation.putExtra(TAG_UID, uid);
					conversation.putExtra(TAG_FID, uids[position]);
		            startActivity(conversation);
				}
			});
        }
    }
    
    // Calls another activity for selecting the friend and returns with value after selection
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1) {
    	     if(resultCode == RESULT_OK){
    	    	 
    	      //receiving the string item sent from the previous activity bundle
    	      String rUid = data.getStringExtra("uid");
    	      String rFid = data.getStringExtra("fid");
    	      Intent conversation = new Intent(getApplicationContext(), ConversationActivity.class);
				conversation.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				conversation.putExtra(TAG_UID, rUid);
				conversation.putExtra(TAG_FID, rFid);
	            startActivity(conversation);
    	     }
    	}

    	if (resultCode == RESULT_CANCELED) {
    	     Toast.makeText(getApplicationContext(), "None selected", Toast.LENGTH_SHORT).show();
    	}
   }	
}