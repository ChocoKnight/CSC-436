# Details for Transaction Tracker Project

1. Transaction Tracker is an app to meant to help users keep track of any purchases they make. 
It can log various aspect of a purchase, like what you bought, where you bought it, some details about the purchase, the type of purchase, and the amount spent. This app will then show users exactly how much they are spending over certain periods of time. 
It can also be used to show users what kind of purchases they are making the most and what they are spending the most on. 
This app was meant to take pictures of receipts from stores you visit to automatically log the transaction information into the app, however this feature was not implemented. 

2. Transaction Tracker Wire Frame can be found [here](TransactionTracker_Wireframe.png/)

3. List of Android and Jetpack Compose Features that made this app possible

    - Composable Functions
    - State Management 
    - Material 3 Components
    - RoomDatabase
        - Flow
    - For the Camera
        - rememberLauncherForActivityResult
        - ActivityResultContracts


4. The minimum SDK is 24, while the target SDK is 34. At the moment, no other outside dependencies are being used. 

5. Things of note

    - The material covered in this project mostly entails how to use Room Databases and the Camera 
    - The transaction lists are lazy lists so they can accomdate any number of purchases within the selected time frame 
