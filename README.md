# ShareATaxi
ESE 543: MOBILE CLOUD COMPUTING

PROJECT REPORT: SHARE A TAXI

PROJECT MEMBERS: AJITH SHRIDHAR HEGDE (109914086) &         HARISHKUMAR KATAGAL (109915793)

1	IDEA

Suppose a passenger has made his plans for travel and knows the time and place of his travel and he is keen on hiring a taxi for his travel. He can then use our android app Share a Taxi to lookup for other people having traveling plans in same direction and around same time and are willing to share the taxi like he wants. And if he does not find any such people, he can save his travel details using the app and later others having similar plans can search for him. 
The user has to first search for other passengers hiring a taxi for same places or in direction on same date and around same time. If the user finds result matching his search, he can select to ride on that taxi with the other user. If there is no matching result, the user can save his travel details on the app, so that other users with similar plans can share the ride. 

2	MOTIVATION

Charges per mile of travel in a taxi are around $3.5. Traveling for a very short distance costs atleast $8. It varies depending on the states in the US. Traveling on a taxi is costly and if the travel distance is more, the cost is also high. Many people, who want to hire a taxi for their travel, do not do so because of the high cost. Especially students, at times do not hire the taxi because of the cost and they cannot afford to travel in a taxi. In cities like NYC, people most of the time hire taxis for their travel. Most of the people prefer taxi to travel to the airport and there will be some people traveling from same location to the airport. 
Traveling in a taxi can be a heavy burden on the pocket of the passengers. So searching for a co-passenger to share a ride can result in decreasing the travel cost and the decrease can be by almost half. Finding such a co-passenger who wants to travel to same place from same place around same time is next to impossible. So our app helps to find the co-passenger who wants to share the taxi.
There are quite a few apps like Uber, Lyft that can be used to hire a taxi to travel. But there is almost no app through which a co-passenger can be searched for travel. Lyft has a feature to check if there is a passenger traveling in same direction but the app is used mainly to hire a taxi. Our app is extensively to find a co-passenger traveling to same place or direction from same starting place and to share the taxi with the passenger.    

3	DESIGN

3.1	MICROSOFT AZURE MOBILE SERVICES

We have used Microsoft Azure as our cloud provider and have setup a database on the cloud. Azure is the cloud computing platform created by Microsoft for building, managing applications through the data centers built by the Microsoft. It provides Platform as a Service (PaaS) and Infrastructure as a Service (IaaS) services and also supports various programming languages and tools.  Apart from cloud storage we have also used Azure for server side authentication using Facebook to login to our application and used Azure Notification service bus to send notifications using google cloud messaging. 

The database on the cloud consists of two tables:-
 1) Data Request Table: This table is designed to store the travel details that the user wants to share the taxi with. The table consists of columns for addresses of start point and the destination point and the time and date of starting. Facebook id obtained for a user during authentication is used as a primary key for this table. Table 1 shows all the fields and their data types. 
 
Table 1: Data request table
<Data type>	Field

destzip	string

date	string

destaddress	string

destcity	string

deststate	string

time	string

fromaddress	string

fromcity	string

fromstate	string

fromzip	string

userId	string

sharefbid	string

sharename	string

shareemail	string

sharephoneno	string

isactive	string

srclong	number

srclat	number

dstlong	number

dstlat	number

Table 2: Facebook Details Table
<Data type>	Field

email	string

phonenumber	number

token	string

UserName	string

userId	string


2) This table is used to store the user personal details such as name, email id and phone number. The primary key of this table is Facebook user id. Table 1 shows all the fields and their data types.
To access the data in the application two java classes are created for the two tables in the database. Each program contains private variables that map to columns of the table. Each variable has its own getter, setter and constructors. For retrieving the data, we create an object of the class and using the query we get the data directly from the table. In the case of multiple rows we create an array list and access the data. 
To query the database in the cloud, we first create objects following objects, 

•	MobileServiceClient – to connect to the database service.

•	MobileServiceTable – to map the java class to the table in the database.

•	ArrayList object of the class created.

