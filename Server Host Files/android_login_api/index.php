<?php

/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data

  /**
 * check for POST request 
 */
 
 $siteURL = "Your SITE URL here.";
 $uid="";
 
if (isset($_POST['tag']) && $_POST['tag'] != '') {
 
    // get tag
    $tag = $_POST['tag'];

    // include db handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();

    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);

    // check for tag type
    if ($tag == 'login') {
        // Request type is check Login
        $email = $_POST['email'];
        $password = $_POST['password'];

        // check for user
        $user = $db->getUserByEmailAndPassword($email, $password);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
			$response["id"] = $user["uid"];
			$response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
			
			$profilepic = $siteURL."users/".$user["email"]."/profilepics/1.jpg";
			$response["user"]["profile_pic"] = $profilepic;
			
            echo stripslashes(json_encode($response));
			//echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
			echo json_encode($response);
        }
    } else if ($tag == 'register') {
        // Request type is Register new user
        $name = $_POST['name'];
        $email = $_POST['email'];
        $password = $_POST['password'];
        // check if user is already existed
        if ($db->isUserExisted($email)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($name, $email, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
				$response["id"] = $user["uid"];
                $response["uid"] = $user["unique_id"];
                $response["user"]["name"] = $user["name"];
                $response["user"]["email"] = $user["email"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
			
				//New Code:
				mkdir("./users/".$user["unique_id"]);
				mkdir("users/".$user["unique_id"]."/profilepics");

                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    }
	
	else if($tag == 'get_friends')
	{
	
		//GET FRIENDS OF A USER
		$uid = $_POST["uid"];		
		$friends = $db->getFriends($uid);
		$results = array('friends'=>$friends);
		echo stripslashes(json_encode($results));
		
	}
	
	else if($tag == 'get_user_details')
	{
		//GET DETAILS OF A USER
		$fid = $_POST["fid"];		
		$details = $db->getDetails($fid);
		$results = array('details'=>$details);
		echo stripslashes(json_encode($results));
	}
	
	else if ($tag == 'add_friend')
	{
	
		$uid=$_POST["uid"];
		$fid=$_POST["fid"];

		if ($uid != NULL && $fid != NULL)
		{
			$response["status"] = $db->addFriend($uid, $fid);
		}
		else
		{
			$response["status"] = 2;
		}
		echo json_encode($response);
	
	} 
	
	else if ($tag == 'get_users')
	{
		$string = $_POST["string"];
		if ($string != NULL)
		{
			$users = $db->loadUsers($string);
			$results = array('users'=>$users);
			echo stripslashes(json_encode($results));
		}
	}
	
	else if ($tag == 'count_friends')
	{
		$uid = $_POST["uid"];
		if ($uid != NULL)
		{
			$count = $db->countFriends($uid);
			$results = array('count_friends'=>$count);
			echo json_encode($results);
		}
	}
	
	else if ($tag == 'get_images')
	{
		$uid = $_POST["uid"];
		if ($uid != NULL)
		{
			$images = $db->getImages($uid);
			$results = array('images'=>$images);
			echo stripslashes(json_encode($results));
		}
	}
	
	else if ($tag == 'image_count')
	{
		$uid = $_POST["uid"];
		if ($uid != NULL)
		{
			$count = $db->countImages($uid);
			$results = array('image_count'=>$count);
			echo json_encode($results);
		}
	}
	
	else if ($tag == 'get_requests')
	{
		$uid = $_POST["uid"];
		if ($uid != NULL)
		{
			$requests = $db->getRequests($uid);
			$results = array('requests'=>$requests);
			echo stripslashes(json_encode($results));
		}
	}
	
	else if ($tag == 'accept_friend')
	{
		$uid = $_POST["uid"];
		$fid = $_POST["fid"];
		if ($uid != NULL && $fid != NULL)
		{
			$request = $db->acceptFriend($uid, $fid);
			if($request)
			{
				$response["success"] = 1;
			}
			else
			{
				$response["success"] = 0;
			}
			echo json_encode($response);
		}
	}
	
	else if ($tag == 'reject_friend')
	{
		$uid = $_POST["uid"];
		$fid = $_POST["fid"];
		if ($uid != NULL && $fid != NULL)
		{
			$request = $db->rejectFriend($uid, $fid);
			if($request)
			{
				$response["success"] = 1;
			}
			else
			{
				$response["success"] = 0;
			}
			echo json_encode($response);
		}
	}
	
	else if ($tag == 'is_friend')
	{
		$uid = $_POST["uid"];
		$fid = $_POST["fid"];
		if ($uid != NULL && $fid != NULL)
		{
			$request = $db->isFriend($uid, $fid);
			if($request)
			{
				$check["is_friend"] = 'y';
			}
			else
			{
				$check["is_friend"] = 'n';
			}
			echo json_encode($check);
		}
	}
	
	else if ($tag == 'insert_comments')
	{
		$uid=$_POST["uid"];
		$image_id=$_POST["image_id"];
		$comment=$_POST["comment"];
		
		if ($uid != NULL AND $comment!=NULL AND $image_id!= NULL)
		{
			$result = $db->insertComment($uid, $image_id, $comment);
			if($result)
			{
				$check["inserted"] = 'y';
			}
			else
			{
				$check["inserted"] = 'n';
			}
			echo json_encode($check);
		}
	}
	
	else if ($tag == 'get_comments')
	{
		$image_id = $_POST["image_id"];
		
		if ($image_id!= NULL)
		{			
			$result = $db->getComments($image_id);
			$response = array('comments'=>$result);
			echo json_encode($response);
		}
	}
	
	else if ($tag == 'send_message')
	{
		$senderID = $_POST["sender_id"]; //The UID of the current user
		$message = $_POST["message"]; //The message text.
		$receiverID = $_POST["receiver_id"]; //Recepient ID
		
		$result = $db->sendMessage($senderID, $receiverID, $message);
		if($result)
		{
			$check["inserted"] = 'y';
		}
		else
		{
			$check["inserted"] = 'n';
		}
		echo json_encode($check);
	}
	
	else if ($tag == 'sent_messages')
	{
		$senderID = $_POST["sender_id"]; //The UID of the current user
		
		$result = $db->sentMessages($senderID);
		if($result)
		{
			$results = array('sent_messages'=>$result);
			echo json_encode($results);
		}
	}
	
	else if ($tag == 'received_messages')
	{
		$receiverID = $_POST["receiver_id"]; //The UID of the current user
		
		$result = $db->receivedMessages($receiverID);
		if($result)
		{
			$results = array('received_messages'=>$result);
			echo json_encode($results);
		}
	}
	
	else if ($tag == 'conversationists')
	{
		$uid = $_POST["uid"]; //The UID of the current user
		
		$result = $db->conversationists($uid);
		if($result)
		{
			$results = array('conversationists'=>$result);
			echo json_encode($results);
		}
	}
	
	else if ($tag == 'conversation')
	{
		$uid = $_POST["uid"]; //The UID of the current user
		$fid = $_POST["fid"]; //The UID of the friend user
		
		$result = $db->conversation($uid, $fid);
		if($result)
		{
			$results = array('conversation'=>$result);
			echo json_encode($results);
		}
	}
	
	else
	{
        echo "Invalid Request";
    }
	
}
else
{
    echo "Access Denied";
}
?>