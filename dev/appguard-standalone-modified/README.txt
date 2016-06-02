README by Konrad Jamrozik.

- The projects here were originally copied from projects kept in 
<repo>\droidmate_external_resources\from_Philipp\appguard\srcs\appguard-standalone.zip
   
- The appguard-standalone has a jar build artifact defined that is placed directly in the dm apk-inliner project as a resource. 
  Note that the appguard-standalone is an inliner, and it requires all the appguard-standalone-* projects to build.
   
HOW TO BUILD:   

1. Delete manually this dir:

C:\my\local\repos\sechair\droidmate-private\dev\appguard-standalone-modified\out

2. Open in IntelliJ this dir:
    
C:\my\local\repos\sechair\droidmate-private\dev\appguard-standalone-modified

3. Build -> Rebuild project

4. Build -> Build Artifacts... -> Edit. 
Check the output path of the artifact is:
C:\my\local\repos\github\droidmate\dev\droidmate\projects\apk-inliner\src\main\resources

5. Build -> Build Artifacts... -> Rebuild

6. Extract from C:\my\local\repos\sechair\droidmate-private\dev\appguard-standalone-modified\out\production\appguard-standalone-loader\appguard-standalone-loader.apk
   the "classes.dex" file. Rename it to "appguard-loader.dex" and put it in C:\my\local\repos\github\droidmate\dev\droidmate\projects\apk-inliner\src\main\resources
   
4. Do clean DroidMate rebuild (gradlew clean build). Test if it works by running testDevice_api23.

