package com.scribbon;
 
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class UserFunctions {
 
    private JSONParser jsonParser;
 
    // Testing in local host using wamp or xampp
    
//	private static String siteURL = "http://10.0.2.2/android_login_api/";
	private static String siteURL = "http://agrimainfotech.com/test/android_login_api/";
	
	
	// Tags and Keys used
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String get_friends_tag = "get_friends";
    private static String get_users_tag = "get_users";
    private static String add_friend_tag = "add_friend";
    private static String get_requests_tag = "get_requests";
    private static String accept_friend_tag = "accept_friend";
    private static String reject_friend_tag = "reject_friend";
    private static String count_friends_tag = "count_friends";
    private static String count_images_tag = "image_count";
    private static String is_friend_tag = "is_friend";
    private static String get_user_details = "get_user_details";
    private static String image_comments = "get_comments";
    private static String insert_comments = "insert_comments";
    private static String get_images_tag = "get_images";
    private static String inserted_tag = "inserted";
    private static String KEY_SEND_MESSAGE = "send_message";
    private static String KEY_CONVERSATION_PEOPLE = "conversationists";
	private static String KEY_CONVERSATION = "conversation";
 
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }
 
    /**
     * function make Login Request
     * @param email
     * @param password
     * */
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        // Log.e("JSON", json.toString());
        return json;
    }
 
    /**
     * function make Register Request
     * @param name
     * @param email
     * @param password
     * */
    public JSONObject registerUser(String name, String email, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
    }
    
    /**
     * function make Search User Request
     * @param name
     * */
    public JSONObject getFriends(String uid){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_friends_tag));
        params.add(new BasicNameValuePair("uid", uid));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
    }
 
    /**
     * Function get Login status
     * */
    public boolean isUserLoggedIn(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if(count > 0){
            // user logged in
            return true;
        }
        return false;
    }
 
    /**
     * Function to logout user
     * Reset Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
    
    // Gets the users in ImageSnap with the search string
	public JSONObject getUsers(String searchString) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_users_tag));
        params.add(new BasicNameValuePair("string", searchString));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Gets the friend status
	  public boolean isFriend(String uid, String fid){
		// TODO Auto-generated method stub
			// Building Parameters
		  String friend = "n";
		  List<NameValuePair> params = new ArrayList<NameValuePair>();
		  params.add(new BasicNameValuePair("tag", is_friend_tag));
	      params.add(new BasicNameValuePair("uid", uid));
	      params.add(new BasicNameValuePair("fid", fid));
	 
	        // getting JSON Object
	        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
	        if(json != null){
	        	try {
					friend = json.getString(is_friend_tag);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        if(friend.equals("y")){
	        	return true;
	        }else{
	        	return false;
	        }
	    }
	  
	    // Sends friend request
	public JSONObject addFriend(String uid, String fid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", add_friend_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("fid", fid));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Gets the count of Friends
	public int countFriends(String uid) {
		// TODO Auto-generated method stub
		// Building Parameters
		int count = 0;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", count_friends_tag));
        params.add(new BasicNameValuePair("uid", uid));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        if(json != null){
        	try {
				count = Integer.parseInt(json.getString(count_friends_tag));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return count;
	}
	
    // Gets the count of Images of a user
	public int countImages(String uid) {
		// TODO Auto-generated method stub
		// Building Parameters
		int count = 0;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", count_images_tag));
        params.add(new BasicNameValuePair("uid", uid));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        if(json != null){
        	try {
				count = Integer.parseInt(json.getString(count_images_tag));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return count;
	}
	
    // Downloads and decodes an image url to Bitmap
	public Bitmap decodeImage(String url) {
    	URL aURL;
    	try {
	    	aURL = new URL(url);
	    	URLConnection conn = aURL.openConnection();
	    	conn.connect();
	    	InputStream is = conn.getInputStream();
	    	BufferedInputStream bis = new BufferedInputStream(is);
	    	Bitmap bm = BitmapFactory.decodeStream(bis);
	    	bis.close();
	    	is.close();
	    	return bm;
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	return null;
}

    // Gets the requests for a user
	public JSONObject getRequests(String uid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_requests_tag));
        params.add(new BasicNameValuePair("uid", uid));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}

    // Accepts a friend request
	public JSONObject acceptFriend(String uid, String fid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", accept_friend_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("fid", fid));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}

    // Rejects a friend request 
	public JSONObject rejectFriend(String uid, String fid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", reject_friend_tag));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("fid", fid));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Gets the details of the user
	public JSONObject getDetails(String uid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_user_details));
        params.add(new BasicNameValuePair("fid", uid));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}

    // Gets the comments of the image
	public JSONObject getComments(String imageId) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", image_comments));
        params.add(new BasicNameValuePair("image_id", imageId));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Inserts the comments for the image
	public boolean insertComments(String uid, String imageId, String comment) {
		// TODO Auto-generated method stub
		// Building Parameters
			// Building Parameters
		  String inserted = "n";
		  List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", insert_comments));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("image_id", imageId));
        params.add(new BasicNameValuePair("comment", comment));
	 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        if(json != null){
        	try {
        		inserted = json.getString(inserted_tag);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if(inserted.equals("y")){
        	return true;
        }else{
        	return false;
        }
	}
 
    // Gets the images of the user
	public JSONObject getImages(String uid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_images_tag));
        params.add(new BasicNameValuePair("uid", uid));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Sends a message from a user to friend
	public boolean sendMessage(String senderID, String receiverID, String message) {
		// TODO Auto-generated method stub
		// Building Parameters
			// Building Parameters
		  String inserted = "n";
		  List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", KEY_SEND_MESSAGE));
        params.add(new BasicNameValuePair("sender_id", senderID));
        params.add(new BasicNameValuePair("message", message));
        params.add(new BasicNameValuePair("receiver_id", receiverID));
	 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        if(json != null){
        	try {
        		inserted = json.getString(inserted_tag);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        if(inserted.equals("y")){
        	return true;
        }else{
        	return false;
        }
	}
	
    // Gets the people previously chated with the user
	public JSONObject getConversationists(String uid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", KEY_CONVERSATION_PEOPLE));
        params.add(new BasicNameValuePair("uid", uid));
 
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Gets the chats of the conversation between users
	public JSONObject getConversation(String uid, String fid) {
		// TODO Auto-generated method stub
		// Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", KEY_CONVERSATION));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("fid", fid));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(siteURL, params);
        // return json
        return json;
	}
	
    // Gets the network status of the device
	public boolean isNetworkAvailable(Context context) {
		   ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		   if (connectivity == null) {

		   } else {
		      NetworkInfo[] info = connectivity.getAllNetworkInfo();
		      if (info != null) {
		         for (int i = 0; i < info.length; i++) {
		            if (info[i].getState() == NetworkInfo.State.CONNECTED) {
		               return true;
		            }
		         }
		      }
		   }
		   return false;
		}
	
}