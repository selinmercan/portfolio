# USC CS310 Project: Trojan Check-In / Check-Out #

### To Run the Project ###
- Set Up Emulator in Android Studio AVD: Pixel 3a XL
- Profile Pictures: Place any photos in an Android Studio Device Emulator File Explorer (path: /storage/emulated/0/Pictures) <br>
&nbsp;&nbsp;&nbsp;&nbsp; images can also be uploaded in the app by supplying links to the desired image(s)
- QR Codes: Set QR Code image to Device Emulator Background Picture (for QR Code Image, look in zip folder) <br>
&nbsp;&nbsp;&nbsp;&nbsp; If you would not like to upload images, set the camera in AVD settings for the Emulator to be your system webcam <br>
&nbsp;&nbsp;&nbsp;&nbsp; QR code images can be found in the submission folder, titled 'qr_codes.png'
- Run

### CSV Format ###
* the import csv function supports 3 commands: add, del, and update.
* the add command requires a building name and capacity: "add, new building name, capacity"
* the update command requires a building name and capacity: "update, existing building name, capacity"
* these capacities must be valid integers >= 0
* the del (delete) command requires just a building name: "del, existing building name"
* whitespace lines are skipped in the csv so they can exist
* lines with commands other than add, del, or update are skipped
* changes are only made once it is determined the csv is valid 

### Project Details ###
* using Android API 26 (Oreo 8.0)
* tested on Pixel 3a XL Emulator

### Team 19 ###
* Judy Song / 8702176522
* Selin Elif Mercan / 3107173607
* Lauren Krikorian / 7659012076
* Abbas Zaidi / 5090748694
* Lucas Makdessi / 3049715606
* Sam Mesfin / 3811279442
<br>

