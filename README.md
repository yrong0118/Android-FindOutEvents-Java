# FindOutEvents
### Find out Events - A LBS based Android app for Local Residents and Tourists in<B><I> Java</B></I>
Find out Events is an android application developed in Java based on the design of the Facebook app. It allows users to like events posted by the other users and add comments. The users can also search for the most popular events nearby in the map page. In addition, users can search for nearby events based on keyword tags and share events with other users.

Find out Events used <B>Google Firebase</B> to store and manage user-generated content including comments, images and geo-locations. It integrated the <B>Google Map API </B>to display the nearby popular events and directions for navigation to the event. It also utilized in-app advertising (Google AdMob) to display Google advertisers and keep users engaged. 
####1) Login/ Sign Up

Used <B>Firebase Authentication</B> to implement login/sign up page that prompts the user to enter their credentials consisting of username and password.
####2）Events List
The news history is displayed in a <B>RecyclerView</B> and shows the event title, description, like count and comments. When there are no liked news articles, the news history displays a message stating “No events”.
####3）Report Events
The user can post the event on this page, the app can fetch its current location. This app also uses the <B>processBar</B> to show the uploading percentage of the image. 
####4）Find out Events
The user can find the events nearby based on their current location.
####5) Authentication 
####6) realtime database
####7) firebase storage
