# Sprint Planning Document (Sprint 3)

## Sprint Goal
To complete the final implementation and testing of all functionalities, ensuring the project is ready for delivery with comprehensive documentation and demonstrations.

## Selected Product Backlog Items (PBIs)
1. **User Story**: As a user, I want to be able to use pre-programmed drive commands so that I can send a frog on a mission.
   - **Description**: Ground Control. Hard code coordinates for TNT, detonator, and rock that blocks the station. Create buttons on the ground control screen to find TNT, find detonator, and blow up rock that blocks the station.
   - **Issue ID**: 35
   - **Assignee**: Tanya Ivanova

2. **User Story**: As a user, I want to log information in an SQLite database using JDBC.
   - **Description**: Not provided
   - **Issue ID**: 24
   - **Assignee**: Danylo Kurbatov

3. **User Story**: As a user, I want to be able to toggle status and radar functionalities with buttons in the UI.
   - **Description**: Not provided
   - **Issue ID**: 25
   - **Assignee**: Radin Soleymani

4. **User Story**: As a user, I want new operations to finish uncompleted ones, if present, without any conflicts.
   - **Description**: Not provided
   - **Issue ID**: 26
   - **Assignee**: Maksim Sadkov

5. **User Story**: As a user, I want the collision detection to be relatively accurate.
   - **Description**: Not provided
   - **Issue ID**: 27
   - **Assignee**: Anastasiia Khylyk

6. **User Story**: As a user, I want to be able to clear repetitive log messages from the terminal.
   - **Description**: Not provided
   - **Issue ID**: 28
   - **Assignee**: Tanya Ivanova

7. **User Story**: As a user, I want the database to be able to record data even when all of the programs are running at the same time.
   - **Description**: Fix the database issue when several applications are running.
   - **Issue ID**: 29
   - **Assignee**: Danylo Kurbatov

8. **User Story**: As a user, I want to see the location of the frog and radar points, as well as the destination point on the ground control map.
   - **Description**: Ground Control. Add radar points on the ground control map (with photo). Also add the current position of the frog and point selected on the map.
   - **Issue ID**: 30
   - **Assignee**: Radin Soleymani

9. **User Story**: As a user, I want the obstacle avoidance algorithm to have optimized hyperparameters for better accuracy.
   - **Description**: Not provided
   - **Issue ID**: 31
   - **Assignee**: Radin Soleymani

10. **User Story**: As a user, I want the used mission commands to be stored in the database in ground control.
    - **Description**: Not provided
    - **Issue ID**: 32
    - **Assignee**: Danylo Kurbatov

11. **User Story**: As a user, I want the databases to be able to import and export information for future use.
    - **Description**: Not provided
    - **Issue ID**: 33
    - **Assignee**: Danylo Kurbatov

12. **User Story**: As a user, I want a source code that is readable and elegant.
    - **Description**: Not provided
    - **Issue ID**: 34
    - **Assignee**: Anastasiia Khylyk

13. **User Story**: As a user, I want test cases that cover functional requirements.
    - **Description**: Not provided
    - **Issue ID**: 35
    - **Assignee**: Anastasiia Khylyk

14. **User Story**: As a user, I want the documentation to provide insight into the architecture and relevant design decisions.
    - **Description**: Not provided
    - **Issue ID**: 36
    - **Assignee**: Radin Soleymani

15. **User Story**: As a user, I want a video with voiceover which demonstrates all important features.
    - **Description**: Not provided
    - **Issue ID**: 75
    - **Assignee**: Anastasiia Khylyk

16. **User Story**: As a user, I want the documentation to include Retrospective markdown documents for each sprint.
    - **Description**: Not provided
    - **Issue ID**: 38
    - **Assignee**: Radin Soleymani

17. **User Story**: As a user, I want the documentation to include Planning markdown documents for each sprint.
    - **Description**: Not provided
    - **Issue ID**: 39
    - **Assignee**: Radin Soleymani

## Detailed Tasks
1. **Pre-programmed Drive Commands**
   - Hard code coordinates for TNT, detonator, and rock.
   - Create buttons on the ground control screen.
   - Test the functionality.

2. **SQLite Database Logging**
   - Implement JDBC logging.
   - Test logging functionality.

3. **UI Toggle Status and Radar Functionalities**
   - Add toggle buttons to UI.
   - Implement functionality.
   - Test the UI toggles.

4. **Finish Uncompleted Operations**
   - Identify uncompleted operations.
   - Ensure new operations finish previous ones.
   - Test for conflicts.

5. **Accurate Collision Detection**
   - Refine collision detection algorithms.
   - Test for accuracy.

6. **Clear Repetitive Log Messages**
   - Implement functionality to clear logs.
   - Test log clearing feature.

7. **Database Recording During High Load**
   - Ensure database can handle multiple applications.
   - Fix any issues.
   - Test under load.

8. **Display Locations on Ground Control Map**
   - Add radar points and frog location to the map.
   - Test the map display.

9. **Optimize Obstacle Avoidance Algorithm**
   - Fine-tune hyperparameters.
   - Test for accuracy.

10. **Store Mission Commands in Database**
    - Implement storage of commands.
    - Test database entries.

11. **Import/Export Database Information**
    - Add import/export functionality.
    - Test for accuracy.

12. **Readable and Elegant Source Code**
    - Review and refactor code.
    - Ensure readability and elegance.

13. **Functional Requirement Test Cases**
    - Write test cases covering all functional requirements.
    - Execute and validate test cases.

14. **Insightful Documentation**
    - Document architecture and design decisions.
    - Review for completeness.

15. **Feature Demonstration Video**
    - Create video with voiceover.
    - Demonstrate all important features.
    - Review and finalize video.

16. **Retrospective Markdown Documents**
    - Write and review retrospective documents for each sprint.

17. **Planning Markdown Documents**
    - Write and review planning documents for each sprint.

## Capacity Planning
- Radin Soleymani: 40 hours
- Anastasiia Khylyk: 35 hours
- Danylo Kurbatov: 30 hours
- Maksim Sadkov: 25 hours
- Tanya Ivanova: 20 hours

## Sprint Backlog
- User Story 35 (Pre-programmed Drive Commands)
- User Story 24 (SQLite Database Logging)
- User Story 25 (UI Toggle Status and Radar Functionalities)
- User Story 26 (Finish Uncompleted Operations)
- User Story 27 (Accurate Collision Detection)
- User Story 28 (Clear Repetitive Log Messages)
- User Story 29 (Database Recording During High Load)
- User Story 30 (Display Locations on Ground Control Map)
- User Story 31 (Optimize Obstacle Avoidance Algorithm)
- User Story 32 (Store Mission Commands in Database)
- User Story 33 (Import/Export Database Information)
- User Story 34 (Readable and Elegant Source Code)
- User Story 35 (Functional Requirement Test Cases)
- User Story 36 (Insightful Documentation)
- User Story 75 (Feature Demonstration Video)
- User Story 38 (Retrospective Markdown Documents)
- User Story 39 (Planning Markdown Documents)

## Dependencies and Risks
- Coordination required between UI and backend teams for map and database functionalities.
- Risk of delays due to unforeseen issues in algorithm optimization and testing.
- Dependencies on team members' availability and potential overlap of tasks.
