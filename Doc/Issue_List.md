## Issue List

### As a user, I want a Git environment custom prepared for this project so that all developers may participate in development
- **Priority:** 5
- **Weight:** 3
- **Assigned Team Member(s):** Maksim Sadkov

### As a user, I want basic wireframes for the UI Setup for The Pilot
- **Priority:** 10
- **Weight:** 2
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to be able to connect to the frog using the pilot application so that I can drive it
- **Priority:** 15
- **Weight:** 4
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want to have an intuitive interface of the pilot so that I can understand how the frog is moving through
- **Priority:** 20
- **Weight:** 3
- **Assigned Team Member(s):** Tanya Ivanova

### As a user, I want a class diagram of all the entities involved in the project
- **Description:** Designing a class diagram for all the involved entities in the entire project.
- **Priority:** 48
- **Weight:** 3
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want to see what frog detects with its radar to further see the obstacles on the map
- **Priority:** 47
- **Weight:** 4
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want the information about the frog's position, movement and collision points to be logged in CSV so that I can keep track of the mission
- **Priority:** 35
- **Weight:** 3
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to be able to see the obstacles on the map so that I can avoid them when driving the frog manually
- **Priority:** 49
- **Weight:** 4
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want the frog to be able to drive to specific coordinates to send it on missions
- **Priority:** 22
- **Weight:** 8
- **Assigned Team Member(s):** Maksim Sadkov, Radin Soleymani, Tanya Ivanova, Danylo Kurbatov, Anastasiia Khylyk

### As a user, I want to be able to use terminal within the app to send drive commands to the frog
- **Priority:** 45
- **Weight:** 4
- **Assigned Team Member(s):** Maksim Sadkov

### As a user, I want to be able to click on a map and get this point's coordinates so that I can later drive the frog there
- **Description:**
  - Task has to be in the Ground Control FX module
  - Map has to be of size 1000*1000
  - Map must have the same coordinate calculation method as the Frog application (0 coordinate is in the center)
  - Coordinate that is received is output in terminal
- **Priority:** 50
- **Weight:** 3
- **Assigned Team Member(s):** Tanya Ivanova

### As a user, I want the frog to have an algorithm which omits the obstacles so that it can move freely around the map (pseudocode)
- **Description:**
  - Create an algorithm that does not conflict with how the driveToCoordinate method works
  - Give enough details so that it can easily be translated to actual code
  - Write it in word/google doc and send to the team
- **Priority:** 55
- **Weight:** 3
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want to see all of the collision points of the frog in a CSV file so that I can use this information to manipulate the frog
- **Description:**
  - Implement method, already created for CSV files
  - Store x and z coordinates of every collision point
  - Create a new CSV file for collision points
  - Create a new CSV file for command log
- **Priority:** 65
- **Weight:** 3
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to see the direction and route of the frog on the map so that I know where it is driving at a given moment
- **Description:**
  - Add a arrow-like triangle to show the steering wheel angle of the Frog in the pilot app
  - Draw a line behind the Frog, which represents the route
- **Priority:** 60
- **Weight:** 3
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want to be able to use pre-programmed drive commands so that I can send a frog on a mission
- **Description:** Ground Control. Hard code coordinates for tnt, detonator and rock that blocks station. Create buttons on ground control screen to find tnt, find detonator and blow up rock that blocks the station.
- **Priority:** 35
- **Weight:** 5
- **Assigned Team Member(s):** Tanya Ivanova

### As a user, I want to be able to drive frog with avoidance of getting stuck in one place
- **Description:** Refine the algorithms for detecting movement conflicts(getting stuck). Test the enhanced logic with dynamic obstacle scenarios.
- **Priority:** 11
- **Weight:** 5
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want to be able to press on a point on a map and make the frog drive there
- **Description:**
  - Automatic Navigation to Waypoints
  - Implement the logic for navigating and calculating a route to waypoints for Ground Control.
  - Integrate the Radar points visualization feature with The Ground control’s UI (The Frog itself, Radar blip obstacles, the trail behind the frog).
- **Priority:** 16
- **Weight:** 8
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to see the updates from the frog in the terminal
- **Description:**
  - Terminal Output UI and logging
  - Visualizing all the log messages on the terminal UI and connecting it to the CSV Logger.
- **Priority:** 23
- **Weight:** 4
- **Assigned Team Member(s):** Maksim Sadkov

### As a user, I want to be able to drive the frog manually via the ground control app
- **Description:**
  - Ground Control - Add buttons for manual driving
  - Add methods to drive forward, backwards, left, right and stop in MapController class
  - Add buttons to the ground control screen under the map 
  - Link buttons to the methods
  - The frog drives until you press stop.
- **Priority:** 21
- **Weight:** 3
- **Assigned Team Member(s):** Tanya Ivanova

### As a user, I want the radar 2 graph to avoid duplicating visualizations due to an error
- **Priority:** 46
- **Weight:** 6
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want a class diagram scheme of the Database that the program utilizes
- **Priority:** 51
- **Weight:** 2
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to Refactoring the project structure so that the project would be able to function as whole
- **Priority:** 40
- **Weight:** 9
- **Assigned Team Member(s):** Maksim Sadkov

### As a user, I want presentations for sprint reviews
- **Priority:** 56
- **Weight:** 1
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want to log information in an SQLite database using JDBC
- **Priority:** 24
- **Weight:** 5
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to be able to toggle status and radar functionalities with buttons in the UI
- **Priority:** 25
- **Weight:** 3
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want new operations to finish uncompleted ones, if present, without any conflicts
- **Priority:** 26
- **Weight:** 6
- **Assigned Team Member(s):** Maksim Sadkov

### As a user, I want the collision detection to be relatively accurate
- **Priority:** 27
- **Weight:** 7
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want to be able to clear repetitive log messages from the terminal
- **Priority:** 28
- **Weight:** 2
- **Assigned Team Member(s):** Tanya Ivanova

### As a user, I want the database to be able to record data even when all of the programs are running at the same time
- **Description:** Fix the database issue when several applications are running.
- **Priority:** 29
- **Weight:** 3
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want to see the location of the frog and radar points, as well as the destination point on the ground control map
- **Description:** Ground Control. Add radar points on the ground control map (with photo). Also add the current position of the frog and point, selected on the map.
- **Priority:** 30
- **Weight:** 5
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want the obstacle avoidance algorithm to have optimized hyperparameters for better accuracy
- **Priority:** 31
- **Weight:** 5
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want the used mission commands to be stored in the database in ground control
- **Priority:** 32
- **Weight:** 3
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want the databases to be able to import and export information for future use
- **Priority:** 33
- **Weight:** 4
- **Assigned Team Member(s):** Danylo Kurbatov

### As a user, I want a source code that is readable and elegant
- **Priority:** 34
- **Weight:** 2
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want test cases that cover functional requirements
- **Priority:** 70
- **Weight:** 3
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want the documentation to provide insight into the architecture and relevant design decisions
- **Priority:** 36
- **Weight:** 3
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want a video with voice over which demonstrates all important features
- **Priority:** 75
- **Weight:** 10
- **Assigned Team Member(s):** Anastasiia Khylyk

### As a user, I want the documentation to include Retrospective markdown documents for each sprint
- **Priority:** 38
- **Weight:** 1
- **Assigned Team Member(s):** Radin Soleymani

### As a user, I want the documentation to include Planning markdown documents for each sprint
- **Priority:** 39
- **Weight:** 1
- **Assigned Team Member(s):** Radin Soleymani
