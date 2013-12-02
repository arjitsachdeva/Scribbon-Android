package com.scribbon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PhotosActivity extends Activity {
    
//		String siteURL = "http://10.0.2.2/android_login_api/";
    //declaring the variables used
	ListView myPhotoList1, myPhotoList2, myPhotoList3;
	PhotosLazyAdapter myPhotoAdapter1, myPhotoAdapter2, myPhotoAdapter3;
	int iSize = 0;
    //String siteURL = "http://agrimainfotech.com/test/android_login_api/";
    
    String[] iID, iPath, iDescription, iTitle;
    String uid;
    
    //setting the keys and tags
    private static String KEY_IMAGES = "images";
    private static String TAG_IMAGE_ID = "image_id";
    private static String TAG_IMAGE_PATH = "image_path";
    private static String TAG_TITLE = "title";
    private static String TAG_DESCRIPTION = "description";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_layout_floating);
        //attaching the elements in the xml
        myPhotoList1 = (ListView)findViewById(R.id.lvList1);
        myPhotoList2 = (ListView)findViewById(R.id.lvList2);
        myPhotoList3 = (ListView)findViewById(R.id.lvList3);
        
        //receiving the string item sent from the previous activity bundle
        uid = this.getIntent().getStringExtra("uid");
        UserFunctions userFunction = new UserFunctions();
        JSONObject json = userFunction.getImages(uid);
	    // Getting Array of Images
		if(json != null){
		    
			JSONArray images;
			try {
				images = json.getJSONArray(KEY_IMAGES);
				iSize = images.length();
				iID = new String[images.length()];
				iPath = new String[images.length()];
				iTitle = new String[images.length()];
				iDescription = new String[images.length()];
	    	    // looping through All Requests
	    	    for(int i = 0; i < images.length(); i++){
	    	        JSONObject c = images.getJSONObject(i);
	    	        // Storing each json item in variable
	    	        iID[i] = c.getString(TAG_IMAGE_ID);
					iPath[i] = c.getString(TAG_IMAGE_PATH);
					iTitle[i] = c.getString(TAG_TITLE);
					iDescription[i] = c.getString(TAG_DESCRIPTION);
	    	    }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e){
	    		//Will be caught when no Internet connectivity is present, can edit the message stored in the values/string/no_internet
	    		Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
	    	}
		}        
        
		// checks whether the array is not empty
		// the image list is separated into 3 different categories for setting each item with different heights, Grid cannot be used
		if(iSize>0){
	        myPhotoAdapter1 = new PhotosLazyAdapter(this, iPath);
	        myPhotoList1.setAdapter(myPhotoAdapter1);
	        
	        myPhotoAdapter2 = new PhotosLazyAdapter(this, iPath);
	        myPhotoList2.setAdapter(myPhotoAdapter2);
	        
	        myPhotoAdapter3 = new PhotosLazyAdapter(this, iPath);
	        myPhotoList3.setAdapter(myPhotoAdapter3);
	        
	        // actions to be performed when an image is clicked
	        myPhotoList1.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	Intent showPic = new Intent(getApplicationContext(), ShowImageActivity.class);
	                 //describing the elements to be sent along with the new activity call
	            	showPic.putExtra("profile_pic", iPath[position]);
	            	showPic.putExtra("title", iTitle[position]);
	            	showPic.putExtra("desc", iDescription[position]);
	            	showPic.putExtra("image_id", iID[position]);
	                startActivity(showPic);
	            }
	        });
	        
	        // actions to be performed when an image is clicked
	        myPhotoList2.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	Intent showPic = new Intent(getApplicationContext(), ShowImageActivity.class);
	                 //describing the elements to be sent along with the new activity call
	            	showPic.putExtra("profile_pic", iPath[position]);
	            	showPic.putExtra("title", iTitle[position]);
	            	showPic.putExtra("desc", iDescription[position]);
	            	showPic.putExtra("image_id", iID[position]);
	                startActivity(showPic);
	            }
	        });
	        
	        // actions to be performed when an image is clicked
	        myPhotoList3.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            	Intent showPic = new Intent(getApplicationContext(), ShowImageActivity.class);
	                 //describing the elements to be sent along with the new activity call
	            	showPic.putExtra("profile_pic", iPath[position]);
	            	showPic.putExtra("title", iTitle[position]);
	            	showPic.putExtra("desc", iDescription[position]);
	            	showPic.putExtra("image_id", iID[position]);
	                startActivity(showPic);
	            }
	        });
		}
    }
    
    @Override
    public void onDestroy()
    {
    	myPhotoList1.setAdapter(null);
    	myPhotoList2.setAdapter(null);
    	myPhotoList3.setAdapter(null);
        super.onDestroy();
    }
    
    public OnClickListener listener=new OnClickListener(){
        public void onClick(View arg0) {
        	myPhotoAdapter1.imageLoader.clearCache();
        	myPhotoAdapter1.notifyDataSetChanged();
        	myPhotoAdapter2.imageLoader.clearCache();
        	myPhotoAdapter2.notifyDataSetChanged();
        	myPhotoAdapter3.imageLoader.clearCache();
        	myPhotoAdapter3.notifyDataSetChanged();
        }
    };
}