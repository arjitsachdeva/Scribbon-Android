<?php

class DB_Functions {

    private $db;
	public $siteURL; 

    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
		$this->siteURL = "http://10.0.2.2/android_login_api/";
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO users(unique_id, name, email, encrypted_password, salt, created_at) VALUES('$uuid', '$name', '$email', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result) {
            // get user details 
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM users WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
        $result = mysql_query("SELECT * FROM users WHERE email = '$email'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $result = mysql_query("SELECT email from users WHERE email = '$email'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }

    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }
	
	// Function to get details of all the friends
	public function getFriends($uid)
	{

		if (isset($uid))
		{

			$sql = mysql_query("SELECT * FROM users WHERE uid IN (

			SELECT fid FROM friends WHERE uid='$uid' AND status=1

							  UNION

			SELECT uid FROM friends WHERE fid='$uid' AND status=1 )")or die(mysql_error());
			// check for result 
			$no_of_rows = mysql_num_rows($sql);
			if ($no_of_rows > 0)
			{
				while($row = mysql_fetch_array($sql))
				{
					//friend's email
					$email = $row['email'];
					$uniqueID = $row['unique_id'];
					//path to directory to scan. i have included a wildcard for a subdirectory
					//$directory = "users/".$email."/profilepics";
					//get all image files with a .jpg extension.
					//$images = glob("" . $directory . "*.jpg");

					//$comma_separated = implode(",", $images);
					$profilepic = $this->siteURL."users/".$uniqueID."/profilepics/1.jpg";
					//$profilepic = "";
					
					   $friends[] = array(
						  'fid' => $row['uid'],
						  'name' => $row['name'],
						  'email' => $row['email'],
						  'profilepic' => $profilepic
					   );
				}
				return $friends;
			}
		}
		else
		{
			return false;
		}
    }
	
	// Function to get details of a user
	public function getDetails($fid)
	{
		if (isset($fid))
		{
			$sql = mysql_query("SELECT * FROM users WHERE uid='$fid'")or die(mysql_error());
			// check for result 
			$no_of_rows = mysql_num_rows($sql);
			if ($no_of_rows > 0)
			{
				$result = mysql_fetch_array($sql);
				$profilepic = $this->siteURL."users/".$result['unique_id']."/profilepics/1.jpg";
				$details[] = array(
				'name' => $result['name'],
				'email' => $result['email'],
				'pp' => $profilepic
				);
			}
			return $details;
		}
	}
	
	// Function to add a friend
	public function addFriend($uid, $fid)
	{
	
		$status = -1;
		$sql = mysql_query("SELECT * FROM friends WHERE (uid='$uid' and fid='$fid') OR (uid='$fid' and fid='$uid')")or die(mysql_error());
		// check for result 
		$no_of_rows = mysql_num_rows($sql);
		
			while($row = mysql_fetch_array($sql))
			{
				$status = $row['status'];
			}
			
			switch($status)
			{
				case -1:
					mysql_query("INSERT INTO friends (uid, fid, status) VALUES ('$uid', '$fid','0')");	 
					$result = -1;
					break;
				
				case 0:
					$result = 0;
					break;
					
				case 1:
					$result = 1;
					break;
					
				default :
					$result = 2;
			}
			return $result;
		
	}
	
	// Function to search from the public users
	public function loadUsers($string)
	{
		$sql = mysql_query("SELECT * FROM users WHERE name LIKE '%$string%' or email = '$string'")or die(mysql_error());
		// check for result 
		$no_of_rows = mysql_num_rows($sql);
		if ($no_of_rows > 0)
		{
			while($row = mysql_fetch_array($sql))
			{
				//friend's email
				$email=$row['email'];
				$uniqueID = $row['unique_id'];
				//path to directory to scan. i have included a wildcard for a subdirectory
				//$directory = "users/".$email."/profilepics";
				//get all image files with a .jpg extension.
				//$images = glob("" . $directory . "*.jpg");

				//$comma_separated = implode(",", $images);
				$profilepic = $this->siteURL."users/".$uniqueID."/profilepics/1.jpg";
				//$profilepic = "";
				
			   $users[] = array(
				  'uid' => $row['uid'],
				  'name' => $row['name'],
				  'email' => $row['email'],
				  'profilepic' => $profilepic
			   );
			}
			
			return $users;
		}
		else
		return false;
	}
	
