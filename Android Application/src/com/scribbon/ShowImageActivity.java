package com.scribbon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ShowImageActivity extends Activity{

	// declarations
	UserFunctions userFunction = new UserFunctions();
	String pic, title, desc, imageId;
	
    //setting the keys and tags
	private static String KEY_COMMENTS= "comments";
	private static String TAG_COMMENT= "comment";
	private static String TAG_UID= "uid";
	private static String TAG_NAME= "name";
	
	String uid;
	
	String[] comments, uids, names;
	Button like, repin, comment, commentbutton;
	EditText commentbox;
	ImageView showImage;
	TextView iTitle, iDescription;
	ListView iComment;
	SimpleAdapter adapter;
	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_layout);
		
        //attaching the elements in the xml
		RelativeLayout frame = (RelativeLayout)findViewById(R.id.rlFrame); 
		showImage = (ImageView)findViewById(R.id.ivShowImage);
		like = (Button)findViewById(R.id.bLike);
		repin = (Button)findViewById(R.id.bRepin);
		comment = (Button)findViewById(R.id.bComments);
		iTitle = (TextView)findViewById(R.id.tvTitle);
		iDescription = (TextView)findViewById(R.id.tvDescription);
		iComment = (ListView)findViewById(R.id.lvComments);
		
		pic = this.getIntent().getStringExtra("profile_pic");
		title = this.getIntent().getStringExtra("title");
		desc = this.getIntent().getStringExtra("desc");
		imageId = this.getIntent().getStringExtra("image_id");
		
    	// Getting the user details from the internal database
		DatabaseHandler db = new DatabaseHandler(getBaseContext());
		HashMap<String, String> details = db.getUserDetails();
		uid = details.get("id").toString();
		
		// Populating the conversation of a particular uid and fid
		populateComments();
		
		adapter = new SimpleAdapter(this, data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"comment", "name"},
		                                          new int[] {android.R.id.text1,
		                                                     android.R.id.text2});
		iComment.setAdapter(adapter);
		
        //attaching the elements in the xml
		commentbutton = (Button)findViewById(R.id.bComment);
        commentbox = (EditText)findViewById(R.id.etComments);
		
        // Initial visibility modes during loading
		iTitle.setVisibility(View.INVISIBLE);
		iDescription.setVisibility(View.INVISIBLE);
		iComment.setVisibility(View.INVISIBLE);
		commentbutton.setVisibility(View.INVISIBLE);
		commentbox.setVisibility(View.INVISIBLE);
		
		
		showImage.setImageBitmap(userFunction.decodeImage(pic)); // calling function which downloads and show the image via url
		iTitle.setText(title);
		iDescription.setText(desc);
		
		frame.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				if(action == MotionEvent.ACTION_DOWN){
					// actions when touching the image
					iTitle.setVisibility(View.VISIBLE);
					iDescription.setVisibility(View.VISIBLE);
				}
				else if(action == MotionEvent.ACTION_UP){
					// actions when releasing the touch from image
					// creating a handler to create a delay
					Handler myHandler = new Handler();
					myHandler.postDelayed(mMyRunnable, 2000);
				}
				return true;
			}
		});
		
		// Comment onClick actions
		comment.setOnClickListener(new View.OnClickListener() {
			
			// Toggling between Comment and Image button
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(iComment.getVisibility()==View.INVISIBLE){
					comment.setText("IMAGE");
					iComment.setVisibility(View.VISIBLE);
					showImage.setVisibility(View.INVISIBLE);
					commentbox.setVisibility(View.VISIBLE);
					commentbutton.setVisibility(View.VISIBLE);
				}else{
					comment.setText("COMMENT");
					iComment.setVisibility(View.INVISIBLE);
					showImage.setVisibility(View.VISIBLE);
					commentbox.setVisibility(View.INVISIBLE);
					commentbutton.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		// OnClick actions for displaying the profile of the user commented
		iComment.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO Auto-generated method stub
				Intent profile = new Intent(getApplicationContext(), ShowProfileActivity.class);
                profile.putExtra("uid", uid);
                profile.putExtra("fid", uids[position]);
                startActivity(profile);
			}
		});
		
		// OnClick actions for posting the comment
		commentbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(commentbox.getText().length() > 0){
					boolean response = userFunction.insertComments(uid, imageId, commentbox.getText().toString());
					if(response){
						Toast.makeText(getApplicationContext(), "Comment posted", Toast.LENGTH_SHORT).show();
						populateComments();
					}else{
						Toast.makeText(getApplicationContext(), "Comment posting failed", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	
	@SuppressWarnings("unused")
	// Uploading image part not completed as said by you.
	
	private void shareIt() {
		//sharing implementation here
		//sharingIntent.setType("image/jpeg");
		//String shareBody = "Here is the share content body";
		//String shareSubject = "Here is the share content subject";
		//sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSubject);
		//sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		//sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, entity);
		//startActivity(Intent.createChooser(sharingIntent, "Share via"));
		Uri uriToImage = (Uri) this.getIntent().getExtras().get("android.intent.extra.STREAM");
		Toast.makeText(getApplicationContext(), uriToImage.toString(), Toast.LENGTH_SHORT).show();
//		Intent shareIntent = new Intent();
//		shareIntent.setAction(Intent.ACTION_SEND);
//		shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
//		shareIntent.setType("image/jpeg");
//		//startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
//		startActivity(Intent.createChooser(sharingIntent, "Share image using")); 
		}
	
	// the Handler for removing the title and description of the image when the touch is released
	private Runnable mMyRunnable = new Runnable()
	{
	    public void run()
	    {
	    	iTitle.setVisibility(View.INVISIBLE);
			iDescription.setVisibility(View.INVISIBLE);
	    }
	 };
	
	 // Populating the previous comments ofthe image
	private void populateComments(){
		
		try {
    		// Getting the JSON Results
    		JSONObject json = userFunction.getComments(imageId); 
    	    // Getting Array of Comments
    		if(json != null){
    			
    	    JSONArray jComments = json.getJSONArray(KEY_COMMENTS);
    	    comments = new String[jComments.length()];
    	    uids = new String[jComments.length()];
    	    names = new String[jComments.length()];
    	    data.clear();

    	    // looping through All Comments
    	    for(int i = 0; i < jComments.length(); i++){
    	    	Map<String, String> datum = new HashMap<String, String>(2);
    	        JSONObject c = jComments.getJSONObject(i);
    	        // Storing each json item in variable
    	        comments[i] = c.getString(TAG_COMMENT);
    	        uids[i] = c.getString(TAG_UID);
    	        names[i] = c.getString(TAG_NAME);
    	        datum.put("comment", comments[i]);
    		    datum.put("name", names[i]);
    		    data.add(datum);
    	    	}
    		}
    	    
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	} catch (NullPointerException e){
    		//Will be caught when no Internet connectivity is present, can edit the message stored in the values/string/no_internet
    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
    	}
	}
}
