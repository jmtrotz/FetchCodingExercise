# Overview
I created this app as part of the interview process for my application at Fetch Rewards. It
downloads data from <https://fetch-hiring.s3.amazonaws.com/hiring.json> and displays it in a
RecyclerView. Before displaying the data, items with null or blank names are filtered out, 
grouped by their list ID, and sorted by their names.

## Building the application
First, the source code must be downloaded from <https://github.com/jmtrotz/FetchCodingExercise>.
You can download it as a zip file from the GitHub repository, or if you have Git installed on your
PC it can be cloned with
```
git clone https://github.com/jmtrotz/FetchCodingExercise
```

If you're using Linux/MacOS, `cd` into the `/FetchCodingExercise` directory and execute the
following command:
```
./gradlew assemble
```

If you're using Windows, `cd` into the `\FetchCodingExercise` directory and execute the following
command:
```
.\gradlew assemble
```

After it has finished compiling, the APK file will be located in
`~/FetchCodingExercise/app/build/outputs/apk/release`

## Installing and running the application
### Option 1:
Connect your Android device to your PC and copy the APK file to the phone. Use the file
manager on the device to navigate to the directory that the APK file was copied to and select it.
A dialog may appear asking you to enable the "Install unknown apps" setting for your
file manager. If so, please enable this setting and try installing the application again.

### Option 2:
If you have ABD installed on your PC, copy the APK file to the `platform-tools` directory
and execute the following command:
```
adb install app-release-unsigned.apk
```
Once installed, simply open the application and enjoy!

## Troubleshooting
### My device isn't showing up when connected to my PC
1. Ensure that the USB cable is fully inserted into the device and your PC
2. Expand the notifications shade. Tap on the notification that says "USB for charging"
3. Select "File transfer" from the list of options

### The application crashed
1. Please send an email to jmtrotz@gmail.com with details on how to reproduce the
   problem
2. If possible, please capture of a logcat of the crash and attach it to the email