package com.scribbon;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class SelectFriendActivity extends Activity {
    
	// declarations
	UserFunctions userFunction = new UserFunctions();
	EditText friendsSearch;
	ImageView addFriend;
    GridView myFriendsGrid;
    FriendsLazyAdapter myFriendsAdapter, myFriendsNewAdapter;
    
    //setting the keys and tags
    private static String KEY_ID = "id";
    private static String KEY_FRIENDS= "friends";
    private static String TAG_NAME = "name";
    private static String TAG_PROFILE_PIC = "profilepic";
    private static String TAG_FID = "fid";
    
    public String[] fNames;
    public String[] fPics;
    public String[] fFids;
    public Activity save;
    public String searchString = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	// Getting the id of the user
		DatabaseHandler db = new DatabaseHandler(getBaseContext());
		HashMap<String, String> details = db.getUserDetails();
		final String uid = details.get(KEY_ID).toString();
    	
    	try {
    		// Getting the JSON Results
    		JSONObject json = userFunction.getFriends(uid);
    	    // Getting Array of Friends
    		if(json != null){
    			
    	    JSONArray friends = json.getJSONArray(KEY_FRIENDS);
    	    fNames = new String[friends.length()];
    	    fPics = new String[friends.length()];
    	    fFids = new String[friends.length()];
    	    // looping through All Contacts
    	    for(int i = 0; i < friends.length(); i++){
    	        JSONObject c = friends.getJSONObject(i);
    	        // Storing each json item in variable
    	        String name = c.getString(TAG_NAME);
    	        String pics = c.getString(TAG_PROFILE_PIC);
    	        String fid = c.getString(TAG_FID);
    	        
    	        fPics[i] = pics;
    	        fNames[i] = name;
    	        fFids[i] = fid;
    	    }
    	    
    	    myFriendsAdapter = new FriendsLazyAdapter(this, fPics, fNames);
    	    save = this;
    		}
    	    
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	} catch (NullPointerException e){
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    	}
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        
        //attaching the elements in the xml
        myFriendsGrid = (GridView)findViewById(R.id.gFriends);
        friendsSearch = (EditText)findViewById(R.id.etFriendSearch);
        addFriend = (ImageView)findViewById(R.id.ivPlus);
        myFriendsGrid.setAdapter(myFriendsAdapter);
        
        myFriendsGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                 Intent returnIntent = new Intent();
                 returnIntent.putExtra("uid", uid);
                 returnIntent.putExtra("fid", fFids[position]);
                 setResult(RESULT_OK, returnIntent);     
                 finish();
            }
        });
        
        addFriend.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addUser = new Intent(getApplicationContext(), AddFriendsActivity.class);
                //describing the elements to be sent along with the new activity call
				addUser.putExtra("uid", uid);
				startActivity(addUser);
			}
		});
        
        friendsSearch.addTextChangedListener(new TextWatcher(){

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
				int k = 0, fCount = 0;
				//initiates the function when the user inputs in every 3 character interval
				if(friendsSearch.length()%3 == 0){
					searchString = friendsSearch.getText().toString();
					//counts the matches of searching friend
					for(int j=0;j<fNames.length;j++){
						if(fNames[j].toLowerCase().contains(searchString.toLowerCase()))
							fCount++;
					}
					//creates new variables with the count
					String newFNames[] = new String[fCount];
					String newFPics[] = new String[fCount];
					
					for(int j=0;j<fNames.length;j++){
						if(fNames[j].toLowerCase().contains(searchString.toLowerCase())){
							newFNames[k] = String.valueOf(fNames[j]);
							newFPics[k] = String.valueOf(fPics[j]);
							k++;
						}
					}
					//creates a new adapter with the matching friends
					myFriendsGrid.setAdapter(null);
					myFriendsNewAdapter = new FriendsLazyAdapter(save, newFPics, newFNames);
					myFriendsGrid.setAdapter(myFriendsNewAdapter);
				}
			}
        });
    }

	@Override
    
public void onDestroy()
    {
    	myFriendsGrid.setAdapter(null);
        super.onDestroy();
    }
    
    public OnClickListener listener = new OnClickListener(){
        public void onClick(View arg0) {
        	myFriendsAdapter.imageLoader.clearCache();
        	myFriendsAdapter.notifyDataSetChanged();
        }
    };
}
