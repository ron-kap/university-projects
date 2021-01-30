# Robotics Group Project
This module assessed both programming ability and team coordination. Utilising both Lego Mindstorms EV3 hardware and the leJOS Java framework, to complete a task in each of the two semesters.

## Semester 1
#### Group size: 3
This semester's task required us to develop a program that enabled the EV3 robot to follow a black line with a white background (PID controller). A (randomly-shaped) obstacle was placed in a random location along the black line, and the robot had to avoid contact with the obstacle and then continue with following the line.

Below is a visual representation of the course.

![Image of the course for Semester 1](media\sem1_task.jpg)

Additional information:
- The robot will start at the red line
- The robot can only move once a curtain in front of it is lifted (timer starts)
- The robot needs to travel around the obstacle and reach the track again accurately (robot must be less than 10 cm away the obstacle)
- The robot will be stopped after it repass the red line by placing the curtain
- The robot should start to follow the line again when the curtain is lifted 
- If the robot hits the obstacle or veers off the black line for more than 5 seconds, then the attempt is failed
- The more laps completed in 5 minutes the higher the grade

[Video demonstration](media\sem1_video.mp4) (one round) for our Semester 1 project [find at, media\sem1_video.mp4]

### Semester 1 Gantt chart
![Semester 1 Gantt chart](media\sem1_gantt.jpg)

## Semester 2
#### Group size: 2
This semester's required us to develop a program that enabled the EV3 robot to navigate through an partially randomised arena environment (Bayesian probability). The robot localises and must distinguish between a handful of possible obstacle combinations and navigate to the next stage.

Below is a visual representation of the arena.

![Image of the arena for Semester 1](media\sem2_task.jpg)

#### Tasks:
1. Localise robot by using Bayesian to determine starting point and obstacle location at [Task 2].
2. Plan the shortest path from start point to parking bay [Task 3], also avoiding the obstacle at [Task 2].
3. Park in bay (no contact with walls), scan the park colour, and manoeuver out of bay. Parking colour determines obstacle location at [Task 4].
4. Plan shortest path from parking bay to end (orange wall), also avoiding the obstacle at [Task 4].

Additional information:
- If the robot hits any wall/obstacle (e.g. parking bay walls), then the attempt is failed
-  55% upon completion of all above tasks, 5% for completion of two rounds in 5 minutes, 30% based on source code.

Due to the limitation of not being able to take the EV3 robot home, our team created a simulated evironment to work on the prject without the physical robot. The Java file is [Semester2VT.java](program\src\Semester2VT.java), VT for virtual testing. A [spreadsheet](media\arena.xlsx) [find at, media\arena.xlsx] was also created to visualise the arena environment.

[Video demonstration](media\sem2_video.mp4) (one round) for our Semester 2 project [find at, media\sem2_video.mp4]

### Semester 2 Gantt chart
![Semester 2 Gantt chart](media\sem2_gantt.jpg)

## Main presentation
[Main presentation](media\presentation.pdf) [find at, media\presentation.pdf]

#### Second-year, Java project
#### Group work (3 people first semester, 2 people second semester)