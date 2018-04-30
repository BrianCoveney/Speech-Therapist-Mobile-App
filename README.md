# Speech Therapist Mobile App

Jenkins CI server for automating tests - [link](http://jenkins.briancoveney.com:8080/job/Speech%20Therapist%20Mobile%20App/)

## About 

This project uses Pocketsphinx, a speech recognition library, written by Carnegie Mellon University(CMU). Pocketsphinx is a lightweight speech recognition engine, specifically tuned for handheld and mobile devices.

[https://github.com/cmusphinx/pocketsphinxx
](https://github.com/cmusphinx/pocketsphinx)

### Required steps to use the Pocketsphinx library  

1. Go to the Pocketsphinx Android demo Github page an download [pocketsphinx-android-5prealpha-release.aar
](https://github.com/cmusphinx/pocketsphinx-android-demo/blob/master/aars/pocketsphinx-android-5prealpha-release.aar). 
1. Go to Android Studio. Click File -> New -> New module -> Import Jar/Aar Package (from step 1) -> Finish
1. Open app/build.gradle and add this line to dependencies:
   ```
   compile project(':pocketsphinx-android-5prealpha-release') 
   ```
1. Add these permissions to your project Manifest file:
   ```
   "android.permission.WRITE_EXTERNAL_STORAGE" 
   "android.permission.RECORD_AUDIO" 
   ```
1. Go to Pocketsphinx Android demo page on github and download file assets.xml from ‘models‘ directory, and put it in the app/ folder of your project.
1. Go back to app/build.gradle in your project and add these lines to its absolute end:
   ``` 
   ant.importBuild 'assets.xml'
   preBuild.dependsOn(list, checksum)
   clean.dependsOn(clean_assets) 
   ```
1. On the Pocketsphinx Android demo page, navigate to models/src/main/assets, download the ‘sync’ folder and copy it to your ‘assets‘ folder in your project. This folder contains resources for speech recognition and will be synchronized on the first application run.

### Phonetic dictionary

A phonetic dictionary provides the system with a mapping of vocabulary words to sequences of phonemes. It might look like this:
```
hello H EH L OW
world W ER L D
```
Phonological processes are the patterns that young children use to simplify adult speech. Some children do not outgrow these processes. The are many different patterns of phonological processes, such as 
* Weak syllable deletion:
```
"Telephone" --> "teffone"
"Spider" --> "pider"
```
* Final consonant deletion:
```
"Home" --> "hoe"
"Calf" --> "cah"
```
* Gliding of liquids:
```
"red" --> "wed"
"leg" --> "yeg"
```

### Building a phonetic dictionary

1. Create a text file with a list of the custom words you want to use.
1. Use the online [Sphinx Knowledge Base Tool 
](http://www.speech.cs.cmu.edu/tools/lmtool-new.html) to build a map between these words and their phonological representations.
1. Copy and paste the results into the bottom of this dictionary:
   ``` app > assets > sync > cmudict-en-us.dict ```
1. Finally, rebuild the project.

### Useage

Create a JSpeech Grammar Format file in this directory ``` app > assets > sync >  ```, e.g. ``` test.gram ```.
```
#JSGF V1.0;

grammar test;

public <words> = (
    telephone   | teffone   |
    spider      | pider     |
    bus         | bu        |
    cat         | ca        |
    dog         | dah       |
    leg         | weg       |
    yellow      | yeyo      |
    elephant    | efant     |
    beep        | eep
);
```

The application will now recognise our custom words using this method in our activity:
``` java
private void setupRecognizer(File assetsDir) throws IOException {
    recognizer = SpeechRecognizerSetup.defaultSetup()
            .setAcousticModel(new File(assetsDir, "en-us-ptm"))
            .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
            .setRawLogDir(assetsDir)
            .getRecognizer();
    recognizer.addListener(this);

    // Create grammar-based search for custom word recognition
    File testGrammar = new File(assetsDir, "test.gram");
    recognizer.addGrammarSearch(TEST_SEARCH, testGrammar);
}
```

