A_Star_maze_and_RC_car_implementation
=====================================

This Java project pulls up a UI that generates an NXN grid of nodes (N ranges from 10-100), implements a drawing utility, and can randomly generate mazes using a variation of Prim's algorithm.  A shortest path between start and goal nodes can be calculated using the A* algorithm, and the associated data is sent via serial port to control an RC car using an arduino (assuming a unix-like OS w/ the arduino located at /dev/ttyACM0).  Note: the arduino communication portion of the UI has been commented out to avoid unnecessary echo to the serial port.  To run the app, use the maze.jar file provided.
