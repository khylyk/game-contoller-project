# Test Cases

## TC 1.1

**Name**  
Testing application startup.

**Description**  
Testing whether the application for the Ground Control and Pilot start correctly.

**Requirements**  
RRB-03, RFS-01, RFB-02, RFB-01, RGS-01

**Preconditions**  
The user has started the application TheFrog.exe before starting the Java applications.

| Step | Action                                                                 | Expected Results                                                      |
|------|------------------------------------------------------------------------|-----------------------------------------------------------------------|
| 1    | The user goes into GroundControlFX module, locates the file Main.java, and runs it. | Three application windows open: Ground Control, Terminal, and Radar. |

**Postconditions**  
User has started all of the applications.  
The test cases under the number of 2 test the GroundControlFX module.

## TC 2.1

**Name**  
Testing Ground Control application.

**Description**  
Testing whether the application for the Ground Control performs all of its functionality.

**Requirements**  
RGU-01, RGU-02, RGU-03, RGB-01, RGB-02, RGB-04

**Preconditions**  
The user has all 3 applications running together with The Frog.

| Step | Action                                                                                                                                  | Expected Results                                                                                                                                                     |
|------|-----------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1    | The user presses an empty point on the map in the Ground Control application. The point must be at a place where there are no obstacles. | The Frog in the Frog application starts driving. The Ground Control application shows the point pressed as a bright green circle, the visible obstacles as clusters of red dots, and the frog position as a blue circle with a grey trace behind. |
| 2    | The user clicks a different point on the Ground Control map where there is no obstacle.                                                 | The Frog changes its direction to the new point. The green circle appears at the new destination point and disappears from the previous one. The obstacles, the frog’s position, and trace are still shown.                            |
| 3    | The user clicks the “Stop” button in the Ground Control application.                                                                    | The Frog stops its movement. Everything displayed on the Ground Control map remains the same as before clicking the “Stop” button.                                                                           |
| 4    | The user clicks another point on the Ground Control map again.                                                                          | The Frog starts its movement toward the chosen point. The green circle changes to the newly chosen point. The obstacles, the frog’s position, and trace are still shown.                                      |
| 5    | The user clicks either of the buttons “Forward,” “Right,” “Left,” or “Backwards.”                                                       | The Frog changes its movement from the direction to the previous point to the direction of the button clicked. The obstacles, the frog’s position, and trace are still shown.                               |
| 6    | The user clicks the “Stop” button in the Ground Control application.                                                                    | The Frog stops its movement. Everything displayed on the Ground Control map remains the same as before clicking the “Stop” button.                                                                           |

**Postconditions**  
User was able to drive the Frog to a desired location using the Ground Control application.

## TC 2.2

**Name**  
Testing Ground Control application’s pre-programmed mission commands.

**Description**  
Testing whether the commands for completing a mission work as intended.

**Requirements**  
RFS-05

**Preconditions**  
The user has all 3 applications running together with The Frog.

| Step | Action                                                           | Expected Results                                                                                                 |
|------|------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------|
| 1    | The user presses the “Find TNT” button in the Ground Control application. | The Frog drives to the location of the TNT and picks it up.                                                      |
| 2    | The user presses the “Find detonator” button in the Ground Control application. | The Frog drives to the location of the detonator and picks it up.                                                |
| 3    | The user presses the “Destroy the rocks” button in the Ground Control application. | The Frog drives to the location of the rocks before the base and then blows them up.                             |

**Postconditions**  
User is able to complete the Frog game’s mission by using buttons in the ground control.

## TC 2.3

**Name**  
Testing the terminal application and export features.

**Description**  
Testing whether the terminal is functional and up to the requirements.

**Requirements**  
RGB-03, RGU-04, RGS-02, RGS-03, RGS-04, RGS-05, RFB-04, RFS-03

**Preconditions**  
The user has all 3 applications running together with The Frog.

