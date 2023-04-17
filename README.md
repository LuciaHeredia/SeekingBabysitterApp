# SeekingBabysitterApp

Final project in an app development course. <br />

The app shows a list of babysitters who registered, were reviewed and approved by the manager, that way anyone whos looking for a babysitter can be sure that the babysitter is a real person. <br />

## App description:
The app contains a MainActivity and Fragments and uses a Navigation Graph to navigate between destinations.<br />
The RecyclerViews contains several MaterialDesignCardView for each User added with the help of an Adapter.<br />
To transfer the selected User from the RecyclerView between the fragments or after the LOGIN, I used the Safe Args wich transferd a JsonString of the User from one Fragment to another.<br />
The app uses Firebase Authentication for the registration, Firebase Realtime Database for storing the data of every user and Firebase Storage for the images uploaded.<br />

In the Home Fragment - 3 buttons:<br />
* APPLAY as a Babysitter.<br />
* SEARCH for a babysitter(A search by gender and city).<br />
* LOGIN after veryfing email.<br />

<img src = "ReadmeImages/HOME-gif.gif" height="580">
<img src = "ReadmeImages/applay_screen.png" height="580"> <img src = "ReadmeImages/list_screen.png" height="580"> <img src = "ReadmeImages/login_screen.png" height="580">

If the MANAGER logged in - 2 buttons:<br />
* SEARCH for a babysitter(A search by gender and city) - same as the above.<br />
* MANAGER - loads a list of users to be reviewed.<br />

<img src = "ReadmeImages/home_manager_screen.png" height="580">
<img src = "ReadmeImages/manager_screen.png" height="580">

If a USER logged in - 2 buttons:<br />
* SEARCH for a babysitter(A search by gender and city) - same as the above.<br />
* UPDATE - loads the information of the user, remarks from the manager are visible and can update the information.<br />

<img src = "ReadmeImages/home_user_screen.png" height="580">
<img src = "ReadmeImages/update_screen.png" height="580">

## Created with:
* Android studio: Kotlin.
* Android version: 12
* SDK version: 31
