# BingBoysGolf

### Bugs 
1. Input Screen on MAC is broken
2. Game logic (spawn ball when out of range

### ToDo
1. RuleBasedBot
2. Fix advancedbot
3. Start working on report

// reminder for self to build executable jar:
// - open new terminal
// - cd to desktop
// - gradle dist command will create an executable jar in build/libs
// use launch4j to wrap the jar


CLEAN UP:
- Desktoplauncher (DONE)
- App (DONE)
- Input (DONE)
- themes (DONE)
- models (DONE)
- misc (DONE)
- camera (DONE)
- math (DONE)

- gamelogic (Only end screen)

- ai (TODO)
- screens (TODO)



INSTRUCTIONS TEXT TODO:
MAIN STAGE:
- controls
ADVANCED STAGE:
- info about MUK, MUS, MUKS, MUSS
TERRAIN STAGE:
- info about drawing splines

----------------------------------------------
OLD README

In this repository you will find the golf game of group 16 for Project 1-2.

The physics engine is based on Euler's method and RK4.
To pick the specific numerical solver the game implements, please comment out the appropriate lines 251/252 in the App.java file
The graphics library used for this game in LibGDX.

### INSTRUCTIONS

1. Go to desktop/src/com/project_1_2/group16/DesktopLauncher.java and change the "os" variable
to your operating system (either "WINDOWS" or "MAC").

2. To modify the input, access the input.txt file located in the same folder as this README file.
note: for the interpreter of the inputfile to work, beware to use the exact same syntax. (e.g whitespaces).

3. Controls of the app:
- Drag the camera around to view the ball from various angles.
- To view the terrain freely, press 'C' to enable the free camera.
- In the free camera, you can move around with WASD or the arrow keys, 
you can go up by pressing 'SPACE' and down by pressing 'SHIFT'.
- To make the ball move according to the input velocity, press 'M'.
note: after pressing 'M' once, pressing it again will not work unless you restart the application.
- To exit the app, press 'ESC'.

4. To run the app, go back to the DesktopLauncher.java file and run the main class.


![image text](https://i.ytimg.com/vi/0s2Jzk6yBVk/maxresdefault.jpg)
