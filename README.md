# BingBoysGolf

This is the golf game of Group 16 - BingBoysGolf.

This directory contains all relevant code needed to launch the game,
as well as the full source code of the application.

This game uses [LibGDX](https://libgdx.com/) as graphical engine.
Auxiliariy libraries used include:
[BeanShell Interpreter](https://beanshell.github.io/), for interpreting string formulas

Group 16:
- Mohammed Al-Azzani
- Rafali Arfan
- Tom Bakker
- Papuna Berdzulishvili
- Laurent Bijman
- Pie de Boer
- Thomas Vroom



### Instructions TODO

1. To launch the game, open this repository in your favourite IDE and navigate to the following file:
   BingBoysGolf/desktop/src/com/project_1_2/group_16/DesktopLauncher.java
   And run this file to launch the game. (note: on macOS it is recommended to run the game using gradle instead,
   see instruction bellow in the "notes" section)

2. Use the input-textfields on the UI to customise the game.

3. Please read the info-buttons and the controls for further instructions.

4. Press the play button to run the game! You can find all relevant data in the pink labels on the screen.



### Controls
(you can also find this on the title screen)
- MAIN CONTROLS:
  - ESC - Quit the application
  - C - Switch camera
  - R - Reset ball
  - Ctrl + S - Save level

- BALL CAMERA CONTROLS:
  - Drag the mouse to move the camera
  - Hold SPACE to shoot the ball
  - (the ball will be shot in the direction of the camera)

- CINEMATIC CAMERA CONTROLS:
  - W A S D / Arrow keys - Move around
  - SPACE - Go up
  - SHIFT - Go down

- BOT CONTROLS:
  - 1 - Simulated Annealing
  - 2 - Battle Royale Optimization
  - 3 - Particle Swarm Optimization
  - 4 - Rule-Based bot
  - 5 - Random bot



### Notes

- The default numerical solver is RK4. This isn't changable through the UI because we believe it's the best one to use. 
  If you want to switch numerical solvers, navigate to the following file in the source code:
  BingBoysGolf/core/src/com/project_1_2/group16/screens/GameScreen.java
  and change the numerical solver on line 93 to either RK2 or EULER.

- Gradle instructions:
  - make sure you have gradle installed: https://gradle.org/
  - open a terminal and navigate to this repository
  - to run the game, type "gradle run" in the terminal (starting up might take a while)

- Java Runtime Environment must be needed to run the game (minimum 16).
  - https://www.java.com/nl/

- Citation of graphical assets can be found in a seperate file inside the assets folder.

- Citation of code is mentioned in comments throughout the source code.



##### We hope you enjoy BingBoysGolf!

![image text](https://i.ytimg.com/vi/0s2Jzk6yBVk/maxresdefault.jpg)
