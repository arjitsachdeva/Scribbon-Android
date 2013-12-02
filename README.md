Scribbon-Android
================

This is a basic skeleton that I was making to create an Image sharing social network in Android for integration with one my other projects. The following functionalities have been implemented primarily:

-User Login/Registration

-Add Friends/View pending friend requests/Accept Friend requests

-Profile pictures

-In-app messaging inbox: Send new message, reply etc.

-View Images shared by other users in a 3 independently scrollable-column layout.

-Image privacy(visible to friends or public).

-Lazy Loading of Images.

-User dashboard(tabular layout).

-Comment/Rate an image etc.

You'll need a place to host the PHP files, as well as a MySQL database.

Setup:
1) Please insert your website URL(where you hosted the PHP files, till the index.php directory) in the UserFunctions.java file inside the com.scribbon package.

2) Insert your website URL in the index.php file.

3) Insert your MySQL database and user details(after you create both) in the config.php file.

4) Go to the MySQL database(via phpMyAdmin or similar), and import the database SQL file directly into it. 

5) Import the Android project in Eclipse or any IDE you use.

6) Run it!

The application is functional, but devoid of any amazing graphics/design. As I said, it's just a skeleton focused on the back-end functionality.