final ArrayList<RequestData> results =mTableDb.where().field("userId").eq(UserId).and(mTableDb.where().field("isactive").eq("0")).execute().get();

A sample query is as shown above. All the data that is matching this query is retrieved and is saved in the arraylist variable object. Using getter methods we can retrieve the data from each of the fields. Similarly we can use different queries to insert/update or delete the data from the tables. All these queries will be embedded in async tasks and run as separate threads over main thread.

3.2	APP OPERATION

When the app is launched for the first time, the app user has to use their Facebook login ID and password for authentication. Microsoft Azure authenticates the user who logged in using the Facebook assigning a unique ID for each user. The app uses Facebook user name as the app’s username by default. The user can update his username by selecting the Update Profile option available in the notification drawer. He can also update his email-id and phone no. using this option. The Notification Drawer also displays the user’s Facebook profile picture and user name.
The user, who wants to share a taxi, goes to the Share a Taxi tab. He enters the date and time at which he intends to start his travel. He then enters the address of the starting point where he will be catching the taxi in the fields of starting point. The street address, city, state and zip are stored in the table in the database on the cloud. The address is then used in Google Maps geo-code API to get the longitude and latitude of the place. This value of longitude and latitude is stored in the database against the user in the table.   
Now, a user who wants to search for a taxi which is heading to same destination point from same starting point, the user has to go to Search a Taxi tab. The user has to enter the address of the place he wants to hire the taxi from and also the destination address has to be entered. Now both the longitude and latitude of both the places are got by using the Google Maps geo-code. Both the longitude and latitude are now compared with the longitude and latitude that was saved by the other users in the database. The results which match are derived from the database on the cloud. The user can select one of the results which he wants and then press the Submit button. 
After pressing the Submit button, the user whose request was selected gets a notification on his phone and the details of the travel that includes the addresses of the starting point and destination point are displayed in the Shared Requests tab along with the contact details which includes name, email id and phone no. of the user who had submitted his request for sharing the taxi. 


3.3	USER INTERFACE

The app has user friendly design through which the user can first either select Search a Taxi or can select Share a Taxi. It also has screen which shows the search results and also the shared taxi details. A few UI mockups are as shown below.

3.3.1	NOTIFICATION DRAWER

A notification drawer is present which displays “Welcome” message along with the user profile picture and an option button to update user profile. On clicking Update Profile button a small window opens which has options to update user name, email id and phone no. By default, Facebook name is used as user name for the app. The app user can change his user name using this.  


                                                                                                                   					 
3.3.2	SEARCH A TAXI

The tab is divided into two parts, one to fill starting point details such as street address, city, state along with the date and time of departure. The other part is filling the details of destination point’s address, city and state.  Now the entered details are searched in a database created in the cloud. The search is based on the starting point and destination point. The results are returned even if the searched destination lies in the direction of a destination point stored in the data base with same starting point.

3.3.3	SHARE A TAXI

In this, the user enters the details of his travel which includes the date and time of start, address of the start point that includes street address, city and state and also the details of the destination point with same details. The starting point details and destination point details are stored in the database on the cloud. 

3.3.4	MY REQUESTS 

This tab displays the travel details that were saved in the database of the taxi that the user wants to share. All the requests the user had submitted using the Share a Taxi tab are shown here. Each request that were made previously can be deleted using Delete button. On clicking the Delete button, the entry in the database table on the cloud is deleted and it disappears even from the My Requests tab. For each of the bookings there is a delete button which can be used to delete the request from the database.

3.3.5	SHARED REQUESTS

First, a user stores the information of his travel using Share a Taxi tab. Then another user searches for a taxi based on his travel details using Search a Taxi tab and the results that matches his search are displayed as an alert. The user can select one of the results which he wants and then press the Submit button. After pressing the Submit button, Shared Requests tab has the details of the travel that includes the addresses of the starting point and destination point along with the contact details which includes name, email id and phone no. of the user who had submitted his request for sharing the taxi. Each of these requests will have a Cancel button which can be used by the user to cancel the request. After canceling the request, it reappears in the My Requests tab.

3.4	APIS USED IN THE APP

