# GestureGeniusApp
GestureGenius is an Android app that aims to facilitate communication between deaf people who practice the Lebanese Sign Language LSL and other individuals. 
After signing up, the main page of the app will offer users two main functions: 
  1) a catalog that contains all the words of the LSL in arabic and in english with their equivalent gesture, equipped with a search-bar for ease of use
  2) an LSL detector, which will open the camera of the phone and start detecting signs made by the user or a deaf person
 The aim of the catalog is to faciliate communication between those who do not know the LSL and those who practice it while the purpose of the LSL detector is to facilitate the understanding of what a deaf person wants to communicate.
 # How To Use The Android GestureGenius App ?
 - First, download this repository as a ZIP file and extract it, or use git and clone this repository using the git clone command. 
 - Once you have the required folder, open it in android studio. 
 - Use your android phone to download the app. You will have to enable developer mode in your phone (see steps to enable developer options here : https://developer.android.com/studio/debug/dev-options).
 -  Once you plug in your device and run the code, the application will be successfully downloaded and you will be able to use it.
 # Limitations and Improvements
 Our model is not fully implemented yet in the app, but we are working on adding a percentage of accuracy of the sign that it is guessing and a square around the hand that is making signs. 
 For that reason, we added to this repo the python codes with which we collected our data and tested our model. In order to use the model with better results, follow these steps:
  1) open PyCharm (Python IDE) or Visual Studio Code (or any app that supports python). We used PyCharm.
  2) download the necessary packages (use any Python version between 3.7 and 3.10 because the new versions do not support yet MediaPipe which is one of the packages needed)
  3) copy the main.py code and the necessary files that are in the folder here (labels.txt and keras_model.h5)
  4) run the code and check for errors
  5) try making signs from the available words and have fun with it :)
  
 Of course we will be adding more words in the near future. This is just a prototype. Around 30 sentences can be formed with the available words.
 Also, make sure that the background of the hand is clean, preferably white, and that the light is neither too low or too bright, since these factors can affect the accuracy of the results.
 # Future Features
 Here are a few of the features we would like to implement in the future:
  - add a learn / test mode similar to the Duolingo app style to help people learn the LSL
  - add more words to the dataset
  - let the model display “sign not available” or “try another angle” to new signs instead of assigning one of the available signs
  - add more than the LSL model to make it international
  - expand it to IOS 
