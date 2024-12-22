# technical design of "the frog" project
team dhi1v.so-3

anastasiia khylyk  
danylo kurbatov  
radin soleymani  
maksim sadkov  
tanya ivanova  

## 1. architecture

### classes and methods

#### modules
there are two modules, in which the classes of the project are stored:

groundcontrolfx and pilotfx. the groundcontrolfx module contains everything related to the features of ground control map (see image below).

the pilotfx module contains all classes related to the pilot features, which are showing radar points on a separate map and terminal, as well as navigation algorithms.

![Ground Control Map](https://cdn.discordapp.com/attachments/1232097711625605171/1254549879267524679/adsasdd.png?ex=6679e61f&is=6678949f&hm=15f018836326232fd27c3514c5ba1751bb7ec48119b8dad802309f28a9c40b3f&)

#### ground control module
the ground control module contains a folder "controller", which has a class named `mapcontroller`. outside of this folder, there is a `main` class. the `mapcontroller` class has all of the methods necessary for displaying the map, showing the obstacles and the position of the frog with its trajectory, and adding functionality to the movement buttons under the map.

##### methods in `mapcontroller` class:

- `void initialize()`
  - connects to the `pilot` class in the pilotfx module. ensures radar points detected by the pilot can be accessed via the `mapcontroller` class, as well as the frog position. synchronizes processes within the two applications, ensuring the same radar points are displayed on both the ground control map and the pilot’s radar point map.

- `void handlemouseclick(mouseevent event)`
  - provides the functionality of clicking on the ground control map to choose a coordinate and then sending the drive command of the frog there. it gets the coordinates of the mouse click and transfers them to the system of coordinates used by the frog. it then calls `handledraw(double x, double z)`, which draws a circle at the point where the mouse was clicked.

- `void handledraw(double x, double z)`
  - draws a bright green filled circle at the point where the mouse was clicked.

- `void buttonclicked(actionevent event)`
  - includes an if statement that calls a respective method when one of the 5 ground control movement buttons is clicked. it ensures that if the frog is already driving in a certain direction or to a coordinate, the previous thread is interrupted and a new thread is started.

- `void driveforward()`, `void driveleft()`, `void driveright()`, `void drivebackwards()`
  - call a `drive()` method with parameters depending on the direction. for forward, the power is 1 and the angle is 0; for left, the power is 1 and the angle is -30; for right, the power is 1 and the angle is 30; and for backwards, the power is -1 and the angle is 0. the duration for each drive command is 0.5 seconds.

- `void stop()`
  - does nothing if there is no current process running. if a process is running, it stops it by interrupting its thread.

- `void drive(double power, int angle, double duration)`
  - sends a drive command to the frog through the pilot in a new thread. fetches messages from the pilot to ensure frog position and radar points are updated.

- `void updatefrogposition(double x, double z, double frogangle)`
  - draws the frog’s position and the trail of the path it has covered on the ground control map. scales the coordinates received to translate them from the frog’s system of coordinates to the javafx one. checks that the scaled coordinates fit on the canvas and draws them only in this case to avoid errors.

- `void onradarupdate(double x, double z)`
  - draws obstacles (sets of radar points) on the ground control map. scales them according to javafx’s system of coordinates and draws them only if they fit on the canvas.

- `void clearcanvas(graphicscontext gc)`
  - clears the canvas on which the frog’s position, trail, and obstacles are drawn. necessary for successfully updating the frog’s position.

- `void onbuttonfindtnt()`
  - drives the frog to the coordinate where tnt is located. triggered by the corresponding button in the user interface and uses the `drivetocoordinate` method with predefined coordinates (-223, -345) for the tnt location.

- `void onbuttonfinddetonator()`
  - drives the frog to the coordinate where the detonator is located. triggered by the corresponding button in the user interface and uses the `drivetocoordinate` method with predefined coordinates (-147, 241) for the detonator location.

- `void onbuttondestroytherocks()`
  - drives the frog back to the base entrance, which is blocked with rocks. triggered by the corresponding button in the user interface and uses the `drivetocoordinate` method with predefined coordinates (1, -100) for the base entrance.

- `void exportradarpoints()`
  - handles export of radar points from the database. draws a point on the map for each received point.

##### methods in `main` class:
- `void start(stage stage)`
  - runs three fxml files simultaneously. opens the window for the ground control map, the pilot radar map, and the terminal. the .fxml scenes for these applications are stored in the "resources" folder, along with the map image displayed on the ground control.

##### methods in `terminallauncher` class:
- `void start(stage stage)`
  - initializes the `pilot` instance and loads the terminal view user interface. sets up the stage with the terminal view fxml file and displays the window.

- `public static void main(string[] args)`
  - launches the javafx application. entry point of the application.

##### methods in `terminalviewcontroller` class:
- `void initialize()`
  - sets up listeners for status and radar updates from the `pilot` class. configures the terminal text area to automatically scroll to the bottom when new text is added, ensuring the latest messages are always visible.

- `void onenterpressed(keyevent event)`
  - handles the enter key press event in the input field. validates and executes the command entered by the user. if the frog is currently running, it raises an alert. valid commands are added to the terminal log and executed by the pilot.

- `void savecollisionpoints()`
  - saves the current collision points to the database and logs the action in the terminal. ensures all detected collision points are persisted.

- `void savewaypoints()`
  - saves the current waypoints to the database and logs the action in the terminal. ensures all waypoints are persisted.

- `void saveterminallog()`
  - saves the terminal log to the database and logs the action in the terminal. ensures all command logs are persisted.

- `void onstatusreceived(string status)`
  - handles status messages received from the `pilot`. logs the status message in the terminal if status messages are enabled. duplicate status messages within a short period are ignored to reduce clutter.

- `void onradarreceived(double x, double y)`
  - handles radar messages received from the `pilot`. logs the radar data in the terminal if radar messages are enabled.

- `void togglestatus()`
  - toggles the display of status messages in the terminal. updates the text of the toggle button to reflect the current state (show/hide status messages).

- `void toggleradar()`
  - toggles the display of radar messages in the terminal. updates the text of the toggle button to reflect the current state (show/hide radar messages).

- `private void raisealertwindow(string message)`
  - raises an alert window with the given message. used to notify the user of errors or important information.

- `private void validateline(string line)`
  - validates the format of a command line. throws an illegalargumentexception if the command line is invalid.

- `private void validatestatuscommand(string[] lineparts)`
  - validates the format of a status command. throws an illegalargumentexception if the command is invalid.

- `private void validateradarcommand(string[] lineparts)`
  - validates the format of a radar command. throws an illegalargumentexception if the command is invalid.

- `private void validatedrivecommand(string[] lineparts)`
  - validates the format of a drive command. throws an illegalargumentexception if the command is invalid.

#### pilot module
the pilotfx module contains three folders: "controller", "model", and "utils", as well as two classes not belonging to any folder. in the "controller" module, there are classes that interact with fxml files for the pilot. the `radarpanelcontroller` class has methods needed to manage the radar point fxml view, displaying radar points and the frog’s position. the `terminalviewcontroller` class handles user interactions with the terminal window, such as pressing buttons and sending commands in the text area.

the "model" folder includes classes representing objects in the pilot-frog-ground control scope. the `pilot` class represents the pilot object, and `updatelistener` is an interface used by the pilot class. the `pilot` class has methods responsible for frog navigation and receiving information about its status and radar points.

the "utils" folder includes classes providing data storage functionality. the `csvlogger` class handles writing into csv files, the `database` class stores data in a database, and the `logger` class is used by both the other classes.

finally, there is one class outside of folders: `radarpoint`. it is a runnable class that starts the fxml scene with the radar point map.

##### methods in `radarpanelcontroller` class:
- `void initialize()`
  - connects to the `pilot` class, adding listeners for radar updates and frog position updates. ensures radar data and frog positions are updated on the radar panel. sets the initial status label to indicate that the radar is waiting for data.

- `void updatefrogposition(double x, double z, double frogangle)`
  - draws the frog's position on the radar canvas and an arrow indicating its direction. translates the coordinates from the frog's coordinate system to the javafx coordinate system. ensures the frog's position is within the visible area of the canvas before drawing it.

- `void onradarupdate(double x, double z)`
  - handles radar updates by drawing radar points on the radar canvas. translates the coordinates from the frog's coordinate system to the javafx coordinate system. ensures the radar points are within the visible area of the canvas before drawing them. logs duplicate radar points but does not draw them again.

- `void clearradar(graphicscontext gc)`
  - clears the radar canvas. used to refresh the radar display and avoid clutter from old data points.

- `void clearfrog(graphicscontext gc)`
  - clears the frog canvas. used to refresh the frog's position display and avoid clutter from old data points.

##### methods in `pilot` class:
- `static pilot getinstance()`
  - returns the singleton instance of the pilot class. ensures there is only one instance of the pilot class throughout the application.

- `boolean isrunning()`
  - checks if the frog is currently running. returns true if the frog is running, false otherwise.

- `void setrunning(boolean running)`
  - sets the running state of the frog. used to start or stop the frog's movement.

- `void addstatuslistener(statuslistener listener)`
  - adds a status listener. the listener will be notified of status updates from the frog.

- `void addradarlistener(radarlistener listener)`
  - adds a radar listener. the listener will be notified of radar updates from the frog.

- `void addfroglocation(froglocation locator)`
  - adds a frog location listener. the listener will be notified of frog position updates.

- `void drivefrogtocoordinate(double x, double z)`
  - drives the frog to the specified coordinates. calculates the required angle and sends drive commands to the frog. handles obstacle avoidance if the frog gets stuck.

- `void senddrivecommand(double motorpower, double steeringangle, double duration)`
  - sends a drive command to the frog. constructs the command string and sends it to the sasacommunicator.

- `void executecommand(string command)`
  - executes a command by sending it to the sasacommunicator. used to send custom commands to the frog.

- `void sleep(long ms)`
  - pauses the current thread for the specified number of milliseconds. used to introduce delays between commands.

- `void fetchmessages()`
  - fetches messages from the sasacommunicator and processes them. ensures that status and radar updates are received and handled.

- `private void avoidobstacle()`
  - attempts to avoid an obstacle by reversing and steering. used when the frog detects it is stuck.

- `private void processmessage(string message)`
  - processes a message from the sasacommunicator. handles status and radar messages, updates the frog's position, and notifies listeners. checks if there is an obstacle in front of the frog.

##### methods in `updatelistener` interface:
- `void onupdatereceived(string message)`
  - called when an update message is received. implementing classes should define the behavior for handling update messages.

##### methods in `radarpoint` class:
- `void start(stage stage)`
  - loads and displays the radar panel user interface. initializes the stage with the radar panel fxml file and sets the window title and dimensions.

##### methods in `csvlogger` class:
- `csvlogger(string filepath)`
  - initializes the csvlogger with the specified file path. clears the existing csv file at the specified path.

- `void insertxzpoints(double x, double z)`
  - inserts xz coordinates into the csv file. writes the coordinates to the file with the appropriate headers.

- `void insertcommandlog(localtime time, string command)`
  - inserts a command log with a timestamp into the csv file. writes the log entry to the file with the appropriate headers.

- `private void clearcsv()`
  - clears the csv file by creating a new empty file at the specified path. called during the initialization of the csvlogger.

- `private boolean checkforheader(string[] header)`
  - checks if the csv file contains the specified header. reads the file and compares each line to the header.

- `private void addtocsv(string[] header, string[] data)`
  - adds data to the csv file, ensuring the header is written if it does not already exist. appends the data to the file.

##### methods in `database` class:
- `database(string url)`
  - initializes the database with the specified database url. establishes a connection to the database.

- `void addwaypoints(double x, double z)`
  - adds waypoints to the database. inserts the xz coordinates into the waypoints table.

- `void addcollisionpoints(double x, double z)`
  - adds collision points to the database. inserts the xz coordinates into the collisionpoints table.

- `void addcommandlog(localtime time, string command)`
  - adds a command log to the database. inserts the timestamp and command into the commandlog table.

- `private void insertpoints(string sql, double x, double z)`
  - inserts points into the database using the specified sql statement. executes the prepared statement with the given coordinates.

- `private void insertcommand(string sql, string time, string command)`
  - inserts a command log into the database using the specified sql statement. executes the prepared statement with the given time and command.

- `list<string> exportradarpoints()`
  - exports saved radar points from the collisionpoints table in the ground control database.

##### methods in `logger` class:
- `static void addcollisionpoints(double x, double z)`
  - adds collision points to the log. ensures the points are logged only if the system is not currently uploading data.

- `static void addwaypoints(double x, double z)`
  - adds waypoints to the log. ensures the points are logged only if the system is not currently uploading data.

- `static void addcommandlog(localtime time, string command)`
  - adds a command log entry to the log. ensures the log entry is recorded only if the system is not currently uploading data.

- `static void uploadwaypointstodatabase()`
  - uploads waypoints to the database and the corresponding csv file. sets the uploading flag to true to prevent concurrent modifications.

- `static void uploadcollisionpointstodatabase()`
  - uploads collision points to the database and the corresponding csv file. sets the uploading flag to true to prevent concurrent modifications.

- `static void uploadcommandlogtodatabase()`
  - uploads command logs to the database and the corresponding csv file. sets the uploading flag to true to prevent concurrent modifications.

- `list<string> exportradarpointsfromdb()`
  - returns radar points as a list of strings.

### database
the project includes two databases: one for the ground control module and the other for the pilot module. there are three classes responsible for saving the data: `database`, `csvlogger`, and `logger`. in contrast to the first two classes, the `logger` class is static and can be called from any place in the project. the logger class saves two types of data: coordinates of obstacles and terminal commands, to the lists. after this, the user can press one of the "save" buttons in the terminal, and these lists are uploaded both to the database and csv files. the `database` class connects to the database and has methods with pre-made sql statements to insert data into the tables. the `csvlogger` class writes to a csv file and has methods for handling writing different types of data.

below is the class diagram of the database that shows the tables and fields in them. all of the tables are independent, which means they do not relate to the other tables. data types of the fields are indicated in parentheses.

![Class Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254549888042008586/asddassad.png?ex=6679e621&is=667894a1&hm=22d11250010cb1a1bf7863665cfefa4847547ebf6e28c23baaa865f577aa644d&)

#### architectural decisions

1. exporting and importing is in the pilot, even though under ground control requirements there is

![Class Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254549953955627059/asdsefgfrw.png?ex=6679e631&is=667894b1&hm=0392acf8559086b4adab0a687b5843406ee53922866b817c9fd6c1873bb46091&)

#### sasacommunicator
- connected to frog
  - handles all communication between the system and the frog. sends commands to the frog and receives updates, acting as an intermediary.

- used by pilot
  - the `pilot` class uses an instance of `sasacommunicator` to send commands to the frog and receive status updates. `pilot` delegates network communication management to `sasacommunicator`.

#### pilot
- manages communication with `sasacommunicator`
  - uses `sasacommunicator` to interact with the frog. sends commands through `sasacommunicator` and processes received messages.

- interacts with `logger`
  - logs significant events like movement commands, status updates, and radar data using the `logger` class. helps maintain a record of operations for debugging and analysis.

- controls `radarpanelcontroller`
  - updates the radar display. sends radar and frog position updates to the `radarpanelcontroller` for visualization.

#### logger
- connected to `pilot`
  - `pilot` calls various methods in `logger` to log waypoints, collision points, and command logs. `logger` logs this information to both csv files and databases.

- uses `csvlogger` and `database`
  - `logger` uses `csvlogger` to write logs to csv files and `database` to insert logs into the database. allows for persistent storage of logs for easier analysis and review.

#### csvlogger
- interacts with `logger`
  - `logger` uses `csvlogger` to record logs in csv format. writes waypoints, collision points, and command logs to csv files for later review.

- stores logs
  - responsible for writing log data to csv files. checks for existing headers, clears files when needed, and appends new log entries.

#### database
- interacts with `logger`
  - `logger` uses `database` to store logs in a structured format. inserts waypoints, collision points, and command logs into the database.

- stores logs
  - manages the creation of tables and data insertion, ensuring logs are stored reliably and can be queried.

#### terminalviewcontroller
- connected to `pilot`
  - uses `pilot` to execute user commands entered in the terminal. sends commands like driving directions, status requests, and radar toggles to `pilot`.

- displays status and radar messages
  - receives status and radar updates from `pilot` and displays them in the terminal interface. toggles display of these messages based on user interaction.

#### mapcontroller
- connected to `pilot`
  - interacts with `pilot` to drive the frog based on user inputs from the map interface. sends driving commands and coordinates to `pilot`.

- updates frog position and radar data
  - receives updates about the frog's position and radar data from `pilot`. updates the visual representation on the map accordingly.

#### radarpanelcontroller
- connected to `pilot`
  - receives updates about the frog's position and radar data from `pilot`. uses this information to update the radar display in the user interface.

#### interactions overview
- command flow
  - user inputs (through `terminalviewcontroller` or `mapcontroller`) -> `pilot` -> `sasacommunicator` -> frog

- feedback flow
  - frog -> `sasacommunicator` -> `pilot` -> (`terminalviewcontroller` / `radarpanelcontroller` / `mapcontroller`)

- logging flow
  - `pilot` -> `logger` -> (`csvlogger` / `database`)

### activity diagrams

#### steps
1. user: the user initiates the process by clicking on a map. the clicked point is processed to the next step.
2. ground control: the coordinates corresponding to the location clicked by the user on the map are received by the ground control system.
3. pilot: the received coordinates are input into a separate method called `drivefrogtocoordinates`. this method talks to the `sasacommunicator`.
4. sasacommunicator: receives the driving commands from the pilot and sends them to the frog.

![Activity Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254549971278233681/afsdfsfdfdsf.png?ex=6679e635&is=667894b5&hm=80fb8aba923a49b9a91cb437113a866bfb7199c1383a928ffd65081117a544f5&)

#### steps
1. user: the user initiates the process by clicking on a direction (forward, backward, left, or right).
2. ground control: the drive command corresponding to the user's click is received by the ground control system.
3. pilot: the ground control system forwards the drive command to the pilot system method.
4. sasacommunicator: receives the drive commands from the pilot and sends them to the frog.

![Activity Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254549988311044257/sdfsdfdsfdfsdfdf.png?ex=6679e639&is=667894b9&hm=6a592483c9471e18986e6a1deb6d41d3ef9ab6644f7401ccf01456088b637324&)

#### steps
1. user: the user clicks on mission control buttons and then proceeds to the pilot directly.
2. ground control: -
3. pilot: receives the button input and starts the `drivefromtocoordinates` method with hardcoded coordinates.
4. sasacommunicator: receives the drive commands from the pilot and sends them to the frog.

![Activity Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254550001351262229/sdfdsfdsfdsfdsfdfd.png?ex=6679e63c&is=667894bc&hm=3b5617a0b5dd69bef5239c2e8fef52b51daa82d37e910de58d35874a7d042361&)

#### steps
1. user: types the command into the terminal window and presses enter.
2. ground control: receives the input and validates it so the pilot can use the final command without confusion.
3. pilot: receives the command and uses its method to contact the `sasacommunicator`.
4. sasacommunicator: receives the drive commands from the pilot and sends them to the frog.

![Activity Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254550013644767384/sdfdsfdsfsdf.png?ex=6679e63f&is=667894bf&hm=4aeebe19e33ff6ae99317129d81acf9bd7f33b5ff903ca7b35c3a5e805ac6d0e&)

#### steps
1. user: presses the save button on the option they want (waypoints, terminal log, collision points).
2. ground control: uploads selected data to the database and provided csv files.

![Activity Diagram](https://cdn.discordapp.com/attachments/1232097711625605171/1254550025334161528/sdfdsffsefse.png?ex=6679e642&is=667894c2&hm=ce44d00af5d1cc6a18497f9f8e762ca17e8a959538971bb18202f2bf2ee8f1ba&)

### wireframes
the wireframes were created to visually plan how the user interface of the applications must look. below is the wireframe for the pilot application. the map and the radar view for the frog are represented. there is also a terminal window for the application. in the actual application, implementation differs: each feature is implemented in a separate window. there is a separate screen for the radar map and the terminal. the radar view was not added as the radar map already covers its functionality. the terminal was included in the ground control application instead of the pilot due to a misunderstanding of the requirement.

![Wireframe](https://cdn.discordapp.com/attachments/1232097711625605171/1254550036746866779/esfsffeferssef.png?ex=6679e645&is=667894c5&hm=306775b9964597b6434838271329d47681e8c0bdef57bfde8cceaeb68e182ed7&)

the ground control wireframe closely resembles the designed application. there is a map with buttons to move and one to complete the mission. in the designed application, there are three mission buttons for each step. the map shows the trace of the frog and the chosen destination point.

### how to extend code
to extend the code of "the frog", a developer needs to understand the difference in functionality between the ground control module and the pilot module. the pilot module is responsible for the algorithmic aspect of the frog. for pathfinding and obstacle avoidance, a developer must work with the pilot module. to get radar data from the frog, refer to the `processedradarpoints` set in the `radarpanelcontroller` class. to create a new class that interacts with the frog, a developer can initialize a new `sasacommunicator` in the new class or use the pilot instance in the new class. the pilot already initializes a `sasacommunicator`, allowing connection to the frog through it.

the ground control module covers mostly gui functionality. it makes the ground control map interactive and adds buttons to it. this module works closely with fxml, so to enhance application windows, a developer needs to initialize visual objects with @fxml and generate an fxml module using scene builder or manually. to control a graphic window, create a controller class linked to the fxml module. to make the application run together with others, add a start script to the main class in the ground control module. when the main class is run, the existing windows with new ones will open.
