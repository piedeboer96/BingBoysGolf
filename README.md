# BingBoysGolf

This is the golf game of Group 16 - BingBoysGolf.

This directory contains all relevant code needed to launch the game,
as well as another directory containing the full source code.

This game uses LibGDX as graphical engine.

Group 16:
- Mohammed Al-Azzani
- Rafali Arfan
- Tom Bakker
- Papuna Berdzulishvili
- Laurent Bijman
- Pie de Boer
- Thomas Vroom



### Instructions

1. To launch the game, double click the BingBoysGolf.jar file. 
   If this doesn't work, open a terminal and type the following command:
   java -jar BingBoysGolf.jar

2. Use the input-textfields on the UI to customise the game.

3. If anything is unclear, please read the info-buttons and the controls for further instructions.

4. Press the play button to run the game!



### Notes

- The default numerical solver is RK4. This isn't changable through the UI cause 
  we believe it's the best one to use. 
  If you want to switch numerical solvers, navigate to the following file in the source code:
  BingBoysGolf/core/src/com/project_1_2/group16/screens/GameScreen.java
  and change the numerical solver on line 87 to either RK2 or EULER.
  (changing this will only effect the source code, not the pre-compiled project,
  the main class of the source code is located in the desktop package)

- Java Runtime Environment must be needed to run the game (minimum 16).

- Citation of graphical assets can be found in a seperate file inside the assets folder.

- Citation of code is mentioned in comments throughout the source code.



##### We hope you enjoy BingBoysGolf!

![image text](https://i.ytimg.com/vi/0s2Jzk6yBVk/maxresdefault.jpg)