	// Function to count the number of friends of a user
	public function countFriends($uid)
	{
		$count = 0;
		if (isset($uid))
		{
			$sql = mysql_query("SELECT uid FROM users WHERE uid IN (
			SELECT fid FROM friends WHERE uid='$uid' AND status=1
							  UNION
			SELECT uid FROM friends WHERE fid='$uid' AND status=1 )");
			$count = mysql_num_rows($sql);
		}
		return $count;
	}
	
	// Function to count the number of images of a user
	public function countImages($uid)
	{
		$count = 0;
		if (isset($uid))
		{
			$sql = mysql_query("SELECT * FROM images WHERE uid='$uid'");
			$count = mysql_num_rows($sql);
		}
		return $count;
	}
	
	// Function to accept a friend request
	public function acceptFriend($uid, $fid)
	{
		if (isset($uid) && isset($fid))
		{
			$sql = mysql_query("UPDATE friends SET status='1' WHERE uid='$uid' AND fid='$fid'");
			if($sql)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	// Function to reject a friend request
	public function rejectFriend($uid, $fid)
	{
		if (isset($uid) && isset($fid))
		{
			$sql = mysql_query("DELETE FROM friends WHERE uid='$uid' AND fid='$fid'");
			if($sql)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	// Function to check whether a user is a friend or not
	public function isFriend($uid, $fid)
	{
		if (isset($uid) && isset($fid))
		{
			$sql = mysql_query("SELECT status FROM friends WHERE (uid='$uid' AND fid='$fid') OR (uid='$fid' AND fid='$uid')");
			$no_of_rows = mysql_num_rows($sql);
			if ($no_of_rows > 0)
			{
				$result = mysql_fetch_array($sql);
				$status = $result['status'];
				if($status == '1')
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
	
	// Function to load all image details of user
	public function getImages($uid)
	{
		$sql = mysql_query("SELECT * FROM images WHERE uid='$uid'");
		while($row = mysql_fetch_array($sql))
		{
		   $images[] = array(
						  'image_id' => $row['image_id'],
						  'image_path' => $row['imagepath'],
						  'title' => $row['title'],
						  'description' => $row['description']
			);
		}
		return $images;
	}
	
	// Function to load all requests for a user
	public function getRequests($uid)
	{
		
		$sql = mysql_query("SELECT * FROM users WHERE uid IN (SELECT uid FROM friends WHERE fid='$uid' AND status='0')"); //incoming requests
		$sql2 = mysql_query("SELECT * FROM users WHERE uid IN (SELECT uid FROM friends WHERE uid='$uid' AND status='0')"); //outgoing requests
	
		if(mysql_num_rows($sql)>0)
		{
			while($row = mysql_fetch_array($sql))
			{
				$profilepic = $this->siteURL."users/".$row['unique_id']."/profilepics/1.jpg";
				$requests[] = array(
							  'name' => $row['name'],
							  'pp' => $profilepic,
							  'email' => $row['email'],
							  'uid' => $row['uid'],
							  'type' => 'i' //incoming
				);
			}
		}
		
		if(mysql_num_rows($sql2)>0)
		{
			while($row = mysql_fetch_array($sql2))
			{
				$profilepic = $this->siteURL."users/".$row['unique_id']."/profilepics/1.jpg";
				$requests[] = array(
							  'name' => $row['name'],
							  'pp' => $profilepic,
							  'email' => $row['email'],
							  'uid' => $row['uid'],
							  'type' => 'o' //outgoing
				);
			}
		}
		return $requests;
	}
	
	// Function to insert comments to an image
	public function insertComment($uid, $imageID, $comment)
	{
		$sql = mysql_query("INSERT INTO comments(image_id, comment, uid) VALUES ('$imageID','$comment','$uid')");
		if($sql)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Function to load all comments of an image
	public function getComments($imageID)
	{
		$sql = mysql_query("SELECT * FROM comments NATURAL JOIN users WHERE image_id='$imageID'");
		if(mysql_num_rows($sql)>0)
		{
			while($row = mysql_fetch_array($sql))
			{
			   $results[] = array(
					'comment' => $row['comment'], //The comment of the user
					'uid' => $row['uid'], //The UID of the user
					'name' => $row['name'] //The Name of the user
			   );
			}
			return $results;
		}
	}
	
	// Function to send message to a particular user
	public function sendMessage($senderID, $receiverID, $message)
	{
		$sql = mysql_query("INSERT INTO messages (message, sender_id, receiver_id) values ('$message','$senderID','$receiverID')");
		if($sql)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	// Loading all outgoing messages - NOT IN USE
	public function sentMessages($senderID)
	{
		//$sql = mysql_query("SELECT name, uid, message, time from users, messages WHERE users.uid = messages.receiver_id AND messages.sender_id = '$senderID'");
		$sql = mysql_query("SELECT name, email, uid, message, time from users, messages where users.uid = messages.receiver_id OR users.uid = messages.sender_id SORT BY messages.message ASC");
		if(mysql_num_rows($sql)>0)
		{
			while($row = mysql_fetch_array($sql))
			{
			   $results[] = array(
					'name' => $row['name'], //The comment of the user
					'uid' => $row['uid'], //The UID of the user
					'message' => $row['message'], //The Message of the user
					'time' => $row['time'] //The Message of the user
			   );
			}
			return $results;
		}
	}
	
	// Loading all incoming messages - NOT IN USE
	public function receivedMessages($receiverID)
	{
		$sql = mysql_query("SELECT name, uid, message, time from users, messages WHERE users.uid = messages.sender_id AND messages.receiver_id = '$receiverID'");
		if(mysql_num_rows($sql)>0)
		{
			while($row = mysql_fetch_array($sql))
			{
			   $results[] = array(
					'name' => $row['name'], //The comment of the user
					'uid' => $row['uid'], //The UID of the user
					'message' => $row['message'], //The Message of the user
					'time' => $row['time'] //The Message of the user
			   );
			}
			return $results;
		}
	}
	
	// Loading all users who chatted with user
	public function conversationists($uid)
	{
		$sql = mysql_query("SELECT DISTINCT name, uid from users, messages 
		WHERE (users.uid = messages.sender_id AND messages.receiver_id = '$uid')
		OR (users.uid = messages.receiver_id AND messages.sender_id = '$uid')");
		if(mysql_num_rows($sql)>0)
		{
			while($row = mysql_fetch_array($sql))
			{
			   $results[] = array(
					'name' => $row['name'], //The comment of the user
					'uid' => $row['uid'] //The UID of the user
			   );
			}
			return $results;
		}
	}
	
	// Loading all chat between two users
	public function conversation($uid, $fid)
	{
		$sql = mysql_query("SELECT name, uid, message, time from users, messages WHERE (users.uid = messages.sender_id AND messages.sender_id = '$uid' AND messages.receiver_id='$fid')
		OR (users.uid = messages.sender_id AND messages.receiver_id = '$uid'  AND messages.sender_id='$fid')
		ORDER BY messages.time ASC");
		if(mysql_num_rows($sql)>0)
		{
			while($row = mysql_fetch_array($sql))
			{
			   $results[] = array(
					'name' => $row['name'], //The comment of the user
					'uid' => $row['uid'], //The UID of the user
					'message' => $row['message'], //The message
					'time' => $row['time'] //The message time
			   );
			}
			return $results;
		}
	}
}
?>
