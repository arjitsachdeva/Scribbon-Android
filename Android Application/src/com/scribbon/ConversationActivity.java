package com.scribbon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ConversationActivity extends Activity{

	ListView conversation;
	EditText message;
	Button send, clear;
	String uid, fid;
	UserFunctions userFunction = new UserFunctions();
	CustomArrayAdapter adapter;
	int iSize = 0;
	
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	
	String[] names, uids, messages, times;
	
    //setting the keys and tags
	private static String TAG_NAME = "name";
	private static String TAG_UID = "uid";
	private static String TAG_MESSAGE = "message";
	private static String TAG_TIME = "time";
	private static String KEY_CONVERSATION = "conversation";
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation_layout);
		
		conversation = (ListView)findViewById(R.id.lvConversation);
		message = (EditText)findViewById(R.id.etMessage);
		send = (Button)findViewById(R.id.bSend);
		clear = (Button)findViewById(R.id.bClear);
		
        //receiving the string items sent from the previous activity bundle
		uid = this.getIntent().getStringExtra("uid");
		fid = this.getIntent().getStringExtra("fid");
		
		//function for populating the conversation
		populateConversation();
		
		//will be called only if any previous conversation exists
		if(iSize > 0){
			//calling the lazy adapter with the array parameters created
			adapter = new CustomArrayAdapter(this, android.R.layout.simple_list_item_1, names, uids, messages, times, uid);
			conversation.setAdapter(adapter);
		}
		
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(message.getText().length() > 0){
					//message sending
					boolean response = userFunction.sendMessage(uid, fid, message.getText().toString());
					if(response){
						Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
						populateConversation();
					}else{
						Toast.makeText(getApplicationContext(), "Message sending failed", Toast.LENGTH_SHORT).show();
					}
				}
				onCreate(savedInstanceState);
				send.clearFocus();
			}
		});
		
		clear.setOnClickListener(new View.OnClickListener() {
			//clearing the textbox
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				message.setText("");
			}
		});
	}
	
private void populateConversation(){
		//collecting and updating arrays of the conversation of the given uid and fid
		try {
			// Getting the JSON Results
			JSONObject json = userFunction.getConversation(uid, fid); 
		    // Getting Array of Contacts
			if(json != null){
				
		    JSONArray conversation = json.getJSONArray(KEY_CONVERSATION);
		    names = new String[conversation.length()];
		    uids = new String[conversation.length()];
		    messages = new String[conversation.length()];
		    times = new String[conversation.length()];
		    iSize = conversation.length();
		    // looping through All Requests
		    for(int i = 0; i < conversation.length(); i++){
		        JSONObject c = conversation.getJSONObject(i);
		        // Storing each json item in variable
		        names[i] = c.getString(TAG_NAME);
		        uids[i] = c.getString(TAG_UID);
		        messages[i] = c.getString(TAG_MESSAGE);
		        times[i] = c.getString(TAG_TIME);
		    	}
			}
		    
		} catch (JSONException e) {
		    e.printStackTrace();
		} catch (NullPointerException e){
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    	}
		
	}

}
