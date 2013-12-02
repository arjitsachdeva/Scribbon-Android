package com.scribbon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowProfileActivity extends Activity{

    //setting the keys and tags
	private static String KEY_DETAILS= "details";
	UserFunctions userFunction = new UserFunctions();
	String uid, fid, name, email, pp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_layout);
		
        //receiving the string item sent from the previous activity bundle
		uid = this.getIntent().getStringExtra("uid");
		fid = this.getIntent().getStringExtra("fid");
		
		JSONObject json = userFunction.getDetails(fid);
	    // Getting Array of Details
		if(json != null){
		    try {
				JSONArray details = json.getJSONArray(KEY_DETAILS);
				JSONObject c = details.getJSONObject(0);
				name = c.getString("name");
				email = c.getString("email");
				pp = c.getString("pp");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e){
	    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
	    	}
		}
		
        //attaching the elements in the xml
		ImageView profileImage = (ImageView)findViewById(R.id.ivProfilePic);
		TextView profileName = (TextView)findViewById(R.id.tvProfileName);
		TextView profileEmail = (TextView)findViewById(R.id.tvProfileEmail);
		ImageView profileAdd = (ImageView)findViewById(R.id.ivProfileAdd);
		ImageView profileAlbum = (ImageView)findViewById(R.id.ivAlbum);
		Button profileFriends = (Button)findViewById(R.id.bFriends);
		Button profileShares = (Button)findViewById(R.id.bShares);
		
		// Setting parameters for the layout
		profileImage.setMaxHeight(80);
		profileImage.setMaxWidth(50);
		profileImage.setImageBitmap(userFunction.decodeImage(pp));
		profileName.setText(name);
		profileEmail.setText(email);
		profileFriends.setText(userFunction.countFriends(fid)+"\nFriends");
		profileShares.setText(userFunction.countImages(fid)+"\nShares");
		
		// Toggling different images for Friend and NonFriend cases
		if(userFunction.isFriend(uid, fid)){
			profileAdd.setImageResource(R.drawable.del_friend);
		}else{
			profileAdd.setImageResource(R.drawable.add_friend);
		}
		
		profileAlbum.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent photosIntent = new Intent(getApplicationContext(), PhotosActivity.class);
	            photosIntent.putExtra("uid", uid);
	            startActivity(photosIntent);
			}
		});
		
		profileAdd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!userFunction.isFriend(uid, fid)){
					try {
			    		// Getting the JSON Results
			    		JSONObject json = userFunction.addFriend(uid, fid); 
			    	    // Getting Friend Request Status
			    		if(json != null){
				    	    int result = json.getInt("status");
				    	    
				    	    // Self explanatory cases
				    	    
				    	    if(result == -1){
				    	    	Toast.makeText(getApplicationContext(), "Friend Request Sent!", Toast.LENGTH_SHORT).show();
				    	    
				    	    } else if(result == 0){
				    	    	Toast.makeText(getApplicationContext(), "Request Already Sent!", Toast.LENGTH_SHORT).show();
				    	    	
				    	    } else if(result == 1){
				    	    	Toast.makeText(getApplicationContext(), "Is your Friend Already", Toast.LENGTH_SHORT).show();
				    	    	
				    		} else {
				    			Toast.makeText(getApplicationContext(), "Some Error Occured, Retry", Toast.LENGTH_SHORT).show();
				    		}
			    		}
					} catch (JSONException e) {
			    	    e.printStackTrace();
					} catch (NullPointerException e){
			    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
			    	}
				}else{
					JSONObject json = userFunction.rejectFriend(uid, fid);
					// Getting Array of Result
					if(json != null){
			    	    String result;
			    	    try {
							result = json.getString("success");
							
				    	    // Self explanatory cases
							
				    	    if(result.equals("0")){
				    	    	Toast.makeText(getApplicationContext(), "IO Error - Try Again", Toast.LENGTH_SHORT).show();
				    	    } else if(result.equals("1")){
				    	    	Toast.makeText(getApplicationContext(), "Unfriended", Toast.LENGTH_SHORT).show();	
				    	    	finish();
				    	    	startActivity(getIntent());
				    		}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NullPointerException e){
				    		//Will be caught when no Internet connectivity is present, can edit the message stored in the values/string/no_internet
				    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
				    	}
			    	}
				}
			}
		});

	}
}
