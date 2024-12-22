# The Frog Project Architecture insight

##  Overview

### System Components

1. **The Frog (Client)**
   - **Functionality:** 
     - Executes movement commands.
     - Sends status and radar updates to The Pilot.
     - Automatically picks up TNT and Detonator and performs the drop-off near obstructions.
   - **Communication:** 
     - Uses `SaSaCommunicator` to communicate with The Pilot.

2. **The Pilot (Client)**
   - **Functionality:** 
     - Receives and processes status updates from The Frog.
     - Sends movement commands to The Frog.
     - Implements pathfinding algorithms to navigate the Frog.
     - Detects and avoids obstacles.
   - **Communication:** 
     - Uses `SaSaCommunicator` to communicate with The Frog and Ground Control.
   - **Control Mechanism:** 
     - Controls the Frog manually and automatically.
     - Interfaces with the radar to detect obstacles.

3. **Ground Control (Client)**
   - **Functionality:** 
     - Monitors mission progress.
     - Provides high-level navigation commands.
     - Displays Frog’s position, radar data, and logs.
   - **Interfaces:** 
     - **Map View:** Shows the map of the asteroid and Frog’s path.
     - **Terminal View:** Allows input of commands and displays logs.
     - **Radar Panel:** Visualizes radar data.

4. **SaSaServer (Server)**
   - **Functionality:** 
     - Acts as a message broker for communication between The Frog, The Pilot, and Ground Control.
     - Ensures reliable message delivery.

### Communication Protocol

- **Message Structure:** Messages consist of an ID and a payload.
  - **ID:** Identifies the sender (e.g., FROG, PILOT, CONTROL).
  - **Payload:** Contains the actual message data.
- **Message Types:** Status updates, radar data, and movement commands.

## Detailed Breakdown of Key Components

### 1. SaSaCommunicator

- **Responsibilities:**
  - Establishes and maintains connections with the SaSaServer.
  - Sends and receives messages.
  - Handles reconnection logic.
- **Design Decisions:**
  - **BlockingQueue:** Uses `BlockingQueue` for message handling to ensure thread safety and efficient message processing.
  - **Thread Management:** Runs in a separate thread to handle continuous message reception without blocking the main application.

### 2. Pilot

- **Responsibilities:**
  - Manages The Frog’s navigation and communication.
  - Processes status updates and radar data.
  - Commands The Frog to move to specified coordinates.
- **Design Decisions:**
  - **Singleton Pattern:** Ensures a single instance for centralized control.
  - **Listeners:** Implements listener interfaces (`StatusListener`, `RadarListener`) to notify other components about updates.
  - **Pathfinding and Obstacle Avoidance:** Implements basic algorithms to navigate the Frog around obstacles.

### 3. RadarPanelController

- **Responsibilities:**
  - Manages the radar visualization.
  - Updates the UI with radar data and Frog’s position.
- **Design Decisions:**
  - **JavaFX Platform.runLater():** Ensures UI updates are performed on the JavaFX Application Thread for thread safety.
  - **Canvas Drawing:** Uses `Canvas` for efficient drawing of radar points and Frog’s position.

### 4. TerminalViewController

- **Responsibilities:**
  - Handles user inputs and commands.
  - Displays status and radar updates.
  - Manages logging of commands.
- **Design Decisions:**
  - **Command Validation:** Ensures commands are valid before sending them to the Pilot.
  - **Thread Safety:** Uses `Platform.runLater()` to safely update the UI from background threads.

### 5. MapController

- **Responsibilities:**
  - Manages the map view and interaction with The Frog.
  - Handles user inputs (button clicks and mouse clicks) to control The Frog.
  - Updates the map with radar points and Frog’s position.
- **Design Decisions:**
  - **Canvas Layers:** Uses multiple `Canvas` elements for drawing different layers (trail, radar points, Frog’s position).
  - **Thread Management:** Runs navigation commands in separate threads to avoid blocking the main application.

### 6. Logger and CSVLogger

- **Responsibilities:**
  - Logs mission data (waypoints, collision points, command logs) to CSV files and SQLite database.
  - Supports exporting and importing data.
- **Design Decisions:**
  - **CSV Logging:** Uses OpenCSV library for efficient CSV operations.
  - **Database Logging:** Uses SQLite for persistent storage of mission data.

## Diagrams

```plaintext
+--------------------+       +------------------+       +----------------------+
| SaSaCommunicator   |<----->|       Pilot      |<----->|  RadarPanelController|
+--------------------+       +------------------+       +----------------------+
           ^                       ^                           ^
           |                       |                           |
           v                       v                           v
+----------------------+   +---------------------+   +----------------------+
| TerminalViewController|  |    MapController    |   |      Logger          |
+----------------------+   +---------------------+   +----------------------+
                                 |                           |
                                 v                           v
                           +--------------------+     +--------------------+
                           |     CSVLogger      |     |     Database       |
                           +--------------------+     +--------------------+


 User         TerminalViewController         Pilot         SaSaCommunicator       Frog
  |                     |                      |                  |                  |
  |----> Enters Command |                      |                  |                  |
  |                     |----> Validate Command|                  |                  |
  |                     |                      |                  |                  |
  |                     |----> Send Command----|----> Send Message|----> Execute Command
  |                     |                      |                  |                  |
  |                     |<---- Acknowledge ----|<---- Acknowledge |                  |
  |<---- Display Result |                      |                  |                  |