The following APIs have been used in our app:

1) Facebook Graph API: This API is used to retrieve the Facebook user’s name and profile picture. This                               API is provided by Facebook for the android. To get the profile picture, a url is created as shown below and an http url connection is made. After the connection is established, a buffered input stream is used to get the input stream and is decoded and saved as a bitmap image. This bitmap image is then rendered in the navigation drawer as profile photo. To retrieve the Facebook username server side java script is written which extracts the username from the json feed which is got using the url and token for the user.

2) Google Maps geo-code API: This API is provided by the Google. This API is used to get the latitude and longitude of a place by giving the address of the place as the input. This is a JSON request and a url is created using the address entered by the user. After we get the JSON feed back, we parse it to get the latitude and longitude values. In the app, we use this API to get the latitude and longitude of the starting point address and destination point address which are then stored in database on the cloud.

3) Google Cloud Messaging API: This API provided by Google provides a channel for communication and it uniquely identifies each of the android devices that has the app installed by assigning each of them a unique ID. The project ID that is obtained from this GCM API is used in the Azure notification hub.

4) Azure Notification Hub: This API is provided by the Microsoft Azure. This sends notification to the user when other user chooses to share the user’s taxi.  When the application is first launched on a device, each device registers itself with the hub using GCM and an unique id is required by the hub for each device that has this application. This unique id is then used to send notification to a particular device by the Azure hub.

4	TECHNOLOGIES USED

4.1	RECYCLER VIEW

To create complex lists and cards with material design style in the app, Recycler View has been used. Recycler View replaces the List View that was previously used. Recycler View is newly introduced in android Lollipop. It has many advantages over list view like its very easy to modify the data in the recycle view compared to the list view. And like list view this also uses an adapter to bind data to a fragment. Recycler view together with card view make the appearance more elegant.


4.2	ANDROID STUDIO

We have developed the android app using the android development application Android Studio. Android Studio is the new application that provides the environment for developing android application. We used Android Studio because of the additional features compared to Eclipse and are easy to develop apps as compared to Eclipse. 

4.3	MATERIAL DESIGN

We have built our app based on the Google’s new design language called Material Design. Material design is a comprehensive guide for visual, motion, and interaction design across platforms and devices.  It has an increased use of grid layouts, responsive animations and transitions, depth effects and padding. App built using Material Design is more attractive, more user-friendly. The icons used in our app are the ones present in Material Design. 

5	TECHNICAL CHALLENGES

Following are some of the technical challenges we faced,

1.	There are many cloud providers like Google, Amazon and Microsoft. But there are no proper tutorials for setting up the cloud and creating database. In few setting up the cloud was easy but creating databases in it was not.  
2.	There were restrictions on the cloud usage such as number of days it could be used for free, number of requests per day, amount of data that can be stored on the cloud, etc. So selecting the right cloud provider for our app considering all these was important. 
3.	Also all these cloud provider provide these trial services only for 1 or 2 months. In our project since we used Azure which provides 1 month free trial services, we had to migrate services three times to new accounts. And also there is no proper way to migrate the data. Everything was supposed to be done manually and was time consuming. 
4.	Another challenge that we faced was handling the async tasks and threads. Since we had a lot of interactions with the cloud database and to access a service we have to either user async task or threads. The main issue was to keep everything in sync. We faced issues where some tasks took longer time to finish and some took less time. It was hard to keep them in sync unless you delay the other.
5.	Also since we are using trial versions, the service are very slow and response time is unpredictable. 



6	EVALUATION    
We created a number of test cases to validate the functionality of our application. The test cases also included a number of negative scenarios like can we select a date in the past, or can we leave all the edit texts blank and save the data etc. All these scenarios are now covered in the application. Since our application has lot of cloud and api interaction, the performance of our application solely depends on the response times of the cloud api’s. We tried to find the average time of response but as said before since we are using free trial services  the service are very slow and response time is unpredictable. We also evaluated the performance of each of the threads these again depend on services. Also amount of on mobile device memory for storing data is minimal in our application since all the data is stored inside the cloud. 

