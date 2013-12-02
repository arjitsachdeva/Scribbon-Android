package com.scribbon;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
 
public class DashboardActivity extends TabActivity implements OnItemSelectedListener{
    UserFunctions userFunctions;
    Spinner sAction;
    TextView welcome;
    
    //setting the keys and tags
    private static String KEY_ID = "id";
    String uid;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        /**
         * Dash board Screen for the application
         * */
        // Check login status in database
        userFunctions = new UserFunctions();
        if(userFunctions.isUserLoggedIn(getApplicationContext())){
        	// user already logged in show dash board
            setContentView(R.layout.dashboard);
            
            sAction = (Spinner) findViewById(R.id.sAction);
            welcome = (TextView) findViewById(R.id.tvName);
            //ivProfilePic = (ImageView)findViewById(R.id.ivProfilePic);
            sAction.setOnItemSelectedListener(this);
            TabHost tabHost = getTabHost();
            
            //describing the option for the drop-down spinner
            ArrayList<String> categories = new ArrayList<String>();
            categories.add("");
            categories.add("My Profile");
            categories.add("Requests");
            categories.add("Settings");
            categories.add("Log Out");
            
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            dataAdapter.setDropDownViewResource(android.R.layout.test_list_item);
     
            // attaching data adapter to spinner
            sAction.setAdapter(dataAdapter);
            
            // Getting user details from internal database
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            @SuppressWarnings("rawtypes")
			HashMap details = db.getUserDetails();
            String name = details.get("name").toString();
            //String profilePic = details.get("profile_pic").toString();
            //setting the dashboard welcome message
            welcome.setText("Welcome, "+ name);
            uid = details.get(KEY_ID).toString();
            
            // Tab for Photos
            TabSpec photospec = tabHost.newTabSpec("Photos");
            // setting Title and Icon for the Tab
            photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.icon_photos_tab));
            Intent photosIntent = new Intent(this, PhotosActivity.class);
            photosIntent.putExtra("uid", uid);
            photospec.setContent(photosIntent);
            
            // Tab for Friends
            TabSpec friendspec = tabHost.newTabSpec("Friends");
            // setting Title and Icon for the Tab
            friendspec.setIndicator("Friends", getResources().getDrawable(R.drawable.icon_friends_tab));
            Intent friendsIntent = new Intent(this, FriendsActivity.class);
            friendspec.setContent(friendsIntent);
            
            // Tab for Messages
            TabSpec messagespec = tabHost.newTabSpec("Messages");
            // setting Title and Icon for the Tab
            messagespec.setIndicator("Messages", getResources().getDrawable(R.drawable.icon_messages_tab));
            Intent messageIntent = new Intent(this, MessagesActivity.class);
            messagespec.setContent(messageIntent);
            
//            Uri uri = Uri.parse(profilePic);
//            ivProfilePic.setImageURI(uri);
            
            // Adding all TabSpec to TabHost
            tabHost.addTab(photospec); // Adding photos tab
            tabHost.addTab(friendspec); // Adding friends tab
            tabHost.addTab(messagespec); // Adding messages tab
            
 
        }else{
            // user is not logged in show login screen
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dash board screen
            finish();
        }
    }

    // describing the action to be performed on clicking the option from the drop-down spinner based on position clicked
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		// On selecting a spinner item
		//String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        if(position == 2){
        	// takes you to the requests managing section
            Intent request = new Intent(getApplicationContext(), RequestActivity.class);
            request.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(request);
            //finish();
        }else if(position == 4){
        	// takes you to the logout section
        	userFunctions.logoutUser(getApplicationContext());
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(login);
            // Closing dash board screen
            finish();
        }
        //sets the drop-down to the default selection
        sAction.setSelection(0);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}