| Step | Action                                                                                                     | Expected Results                                                                                                           |
|------|------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|
| 1    | The user types DRIVE 1 0 1 in the Terminal and presses ENTER.                                              | The Frog drives forward for 1 second. The terminal displayed “DRIVE 1 0 1” in its message area.                            |
| 2    | The user chooses a destination point on a map in the Ground Control application and opens the Terminal window. | The terminal displays a sequence of RADAR and STATUS messages while the Frog is driving.                                   |
| 3    | The user clicks “Toggle radar” button in the Terminal window while the Frog is driving.                    | The Terminal stops showing new radar messages; it keeps showing new status messages.                                       |
| 4    | The user types “RADAR ON” in the Terminal and presses ENTER (while the Frog is driving to a destination point). | Radar messages start showing again in the Terminal.                                                                        |
| 5    | The user clicks the “WayPoints” button in the terminal.                                                    | The terminal shows the message “Way points have been successfully saved.” If the user goes to the database they can see the way points there. There is a new CSV file created in the project folder with the way points. |
| 6    | The user clicks the “Terminal Log” button in the terminal.                                                 | The terminal shows the message “Terminal log has been successfully saved.” If the user goes to the database they can see the terminal log there. There is a new CSV file created in the project folder with the terminal log. |
| 7    | The user clicks the “Collision points” button in the terminal.                                             | The terminal shows the message “Collision points have been successfully saved.” If the user goes to the database they can see the collision points there. There is a new CSV file created in the project folder with the collision points. |

**Postconditions**  
User has tested the Terminal and can direct the Frog through it. The user can see the status and radar messages in the Terminal and turn them on or off. The user can export waypoints, terminal log, and collision points.

The test cases under the number of 3 test the PilotFX module.

## TC 3.1

**Name**  
Testing the radar map of the PilotFX module.

**Description**  
Testing whether the radar map of Pilot is functional and up to the requirements.

**Requirements**  
RFU-01, RFU-02, RFU-03

**Preconditions**  
The user has all 3 applications running together with The Frog.

| Step | Action                                                                                                  | Expected Results                                                                                   |
|------|---------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| 1    | The user drives the Frog manually using the Frog application while Radar Map is open.                   | The Radar Map shows all of the obstacles as well as the Frog’s position.                            |
| 2    | The user sends the Frog to a coordinate using the Ground Control map while Radar Map is open.           | The Radar Map shows all of the obstacles as well as the Frog’s position.                            |
| 3    | The user clicks the “Stop” button on the Ground Control application and then clicks either of the buttons “Forward,” “Right,” “Left,” or “Backwards.” | The Radar Map shows all of the obstacles as well as the Frog’s position.                            |
| 4    | The user clicks the “Stop” button on the Ground Control application and then writes “DRIVE 1 0 1” in the Terminal. | The Radar Map shows all of the obstacles as well as the Frog’s position.                            |

**Postconditions**  
User has tested the Radar Map and can see the collision points and the position of the Frog whenever it is driving.

## TC 3.2

**Name**  
Testing the algorithms of the Frog driving in the Pilot.

**Description**  
Testing whether the Frog calculates the path correctly and the effectiveness of obstacle avoidance.

**Requirements**  
RFS-04, RFS-05, RFS-06

**Preconditions**  
The user has all 3 applications running together with The Frog.

| Step | Action                                                                                                           | Expected Results                                                                                                                                       |
|------|------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1    | The user chooses a point on the Ground Control map in a straight line from the Frog, ensuring there are no obstacles between the Frog and the point. | The Frog changes its direction to the destination point’s. It keeps driving until it reaches the destination, then it stops.                           |
| 2    | The user chooses a point on the Ground Control map in a straight line from the Frog and behind it, ensuring there are no obstacles between the Frog and the point. | The Frog changes its direction by turning around and facing the point. It keeps driving until it reaches the destination, then it stops.               |
| 3    | The user chooses a point on the Ground Control map that has some obstacles in between itself and the Frog.       | The Frog changes its direction to the destination point and drives there avoiding all obstacles on its way.                                             |

**Postconditions**  
User has tested the mobility of the Frog by checking its calculation of destination points and obstacle avoidance algorithms.
