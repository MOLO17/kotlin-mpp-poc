
# [POC] Kotlin Multiplatform  
  
This repository contains a proof-of-concept project which demonstrates the usage of the Kotlin MPP   
feature using **Kotlin JVM** and **Kotlin Native** for sharing code between the Android and iOS platforms.  
  
## Requirements  
- Android Studio >= 3.2.1  
- Gradle 4.7   
- Kotlin 1.3.20  
- Xcode >= 10.0  
  
IntelliJ is not required since Android Studio provides almost the same features and gives you the Android plugin  
out of the box.  
  
## Project  
  
This project contains three main folders:    
- `shared` is the directory which contains the sources for the Kotlin MPP shared code;    
- `app` contains the Android application which imports the shared module as Gradle dependency;    
- `ios-app` contains the Xcode project for the iOS application. Open this folder with Xcode only.  
  
### The shared module  
  
The shared module contains the three main source sets that compose a Kotlin MPP.    
- `commonMain` is the directory which contains the shared code. Here goes the pure Kotlin code, which needs to  
be compiled both against JVM and Native;    
- `androidMain` is the directory which contains the Android framework related code. Here you can access to the   
framework classes together with the Java ones. This source set will compile only against JVM;    
- `iOSMain` is the directory which contains the iOS specific code. With the iOS Gradle plugin you can write   
framework-related stuff using Kotlin! Forget the buggy Xcode... for a while. This source set will compile only   
against the Kotlin Native compiler.  
  
The `build.gradle` file of this module implements the `kotlin-multiplatform` plugin using:  
```groovy  
apply plugin: 'kotlin-multiplatform'  
```  
  
Since we need to use Android related classes in the `androidMain` source set, we need to implement also an Android Gradle plugin, [as specified into the documentation](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#supported-platforms).  
We implemented the `library` plugin:  
```groovy  
apply plugin: 'com.android.library'  
```  
By implementing an Android plugin, the project is supposed to have a `main` source directory containing the canonical  
`AndroidManifest.xml`. The only requirement you have is that the file contains a valid package name. Something as simple as:  
```xml  
<?xml version="1.0" encoding="utf-8"?>  
<manifest package="com.molo17.damianogiusti.kotlinnativepoc.shared"/>  
```  
  
The Android plugin allows the `kotlin-multiplatform` plugin to make the `presets.android` extension available,  
allowing us to apply it when defining source targets in the [build.gradle](https://bitbucket.org/MOLO17/kotlin-native-poc/src/master/shared/build.gradle)   
of the shared module.  
  
### The iOS app  
  
The iOS app is contained into the `ios-app/Kotlin-MPP` folder. You can build it by your own by just opening it  
using Xcode.  
  
A build phase has been added to the project for invoking a script on every build. You can find the script into the  
project directory. The script basically invokes the Gradle task which compiles the Kotlin code into a Xcode framework,  
outputting a file named `main.framework` in the `ios-app/Kotlin-MPP/kotlin-outputs` directory, generated at the  
first build.  
  
The *"Framework Search Paths"* has been set to point to the script's output directory, and the built framework has been  
added manually into the project, with the *"Copy if needed"* flag set to `false`.  
  
## Author  

Damiano Giusti (damiano.giusti@molo17.com)  
  
## External References  
  
- https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html  
- https://kotlinlang.org/docs/tutorials/native/mpp-ios-android.html