# SeekingBabysitterApp

Final project in an app development course. <br />

The app shows a list of babysitters who registered, were reviewed and approved by the manager, that way anyone whos looking for a babysitter can be sure that the babysitter is a real person. <br />

## App description:
The app contains a MainActivity and Fragments and uses a Navigation Graph to navigate between destinations.<br />
The RecyclerViews contains several MaterialDesignCardView for each User added with the help of an Adapter.<br />
To transfer the selected User from the RecyclerView between the fragments or after the LOGIN, I used the Safe Args wich transferd a JsonString of the User from one Fragment to another.<br />
The app uses Firebase Authentication for the registration, Firebase Realtime Database for storing the data of every user and Firebase Storage for the images uploaded.<br />

<img src = "AppPictures/entry page.png">

## App created with:
* Android version: 12
* SDK version: 31
