# FindOutEvents

### Find out Events - A LBS based Android app for Local Residents and Tourists in<B><I> Java</B></I>
Find out Events is an android application developed in Java based on the design of the Facebook app. It allows users to like events posted by the other users and add comments. The users can also search for the most popular events nearby in the map page. In addition, users can search for nearby events based on keyword tags and share events with other users.

Find out Events used <B>Google Firebase</B> to store and manage user-generated content including comments, images and geo-locations. It integrated the <B>Google Map API </B>to display the nearby popular events and directions for navigation to the event. It also utilized in-app advertising (Google AdMob) to display Google advertisers and keep users engaged. 
#### 1) Login/ Sign Up
Used <B>Firebase Authentication</B> to implement login/sign up page that prompts the user to enter their credentials consisting of username and password.

![](https://github.com/yrong0118/FindOutEvents/blob/master/images/login.png)

#### 2）Events List
The news history is displayed in a <B>RecyclerView</B> and shows the event title, description, like count and comments. When there are no liked news articles, the news history displays a message stating “No events”.

![](https://github.com/yrong0118/FindOutEvents/blob/master/images/eventList.png)

#### 3）Report Events
The user can post the event on this page, the app can fetch its current location. This app also uses the <B>processBar</B> to show the uploading percentage of the image. 

![](https://github.com/yrong0118/FindOutEvents/blob/master/images/report%20event.png)

#### 4）Find out Events
The user can find the events nearby based on their current location.

![](https://github.com/yrong0118/FindOutEvents/blob/master/images/map.png)

#### 5) Authentication 

![](https://github.com/yrong0118/FindOutEvents/blob/master/images/authentication.png)

#### 6) realtime database

![](https://github.com/yrong0118/FindOutEvents/blob/master/images/firebase-database.png)

#### 7) firebase storage
![](https://github.com/yrong0118/FindOutEvents/blob/master/images/storage.png)
