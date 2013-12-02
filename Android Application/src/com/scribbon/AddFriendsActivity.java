package com.scribbon;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


//Used for adding friends

public class AddFriendsActivity extends Activity {
    
	// declarations
	UserFunctions userFunction = new UserFunctions();
	EditText usersSearch;
	ImageView addFriend;
    GridView usersGrid;
    UsersLazyAdapter usersAdapter;
    
    //setting the keys and tags
    private static String KEY_USERS= "users";
    private static String TAG_NAME = "name";
    private static String TAG_PROFILE_PIC = "profilepic";
    private static String TAG_EMAIL = "email";
    private static String TAG_UID = "uid";
    
    //declaring the variables used
    public String[] fNames;
    public String[] fPics;
    public String[] fEmails;
    public String[] fIds;
    public Activity save;
    public String searchString = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	save = this; //copying the activity for later use
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_layout);
        
        //attaching the elements in the xml
        usersGrid = (GridView)findViewById(R.id.gUsers);
        usersSearch = (EditText)findViewById(R.id.etUserSearch);
        
        //receiving the string item sent from the previous activity bundle
        final String uid = this.getIntent().getStringExtra("uid");
        
        //actions to be performed on button click
        usersGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                 Intent profile = new Intent(getApplicationContext(), ShowProfileActivity.class);
                 //describing the elements to be sent along with the new activity call
                 profile.putExtra("uid", uid);
                 profile.putExtra("fid", fIds[position]);
                 profile.putExtra("name", fNames[position]);
                 profile.putExtra("email", fEmails[position]);
                 profile.putExtra("profile_pic", fPics[position]);
                 startActivity(profile);                               
            }
        });
        
        usersSearch.addTextChangedListener(new TextWatcher(){

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
				searchString = usersSearch.getText().toString();
				//performs only when the entered text receives a space
				if(searchString.length()>2 && searchString.substring(searchString.length()-1, searchString.length()).equals(" ")){
					searchString = searchString.trim();
					try {
			    		// Getting the JSON Results for the string
			    		JSONObject json = userFunction.getUsers(searchString); 
			    	    // Getting Array of Users
			    		if(json != null){
			    	    JSONArray users = json.getJSONArray(KEY_USERS);
			    	    fNames = new String[users.length()];
			    	    fPics = new String[users.length()];
			    	    fEmails = new String[users.length()];
			    	    fIds = new String[users.length()];
			    	    // looping through All Contacts
			    	    for(int i = 0; i < users.length(); i++){
			    	        JSONObject c = users.getJSONObject(i);
			    	        // Storing each json item in variable
			    	        String name = c.getString(TAG_NAME);
			    	        String pics = c.getString(TAG_PROFILE_PIC);
			    	        String email = c.getString(TAG_EMAIL);
			    	        String uid = c.getString(TAG_UID);
			    	        //collecting the results into an array
			    	        fPics[i] = pics;
			    	        fNames[i] = name;
			    	        fEmails[i] = email;
			    	        fIds[i] = uid;
			    	    }
			    	    
			    	    //calling the lazy adapter with the array parameters created
			    	    usersAdapter = new UsersLazyAdapter(save, fPics, fNames, fEmails);
			    	    usersGrid.setAdapter(null);
			    	    usersGrid.setAdapter(usersAdapter);
			    	    
			    		}
			    	    
			    	} catch (JSONException e) {
			    	    e.printStackTrace();
			    	    
			    	} catch (NullPointerException e){
			    		//Will be caught when no Internet connectivity is present, can edit the message stored in the values/string/no_internet
			    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
			    	}
				}
				
			}
        });
    }

	@Override
    
public void onDestroy()
    {
    	usersGrid.setAdapter(null);
        super.onDestroy();
    }
    
    public OnClickListener listener = new OnClickListener(){
        public void onClick(View arg0) {
        	usersAdapter.imageLoader.clearCache();
        	usersAdapter.notifyDataSetChanged();
        }
    };
    
    
    
}
