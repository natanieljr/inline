README by Konrad Jamrozik.

- The projects here were originally copied from projects kept in 
<repo>\droidmate_external_resources\from_Philipp\appguard\srcs\appguard-standalone.zip
   
- The appguard-standalone has a jar build artifact defined that is placed directly in the dm apk-inliner project as a resource. 
  Note that the appguard-standalone is an inliner, and it requires all the appguard-standalone-* projects to build.
   
HOW TO BUILD:   

1. Open in IntelliJ this dir:
    
C:\my\local\repos\sechair\droidmate-private\dev\appguard-standalone-modified

2. Build -> Build Artifacts... -> Rebuild // Check the output path of the artifact is correct!

3. Extract from C:\my\local\repos\sechair\droidmate-private\dev\appguard-standalone-modified\out\production\appguard-standalone-loader\appguard-standalone-loader.apk
   the "classes.dex" file. Rename it to "appguard-loader.dex" and put it in C:\my\local\repos\github\droidmate\dev\droidmate\projects\apk-inliner\src\main\resources
   
4. Do clean DroidMate rebuild. Test if it work by running testDevice.

