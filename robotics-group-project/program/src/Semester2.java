import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.hardware.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;

/*
	NAMING CONVENTION::
		- Classes		--> UpperCamelCase
		- Methods		--> lowerCamelCase
		- Variables		--> lowerCamelCase

	NOTES::
		"_-_TUNE_-_" --> Specified variable/value must be tested and tuned using sensors before deployment in new environment.
		"PRINT PROTECTION" --> Print to EV3's LCD, this ensures proper/exact motor movements.
		
		0 = north
		1 = north-east
		2 = east
		3 = south-east
		4 = south
		5 = south-west
		6 = west
		7 = north-west

	TO-DO::
		N/A
*/

public class Semester2 {
	
	// Declare Vars - Sensors 
		// Color
	private static EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);
	private static SensorMode colorMode = colorSensor.getColorIDMode();	
	private static float[] colorSample = new float[colorMode.sampleSize()];
		// Gyro
	private static EV3GyroSensor gyroSensor;
	private static SampleProvider gyroMode;
	private static float[] gyroSample; 
		// Touch
	private static EV3TouchSensor touchSensor = new EV3TouchSensor(SensorPort.S2);
	private static SampleProvider touchMode = touchSensor.getTouchMode();
	private static float[] touchSample = new float[touchMode.sampleSize()];
		// Motors
	private static RegulatedMotor mLeft = new EV3LargeRegulatedMotor(MotorPort.B);
	private static RegulatedMotor mRight = new EV3LargeRegulatedMotor(MotorPort.A);
		// Misc
	private static GraphicsLCD g = BrickFinder.getDefault().getGraphicsLCD();
	
	
	// Declare Vars - Path Planning
	private static ArrayList<Pair> open;											// Available nodes
	private static ArrayList<Pair> closed;											// Unavailable nodes
	private static ArrayList<Pair> obstacles;										// Obstacle nodes
	
	private static ArrayList<ArrayList<Pair>> points; 								// Double ArrayList of points (gird)
	
	private static HashMap<Pair, Pair> parents;					 					// Map storage for parents, [pair -> parent pair] -- pair = key, parent pair = value
	
	private static int limit = 30 - 1;												// Size of grid, using array counting
		
	private static int colorDetect = 0;												// 0 == undefined, 1 == red, 2 == green
	private static boolean obA = true;
	
	//private static float angle = 0;
	private static float currentTargetAngle;
	private static int totalStraightMoves = 0;
	private static int totalDiagonalMoves = 0;
		
	// Declare Vars - Bayesian
	private static int totalBayesianMoves = 0;

		// Main Bayesian logic for probability
	public static int bayesian()
	{
		// Constants - Vars
		int numPositions = 37; 													// Number of total positions

		ArrayList<Double> prob = new ArrayList<Double>();						// Probabilities List
		double pStart = (1 / (double)numPositions); 							// Initial probability
		double reqProb = 0.5; 													// Target/threshold
		
		double sT = 0.95; 														// Sensor true
		double sF = 0.05; 														// Sensor false
		
		int currentPosition = 999; 												// Placeholder current
		int index = 999;														// Placeholder current
		
		
		// Set initial probabilities
		for (int i = 0; i < numPositions; i++) {
			prob.add(i, pStart);
		}
		
		
		// Grid reference, all blue values
		ArrayList<Integer> blueVals = new ArrayList<Integer>();
		blueVals.addAll(Arrays.asList(2, 3, 4, 6, 7, 10, 11, 12, 14, 15, 18, 19, 20, 23, 24, 25, 27, 28, 31, 32, 33, 35, 36));

		
		// Bayesian loop
		boolean posFound = false;
		
		for (int j = 0; posFound == false && j < numPositions; j++)
		{
			System.out.println("Bayesian Started");

			// Get sensor data
			colorMode.fetchSample(colorSample, 0);
			
			float val = colorSample[0];
			
			for (int i = 0; i < numPositions; i++)
			{
				if (val == 2.0) 								// sensed blue
				{
					if (blueVals.contains(i)) 					// sensed blue and current position is known to be blue
					{
						prob.set(i, prob.get(i)*sT);
					}
					else 										// sensed blue and position is known to be white (not blue)
					{
						prob.set(i, prob.get(i)*sF);
					}
				}
				else if (val == 6.0) 							// sensed white
				{
					if (blueVals.contains(i))					// sensed white and position is known to be blue (not white)
					{
						prob.set(i, prob.get(i)*sF);
					}
					else 										// sensed white and position is known to be white
					{
						prob.set(i, prob.get(i)*sT);
					}
				}
				else {}											// cannot determine if sensed blue or white
			}
						
			
			// Normalise probabilities
			double sum = 0;
			for (int i = 0; i < numPositions; i++)
			{
				sum += prob.get(i);
			}

			double k = 1 / sum;
			for (int i = 0; i < numPositions; i++)
			{
				prob.set(i, prob.get(i)*k);
			}


			// Target/threshold check
			double hProb = Collections.max(prob);
			if (hProb >= reqProb)
			{
				// Location found, end of localisation
				currentPosition = prob.indexOf(hProb);
				index = j;
				posFound = true;

				System.out.println("Bayesian Terminated");
				System.out.println(currentPosition);
				
				return currentPosition;
			}
			else
			{
				// Location not found, therefore move and collect more sensor data
				bayesianMove();
				
				ArrayList<Double> newProb = new ArrayList<Double>();

				for (int i = 0; i < numPositions; i++)
				{	
					if (i == 0)
					{
						newProb.add(i, prob.get(i)*0.05);
					}
					else if (i == (numPositions - 1))
					{
						newProb.add(i, prob.get(i)*0.05+prob.get(i-1)*0.95);
					}
					else
					{
						newProb.add(i, prob.get(i)*0.05+prob.get(i-1)*0.95);
					}
				}
				
				prob = newProb;
			}
		}
			return currentPosition;
	}
	
	
		// Path-planning logic, hard-coded arena path dependent on initial color, return a list of coordinates
	public static ArrayList<Pair> pathPlan(int startX, int startY, int targetX, int targetY)
	{
		// Fill/Reset ArrayLists
		open = new ArrayList<Pair>();
		closed = new ArrayList<Pair>();
		obstacles = new ArrayList<Pair>();
		parents = new HashMap<>();
		
		System.out.println("Enter Path Plan");
		
		fillWalls();
						
		if (colorDetect == 1)
		{
			// Red
			fillWall4();
			fillObstacleC();
			fillObstacleD();
			
			// Block left side
			fillObstacleA();
			fillWall1();
			fillObstacleB();
			fillWall2();
		}
		else if (colorDetect == 2)
		{
			// Green
			fillObstacleC();
			fillWall5();
			fillObstacleD();
			
			// Block left sides
			fillObstacleA();
			fillWall1();
			fillObstacleB();
			fillWall2();
		}
		else if (colorDetect == 0)
		{
			if (obA)
			{
				fillObstacleA();
				fillObstacleB();
				fillWall2();
			}
			else
			{
				fillObstacleA();
				fillWall1();
				fillObstacleB();
			}
		}
		
		// Set Start and Target points
		Pair target = points.get(targetX).get(targetY);
		Pair start = points.get(startX).get(startY);
		
		Pair current = start;
		open.add(current);															// Add current to open list
		
		// Currently path list and closed list are the same, needs to be changed with introduction of obstacles
		ArrayList<Pair> path = new ArrayList<Pair>();
		
		
		boolean target_found = false;
		while (!target_found)
		{
			ArrayList<Pair> n = neighbours(current);								// Get neighbours
			
			open.addAll(n);															// Add neighbours to open list
			
			for (int i = 0; i < n.size() && target_found == false; i++)
			{
				// Check if target is in neighbours
				if (n.get(i).x == target.x && n.get(i).y == target.y)
				{
					current = target;
					target_found = true;
				}
			}
			
			if (!target_found)
			{
				// Haven't found target yet
				int lowestCost = 999999;
				int lowestIndex = 0;
				
				open.remove(current);

				// Find lowest cost waypoint
				for (int i = 0; i < open.size(); i++)
				{
					int cost = Math.abs(target.x - open.get(i).x) + Math.abs(target.y - open.get(i).y);

					if (cost < lowestCost)
					{
						// Priority = top > right  > bottom > left
						lowestCost = cost;
						lowestIndex = i;
					}
				}
				// Add current point to closed list 
				closed.add(current);
							
				// Move to lowest cost waypoint
				current = open.get(lowestIndex);
			}
		}

		// Program has reached the end, will now construct a path from start to target
		while (current != start)
		{			
			path.add(current);											// Add current point to path
			
			current = parents.get(current);								// Get the parent of current, for next iteration
		}
		
		// while stops before start, so we must add it
		path.add(start);
		Collections.reverse(path);
		
		return path;
	}

	
		// Discover neighbours of specified coordinate
	public static ArrayList<Pair> neighbours(Pair p)
	{
		ArrayList<Pair> arr = new ArrayList<Pair>();

		// top
		if (!((p.y + 1) > limit))
		{
			if (!(obstacles.contains(points.get(p.x).get(p.y+1))) && !(closed.contains(points.get(p.x).get(p.y+1))) && !(open.contains(points.get(p.x).get(p.y+1))))
			{
				arr.add(points.get(p.x).get((p.y + 1)));
				parents.put(points.get(p.x).get(p.y+1), p);
			}
		}
		
		// top-right
		if (!((p.y + 1) > limit) && !((p.x + 1) > limit))
		{
			if (!(obstacles.contains(points.get(p.x+1).get(p.y+1))) && !(closed.contains(points.get(p.x+1).get(p.y+1))) && !(open.contains(points.get(p.x+1).get(p.y+1))))
			{
				arr.add(points.get(p.x+1).get(p.y+1));
				parents.put(points.get(p.x+1).get(p.y+1), p);
			}
		}

		// right
		if (!((p.x + 1) > limit))
		{
			if (!(obstacles.contains(points.get(p.x+1).get(p.y))) && !(closed.contains(points.get(p.x+1).get(p.y))) && !(open.contains(points.get(p.x+1).get(p.y))))
			{
				arr.add(points.get(p.x+1).get(p.y));
				parents.put(points.get(p.x+1).get(p.y), p);
			}
		}
		
		// bottom-right
		if (!((p.y - 1) < 0) && !((p.x + 1) > limit))
		{
			if (!(obstacles.contains(points.get(p.x+1).get(p.y-1))) && !(closed.contains(points.get(p.x+1).get(p.y-1))) && !(open.contains(points.get(p.x+1).get(p.y-1))))
			{
				arr.add(points.get(p.x+1).get(p.y-1));
				parents.put(points.get(p.x+1).get(p.y-1), p);
			}
		}
		 
		// bottom
		if (!((p.y - 1) < 0))
		{
			if (!(obstacles.contains(points.get(p.x).get(p.y - 1))) && !(closed.contains(points.get(p.x).get(p.y - 1))) && !(open.contains(points.get(p.x).get(p.y - 1))))
			{
				arr.add(points.get(p.x).get(p.y - 1));
				parents.put(points.get(p.x).get(p.y-1), p);
			}
		}

		// bottom-left
		if (!((p.y - 1) < 0) && !((p.x - 1) < 0))
		{
			if (!(obstacles.contains(points.get(p.x-1).get(p.y-1))) && !(closed.contains(points.get(p.x-1).get(p.y-1))) && !(open.contains(points.get(p.x-1).get(p.y-1))))
			{
				arr.add(points.get(p.x-1).get(p.y-1));
				parents.put(points.get(p.x-1).get(p.y-1), p);
			}
		}

		// left
		if (!((p.x - 1) < 0))
		{
			if (!(obstacles.contains(points.get(p.x-1).get(p.y))) && !(closed.contains(points.get(p.x-1).get(p.y))) && !(open.contains(points.get(p.x-1).get(p.y))))
			{
				arr.add(points.get(p.x-1).get(p.y));
				parents.put(points.get(p.x-1).get(p.y), p);
			}
		}

		// top-left
		if (!((p.y + 1) > limit) && !((p.x - 1) < 0))
		{
			if (!(obstacles.contains(points.get(p.x-1).get(p.y+1))) && !(closed.contains(points.get(p.x-1).get(p.y+1))) && !(open.contains(points.get(p.x-1).get(p.y+1))))
			{
				arr.add(points.get(p.x-1).get(p.y+1));
				parents.put(points.get(p.x-1).get(p.y+1), p);
			}
		}

		return arr;
	}

	
		// Following fill methods used to remove invalid coordinates, dependent on arena setup from initial color
	public static void fillWalls()
	{
		// Bottom corner
		obstacles.add(points.get(0).get(7));
		closed.add(points.get(0).get(7));
		obstacles.add(points.get(0).get(6));
		closed.add(points.get(0).get(6));
		obstacles.add(points.get(0).get(5));
		closed.add(points.get(0).get(5));
		obstacles.add(points.get(0).get(4));
		closed.add(points.get(0).get(4));
		obstacles.add(points.get(0).get(3));
		closed.add(points.get(0).get(3));
		obstacles.add(points.get(0).get(2));
		closed.add(points.get(0).get(2));
		obstacles.add(points.get(0).get(1));
		closed.add(points.get(0).get(1));
		obstacles.add(points.get(0).get(0));
		closed.add(points.get(0).get(0));
		obstacles.add(points.get(1).get(6));
		closed.add(points.get(1).get(6));
		obstacles.add(points.get(1).get(5));
		closed.add(points.get(1).get(5));
		obstacles.add(points.get(1).get(4));
		closed.add(points.get(1).get(4));
		obstacles.add(points.get(1).get(3));
		closed.add(points.get(1).get(3));
		obstacles.add(points.get(1).get(2));
		closed.add(points.get(1).get(2));
		obstacles.add(points.get(1).get(1));
		closed.add(points.get(1).get(1));
		obstacles.add(points.get(1).get(0));
		closed.add(points.get(1).get(0));
		obstacles.add(points.get(2).get(5));
		closed.add(points.get(2).get(5));
		obstacles.add(points.get(2).get(4));
		closed.add(points.get(2).get(4));
		obstacles.add(points.get(2).get(3));
		closed.add(points.get(2).get(3));
		obstacles.add(points.get(2).get(2));
		closed.add(points.get(2).get(2));
		obstacles.add(points.get(2).get(1));
		closed.add(points.get(2).get(1));
		obstacles.add(points.get(2).get(0));
		closed.add(points.get(2).get(0));
		obstacles.add(points.get(3).get(4));
		closed.add(points.get(3).get(4));
		obstacles.add(points.get(3).get(3));
		closed.add(points.get(3).get(3));
		obstacles.add(points.get(3).get(2));
		closed.add(points.get(3).get(2));
		obstacles.add(points.get(3).get(1));
		closed.add(points.get(3).get(1));
		obstacles.add(points.get(3).get(0));
		closed.add(points.get(3).get(0));
		obstacles.add(points.get(4).get(3));
		closed.add(points.get(4).get(3));
		obstacles.add(points.get(4).get(2));
		closed.add(points.get(4).get(2));
		obstacles.add(points.get(4).get(1));
		closed.add(points.get(4).get(1));
		obstacles.add(points.get(4).get(0));
		closed.add(points.get(4).get(0));
		obstacles.add(points.get(5).get(2));
		closed.add(points.get(5).get(2));
		obstacles.add(points.get(5).get(1));
		closed.add(points.get(5).get(1));
		obstacles.add(points.get(5).get(0));
		closed.add(points.get(5).get(0));
		obstacles.add(points.get(6).get(1));
		closed.add(points.get(6).get(1));
		obstacles.add(points.get(6).get(0));
		closed.add(points.get(6).get(0));
		obstacles.add(points.get(7).get(0));
		closed.add(points.get(7).get(0));
				
		// Top corner
		obstacles.add(points.get(22).get(29));
		closed.add(points.get(22).get(29));
		obstacles.add(points.get(23).get(29));
		closed.add(points.get(23).get(29));
		obstacles.add(points.get(23).get(28));
		closed.add(points.get(23).get(28));
		obstacles.add(points.get(24).get(28));
		closed.add(points.get(24).get(28));
		obstacles.add(points.get(24).get(27));
		closed.add(points.get(24).get(27));
		obstacles.add(points.get(25).get(27));
		closed.add(points.get(25).get(27));
		obstacles.add(points.get(25).get(26));
		closed.add(points.get(25).get(26));
		obstacles.add(points.get(26).get(26));
		closed.add(points.get(26).get(26));
		obstacles.add(points.get(26).get(25));
		closed.add(points.get(26).get(25));
		obstacles.add(points.get(27).get(25));
		closed.add(points.get(27).get(25));
		obstacles.add(points.get(27).get(24));
		closed.add(points.get(27).get(24));
		obstacles.add(points.get(28).get(24));
		closed.add(points.get(28).get(24));
		obstacles.add(points.get(28).get(23));
		closed.add(points.get(28).get(23));
		obstacles.add(points.get(29).get(23));
		closed.add(points.get(29).get(23));
		obstacles.add(points.get(29).get(22));
		closed.add(points.get(29).get(22));
		
			// 3 (middle wall)
		obstacles.add(points.get(11).get(19));
		closed.add(points.get(11).get(19));
		obstacles.add(points.get(10).get(18));
		closed.add(points.get(10).get(18));
		obstacles.add(points.get(11).get(18));
		closed.add(points.get(11).get(18));
		obstacles.add(points.get(12).get(18));
		closed.add(points.get(12).get(18));
		obstacles.add(points.get(11).get(17));
		closed.add(points.get(11).get(17));
		obstacles.add(points.get(12).get(17));
		closed.add(points.get(12).get(17));
		obstacles.add(points.get(13).get(17));
		closed.add(points.get(13).get(17));
		obstacles.add(points.get(12).get(16));
		closed.add(points.get(12).get(16));
		obstacles.add(points.get(13).get(16));
		closed.add(points.get(13).get(16));
		obstacles.add(points.get(14).get(16));
		closed.add(points.get(14).get(16));
		obstacles.add(points.get(13).get(15));
		closed.add(points.get(13).get(15));
		obstacles.add(points.get(14).get(14));
		closed.add(points.get(14).get(14));
		obstacles.add(points.get(14).get(15));
		closed.add(points.get(14).get(15));
		obstacles.add(points.get(15).get(15));
		closed.add(points.get(15).get(15));
		obstacles.add(points.get(15).get(14));
		closed.add(points.get(15).get(14));
		obstacles.add(points.get(16).get(14));
		closed.add(points.get(16).get(14));
		obstacles.add(points.get(15).get(13));
		closed.add(points.get(15).get(13));
		obstacles.add(points.get(16).get(13));
		closed.add(points.get(16).get(13));
		obstacles.add(points.get(17).get(13));
		closed.add(points.get(17).get(13));
		obstacles.add(points.get(16).get(12));
		closed.add(points.get(16).get(12));
		obstacles.add(points.get(17).get(12));
		closed.add(points.get(17).get(12));
		obstacles.add(points.get(18).get(12));
		closed.add(points.get(18).get(12));
		obstacles.add(points.get(17).get(11));
		closed.add(points.get(17).get(11));
		obstacles.add(points.get(18).get(11));
		closed.add(points.get(18).get(11));
		obstacles.add(points.get(19).get(11));
		closed.add(points.get(19).get(11));
		obstacles.add(points.get(18).get(10));
		closed.add(points.get(18).get(10));
				// Extra
		obstacles.add(points.get(10).get(17));
		closed.add(points.get(10).get(17));
		obstacles.add(points.get(11).get(16));
		closed.add(points.get(11).get(16));
		obstacles.add(points.get(12).get(15));
		closed.add(points.get(12).get(15));
		obstacles.add(points.get(13).get(14));
		closed.add(points.get(13).get(14));
		obstacles.add(points.get(14).get(13));
		closed.add(points.get(14).get(13));
		obstacles.add(points.get(15).get(12));
		closed.add(points.get(15).get(12));
		obstacles.add(points.get(16).get(11));
		closed.add(points.get(16).get(11));
		obstacles.add(points.get(17).get(10));
		closed.add(points.get(17).get(10));
		obstacles.add(points.get(12).get(19));
		closed.add(points.get(12).get(19));
		obstacles.add(points.get(13).get(18));
		closed.add(points.get(13).get(18));
		obstacles.add(points.get(14).get(17));
		closed.add(points.get(14).get(17));
		obstacles.add(points.get(15).get(16));
		closed.add(points.get(15).get(16));
		obstacles.add(points.get(16).get(15));
		closed.add(points.get(16).get(15));
		obstacles.add(points.get(17).get(14));
		closed.add(points.get(17).get(14));
		obstacles.add(points.get(18).get(13));
		closed.add(points.get(18).get(13));
		obstacles.add(points.get(19).get(12));
		closed.add(points.get(19).get(12));
		
			// Parking Wall
		obstacles.add(points.get(17).get(29));
		closed.add(points.get(17).get(29));
		obstacles.add(points.get(18).get(29));
		closed.add(points.get(18).get(29));
		obstacles.add(points.get(19).get(29));
		closed.add(points.get(19).get(29));
		obstacles.add(points.get(20).get(29));
		closed.add(points.get(20).get(29));
		obstacles.add(points.get(21).get(29));
		closed.add(points.get(21).get(29));
		obstacles.add(points.get(17).get(28));
		closed.add(points.get(17).get(28));
		obstacles.add(points.get(18).get(28));
		closed.add(points.get(18).get(28));
		obstacles.add(points.get(19).get(28));
		closed.add(points.get(19).get(28));
		obstacles.add(points.get(20).get(28));
		closed.add(points.get(20).get(28));
		obstacles.add(points.get(21).get(28));
		closed.add(points.get(21).get(28));
		obstacles.add(points.get(21).get(27));
		closed.add(points.get(21).get(27));
		obstacles.add(points.get(21).get(26));
		closed.add(points.get(21).get(26));
		obstacles.add(points.get(21).get(25));
		closed.add(points.get(21).get(25));
		obstacles.add(points.get(21).get(24));
		closed.add(points.get(21).get(24));
		obstacles.add(points.get(17).get(25));
		closed.add(points.get(17).get(25));
		obstacles.add(points.get(18).get(25));
		closed.add(points.get(18).get(25));
		obstacles.add(points.get(19).get(25));
		closed.add(points.get(19).get(25));
		obstacles.add(points.get(20).get(25));
		closed.add(points.get(20).get(25));
		obstacles.add(points.get(17).get(24));
		closed.add(points.get(17).get(24));
		obstacles.add(points.get(18).get(24));
		closed.add(points.get(18).get(24));
		obstacles.add(points.get(19).get(24));
		closed.add(points.get(19).get(24));
		obstacles.add(points.get(20).get(24));
		closed.add(points.get(20).get(24));
		
		obstacles.add(points.get(16).get(26));
		closed.add(points.get(16).get(26));
		obstacles.add(points.get(16).get(25));
		closed.add(points.get(16).get(25));
		obstacles.add(points.get(16).get(24));
		closed.add(points.get(16).get(24));
		
		
			// Left Wall
		obstacles.add(points.get(1).get(7));
		closed.add(points.get(1).get(7));
		obstacles.add(points.get(1).get(8));
		closed.add(points.get(1).get(8));
		obstacles.add(points.get(1).get(9));
		closed.add(points.get(1).get(9));
		obstacles.add(points.get(1).get(10));
		closed.add(points.get(1).get(10));
		obstacles.add(points.get(1).get(11));
		closed.add(points.get(1).get(11));
		obstacles.add(points.get(1).get(12));
		closed.add(points.get(1).get(12));
		obstacles.add(points.get(1).get(13));
		closed.add(points.get(1).get(13));
		obstacles.add(points.get(1).get(14));
		closed.add(points.get(1).get(14));
		obstacles.add(points.get(1).get(15));
		closed.add(points.get(1).get(15));
		obstacles.add(points.get(1).get(16));
		closed.add(points.get(1).get(16));
		obstacles.add(points.get(1).get(17));
		closed.add(points.get(1).get(17));
		obstacles.add(points.get(1).get(18));
		closed.add(points.get(1).get(18));
		obstacles.add(points.get(1).get(19));
		closed.add(points.get(1).get(19));
		obstacles.add(points.get(1).get(20));
		closed.add(points.get(1).get(20));
		obstacles.add(points.get(1).get(21));
		closed.add(points.get(1).get(21));
		obstacles.add(points.get(1).get(22));
		closed.add(points.get(1).get(22));
		obstacles.add(points.get(1).get(23));
		closed.add(points.get(1).get(23));
		obstacles.add(points.get(1).get(24));
		closed.add(points.get(1).get(24));
		
		
			// Top Wall
		obstacles.add(points.get(5).get(28));
		closed.add(points.get(5).get(28));
		obstacles.add(points.get(6).get(28));
		closed.add(points.get(6).get(28));
		obstacles.add(points.get(7).get(28));
		closed.add(points.get(7).get(28));
		obstacles.add(points.get(8).get(28));
		closed.add(points.get(8).get(28));
		obstacles.add(points.get(9).get(28));
		closed.add(points.get(9).get(28));
		obstacles.add(points.get(10).get(28));
		closed.add(points.get(10).get(28));
		obstacles.add(points.get(11).get(28));
		closed.add(points.get(11).get(28));
		obstacles.add(points.get(12).get(28));
		closed.add(points.get(12).get(28));
		obstacles.add(points.get(13).get(28));
		closed.add(points.get(13).get(28));
		obstacles.add(points.get(14).get(28));
		closed.add(points.get(14).get(28));
		obstacles.add(points.get(15).get(28));
		closed.add(points.get(15).get(28));
		obstacles.add(points.get(16).get(28));
		closed.add(points.get(16).get(28));
		
		
			// Right Wall
		obstacles.add(points.get(28).get(5));
		closed.add(points.get(28).get(5));
		obstacles.add(points.get(28).get(6));
		closed.add(points.get(28).get(6));
		obstacles.add(points.get(28).get(7));
		closed.add(points.get(28).get(7));
		obstacles.add(points.get(28).get(8));
		closed.add(points.get(28).get(8));
		obstacles.add(points.get(28).get(9));
		closed.add(points.get(28).get(9));
		obstacles.add(points.get(28).get(10));
		closed.add(points.get(28).get(10));
		obstacles.add(points.get(28).get(11));
		closed.add(points.get(28).get(11));
		obstacles.add(points.get(28).get(12));
		closed.add(points.get(28).get(12));
		obstacles.add(points.get(28).get(13));
		closed.add(points.get(28).get(13));
		obstacles.add(points.get(28).get(14));
		closed.add(points.get(28).get(14));
		obstacles.add(points.get(28).get(15));
		closed.add(points.get(28).get(15));
		obstacles.add(points.get(28).get(16));
		closed.add(points.get(28).get(16));
		obstacles.add(points.get(28).get(17));
		closed.add(points.get(28).get(17));
		obstacles.add(points.get(28).get(18));
		closed.add(points.get(28).get(18));
		obstacles.add(points.get(28).get(19));
		closed.add(points.get(28).get(19));
		obstacles.add(points.get(28).get(20));
		closed.add(points.get(28).get(20));
		obstacles.add(points.get(28).get(21));
		closed.add(points.get(28).get(21));
		obstacles.add(points.get(28).get(22));
		closed.add(points.get(28).get(22));
				
			// Bottom Wall
		obstacles.add(points.get(24).get(3));
		closed.add(points.get(24).get(3));
		obstacles.add(points.get(23).get(3));
		closed.add(points.get(23).get(3));
		obstacles.add(points.get(22).get(3));
		closed.add(points.get(22).get(3));
		obstacles.add(points.get(21).get(3));
		closed.add(points.get(21).get(3));
		obstacles.add(points.get(20).get(3));
		closed.add(points.get(20).get(3));
		
		obstacles.add(points.get(21).get(2));
		closed.add(points.get(21).get(2));
		obstacles.add(points.get(20).get(2));
		closed.add(points.get(20).get(2));
		obstacles.add(points.get(19).get(2));
		closed.add(points.get(19).get(2));
		obstacles.add(points.get(18).get(2));
		closed.add(points.get(18).get(2));
		
		obstacles.add(points.get(18).get(3));
		closed.add(points.get(18).get(3));
		obstacles.add(points.get(17).get(3));
		closed.add(points.get(17).get(3));
		obstacles.add(points.get(16).get(3));
		closed.add(points.get(16).get(3));
		obstacles.add(points.get(15).get(3));
		closed.add(points.get(15).get(3));
		obstacles.add(points.get(14).get(3));
		closed.add(points.get(14).get(3));
		obstacles.add(points.get(13).get(3));
		closed.add(points.get(13).get(3));
		obstacles.add(points.get(12).get(3));
		closed.add(points.get(12).get(3));
		obstacles.add(points.get(11).get(3));
		closed.add(points.get(11).get(3));
		obstacles.add(points.get(10).get(3));
		closed.add(points.get(10).get(3));
		obstacles.add(points.get(9).get(3));
		closed.add(points.get(9).get(3));
		obstacles.add(points.get(8).get(3));
		closed.add(points.get(8).get(3));
		obstacles.add(points.get(7).get(3));
		closed.add(points.get(7).get(3));
		obstacles.add(points.get(6).get(2));
		closed.add(points.get(6).get(2));
		obstacles.add(points.get(7).get(2));
		closed.add(points.get(7).get(2));
	}
		
	public static void fillObstacleA()
	{
			// A
		obstacles.add(points.get(0).get(29));
		closed.add(points.get(0).get(29));
		obstacles.add(points.get(1).get(29));
		closed.add(points.get(1).get(29));
		obstacles.add(points.get(2).get(29));
		closed.add(points.get(2).get(29));
		obstacles.add(points.get(3).get(29));
		closed.add(points.get(3).get(29));
		obstacles.add(points.get(4).get(29));
		closed.add(points.get(4).get(29));
		obstacles.add(points.get(0).get(28));
		closed.add(points.get(0).get(28));
		obstacles.add(points.get(1).get(28));
		closed.add(points.get(1).get(28));
		obstacles.add(points.get(2).get(28));
		closed.add(points.get(2).get(28));
		obstacles.add(points.get(3).get(28));
		closed.add(points.get(3).get(28));
		obstacles.add(points.get(4).get(28));
		closed.add(points.get(4).get(28));
		obstacles.add(points.get(0).get(27));
		closed.add(points.get(0).get(27));
		obstacles.add(points.get(1).get(27));
		closed.add(points.get(1).get(27));
		obstacles.add(points.get(4).get(27));
		closed.add(points.get(4).get(27));
		obstacles.add(points.get(0).get(26));
		closed.add(points.get(0).get(26));
		obstacles.add(points.get(1).get(26));
		closed.add(points.get(1).get(26));
		obstacles.add(points.get(4).get(26));
		closed.add(points.get(4).get(26));
		obstacles.add(points.get(0).get(29));
		closed.add(points.get(0).get(25));
		obstacles.add(points.get(1).get(25));
		closed.add(points.get(1).get(25));
		obstacles.add(points.get(2).get(25));
		closed.add(points.get(2).get(25));
		obstacles.add(points.get(3).get(25));
		closed.add(points.get(3).get(25));
		obstacles.add(points.get(4).get(25));
		closed.add(points.get(4).get(25));
		
		obstacles.add(points.get(2).get(27));
		closed.add(points.get(2).get(27));
		obstacles.add(points.get(3).get(27));
		closed.add(points.get(3).get(27));
		obstacles.add(points.get(2).get(26));
		closed.add(points.get(2).get(26));
		obstacles.add(points.get(3).get(26));
		closed.add(points.get(3).get(26));
	}
	
	public static void fillObstacleB()
	{
			// B
		obstacles.add(points.get(7).get(23));
		closed.add(points.get(7).get(23));
		obstacles.add(points.get(8).get(23));
		closed.add(points.get(8).get(23));
		obstacles.add(points.get(9).get(23));
		closed.add(points.get(9).get(23));
		obstacles.add(points.get(6).get(22));
		closed.add(points.get(6).get(22));
		obstacles.add(points.get(9).get(22));
		closed.add(points.get(9).get(22));
		obstacles.add(points.get(6).get(21));
		closed.add(points.get(6).get(21));
		obstacles.add(points.get(9).get(21));
		closed.add(points.get(9).get(21));
		obstacles.add(points.get(6).get(20));
		closed.add(points.get(6).get(20));
		obstacles.add(points.get(7).get(20));
		closed.add(points.get(7).get(20));
		obstacles.add(points.get(8).get(20));
		closed.add(points.get(8).get(20));
		obstacles.add(points.get(9).get(20));
		closed.add(points.get(9).get(20));
		
		obstacles.add(points.get(7).get(22));
		closed.add(points.get(7).get(22));
		obstacles.add(points.get(8).get(22));
		closed.add(points.get(8).get(22));
		obstacles.add(points.get(7).get(21));
		closed.add(points.get(7).get(21));
		obstacles.add(points.get(8).get(21));
		closed.add(points.get(8).get(21));
		
		// Extra
		obstacles.add(points.get(10).get(23));
		closed.add(points.get(10).get(23));
		obstacles.add(points.get(10).get(22));
		closed.add(points.get(10).get(22));
		obstacles.add(points.get(10).get(21));
		closed.add(points.get(10).get(21));
	}
		
	public static void fillObstacleC()
	{
			// C
		obstacles.add(points.get(20).get(9));
		closed.add(points.get(20).get(9));
		obstacles.add(points.get(21).get(9));
		closed.add(points.get(21).get(9));
		obstacles.add(points.get(22).get(9));
		closed.add(points.get(22).get(9));
		obstacles.add(points.get(23).get(9));
		closed.add(points.get(23).get(9));
		obstacles.add(points.get(20).get(8));
		closed.add(points.get(20).get(8));
		obstacles.add(points.get(23).get(8));
		closed.add(points.get(23).get(8));
		obstacles.add(points.get(20).get(7));
		closed.add(points.get(20).get(7));
		obstacles.add(points.get(20).get(6));
		closed.add(points.get(20).get(6));
		obstacles.add(points.get(21).get(6));
		closed.add(points.get(21).get(6));
		
		obstacles.add(points.get(21).get(8));
		closed.add(points.get(21).get(8));
		obstacles.add(points.get(22).get(8));
		closed.add(points.get(22).get(8));
		obstacles.add(points.get(21).get(7));
		closed.add(points.get(21).get(7));
		obstacles.add(points.get(22).get(7));
		closed.add(points.get(22).get(7));
		
		
		// Extra
		
		obstacles.add(points.get(20).get(10));
		closed.add(points.get(20).get(10));
		obstacles.add(points.get(19).get(9));
		closed.add(points.get(19).get(9));
	}	
	
	public static void fillObstacleD()
	{
			// D
		obstacles.add(points.get(25).get(4));
		closed.add(points.get(25).get(4));
		obstacles.add(points.get(26).get(4));
		closed.add(points.get(26).get(4));
		obstacles.add(points.get(27).get(4));
		closed.add(points.get(27).get(4));
		obstacles.add(points.get(28).get(4));
		closed.add(points.get(28).get(4));
		obstacles.add(points.get(29).get(4));
		closed.add(points.get(29).get(4));
		obstacles.add(points.get(25).get(3));
		closed.add(points.get(25).get(3));
		obstacles.add(points.get(28).get(3));
		closed.add(points.get(28).get(3));
		obstacles.add(points.get(29).get(3));
		closed.add(points.get(29).get(3));
		obstacles.add(points.get(25).get(2));
		closed.add(points.get(25).get(2));
		obstacles.add(points.get(28).get(2));
		closed.add(points.get(28).get(2));
		obstacles.add(points.get(29).get(2));
		closed.add(points.get(29).get(2));
		obstacles.add(points.get(25).get(1));
		closed.add(points.get(25).get(1));
		obstacles.add(points.get(26).get(1));
		closed.add(points.get(26).get(1));
		obstacles.add(points.get(27).get(1));
		closed.add(points.get(27).get(1));
		obstacles.add(points.get(28).get(1));
		closed.add(points.get(28).get(1));
		obstacles.add(points.get(29).get(1));
		closed.add(points.get(29).get(1));
		obstacles.add(points.get(25).get(0));
		closed.add(points.get(25).get(0));
		obstacles.add(points.get(26).get(0));
		closed.add(points.get(26).get(0));
		obstacles.add(points.get(27).get(0));
		closed.add(points.get(27).get(0));
		obstacles.add(points.get(28).get(0));
		closed.add(points.get(28).get(0));
		obstacles.add(points.get(29).get(0));
		closed.add(points.get(29).get(0));
		
		obstacles.add(points.get(26).get(3));
		closed.add(points.get(26).get(3));
		obstacles.add(points.get(27).get(3));
		closed.add(points.get(27).get(3));
		obstacles.add(points.get(26).get(2));
		closed.add(points.get(26).get(2));
		obstacles.add(points.get(27).get(2));
		closed.add(points.get(27).get(2));
	}	
	
	public static void fillWall1()
	{
			// 1
		obstacles.add(points.get(5).get(25));
		closed.add(points.get(5).get(25));
		obstacles.add(points.get(4).get(24));
		closed.add(points.get(4).get(24));
		obstacles.add(points.get(5).get(24));
		closed.add(points.get(5).get(24));
		obstacles.add(points.get(6).get(24));
		closed.add(points.get(6).get(24));
		obstacles.add(points.get(5).get(23));
		closed.add(points.get(5).get(23));
		obstacles.add(points.get(6).get(23));
		closed.add(points.get(6).get(23));
	}	
	
	public static void fillWall2()
	{
			// 2
		obstacles.add(points.get(10).get(20));
		closed.add(points.get(10).get(20));
		obstacles.add(points.get(9).get(19));
		closed.add(points.get(9).get(19));
		obstacles.add(points.get(10).get(19));
		closed.add(points.get(10).get(19));
	}	
	
	public static void fillWall4()
	{
			// 4
		obstacles.add(points.get(19).get(10));
		closed.add(points.get(19).get(10));
	}	
	
	public static void fillWall5()
	{
			// 5
		obstacles.add(points.get(23).get(7));
		closed.add(points.get(23).get(7));
		obstacles.add(points.get(22).get(6));
		closed.add(points.get(22).get(6));
		obstacles.add(points.get(23).get(6));
		closed.add(points.get(23).get(6));
		
		obstacles.add(points.get(24).get(6));
		closed.add(points.get(24).get(6));
		obstacles.add(points.get(23).get(5));
		closed.add(points.get(23).get(5));
		obstacles.add(points.get(24).get(5));
		closed.add(points.get(24).get(5));
		obstacles.add(points.get(25).get(5));
		closed.add(points.get(25).get(5));
		obstacles.add(points.get(24).get(4));
		closed.add(points.get(24).get(4));
	}
	
	
		// Evaluates original path, returns a recalculation that combines similar movements into groups
	public static ArrayList<Pair> groupUpPath(ArrayList<Pair> path)
	{
		System.out.println("Enter GroupUp");
		
		ArrayList<Pair> grouped = new ArrayList<Pair>();
		int group = 0;
		
		for (int i = 0; i < path.size(); i++)
		{
			if (i == 0)
			{
				for (int j = 0; j < path.size() ;j++) 
				{
					grouped.add(new Pair(0,0));
				}
			}
			else
			{
				Pair difference = new Pair(path.get(i).x - path.get(i - 1).x, path.get(i).y - path.get(i - 1).y);
				
				if (difference.x == 1)
				{
					if (difference.y == 1)
					{
						// move north-east
						
						int dir = 1;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
					else if (difference.y == -1)
					{
						// move south-east
						
						int dir = 3;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;					
						
					}
					else
					{
						// move east
						
						int dir = 2;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
				}
				else if (difference.x == -1)
				{
					if (difference.y == 1)
					{
						// move north-west
						
						int dir = 7;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
					else if (difference.y == -1)
					{
						// move south-west
						
						int dir = 5;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
					else
					{
						// move west
						
						int dir = 6;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
				}
				else
				{
					if (difference.y == 1)
					{
						//move north
						
						int dir = 0;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
					else if (difference.y == -1)
					{
						// move south
						
						int dir = 4;
						
						if (!(grouped.get(group).x == dir))
						{
							group++;
						}
						
						grouped.get(group).x = dir;
						grouped.get(group).y = grouped.get(group).y + 1;
					}
					else {}
				}
			}
		}
		return grouped;
	}
		
	
		// Push movement to motors, given path
	public static void move(ArrayList<Pair> gPath)	
	{
		System.out.println("PRINT PROTECTION");
		
		mRight.stop(true);
		mLeft.stop();
		
		System.out.println("PRINT PROTECTION");
		
		System.out.println("Enter Move");
		for (int i = 0; i < gPath.size(); i++)
		{
			if ((gPath.get(i).x == 0) && (gPath.get(i).y == 0)) {/* IF 0, Don't Move */}
			else 
			{
				switch (gPath.get(i).x)
				{
					case 0:
						// north
						rotateTo(45);
						currentTargetAngle = 45;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 1:
						// north-east
						rotateTo(0);
						currentTargetAngle = 0;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 2:
						// east
						rotateTo(-45);
						currentTargetAngle = -45;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 3:
						// south-east
						rotateTo(-90);
						currentTargetAngle = -90;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 4:
						// south
						rotateTo(-135);
						currentTargetAngle = -135;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 5:
						// south-west
						rotateTo(180);
						currentTargetAngle = 180;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 6:
						// west
						rotateTo(135);
						currentTargetAngle = 135;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 7:
						// north-west
						rotateTo(90);
						currentTargetAngle = 90;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
				}
			}
		}
		System.out.println("PRINT PROTECTION");
		
		mRight.stop(true);
		mLeft.stop();
		
		System.out.println("PRINT PROTECTION");
	}
	
	
		// Push movement to motors, given path
	public static void move2(ArrayList<Pair> gPath)
	{
		System.out.println("PRINT PROTECTION");
		
		mRight.stop(true);			
		mLeft.stop();
		
		System.out.println("PRINT PROTECTION");
		
		// a second version of the move() method to solve a rotation issue#2
		System.out.println("Enter move2");

		for (int i = 0; i < gPath.size(); i++)
		{
			if ((gPath.get(i).x == 0) && (gPath.get(i).y == 0)) {/* IF 0, Don't Move */}
			else 
			{
				switch (gPath.get(i).x)
				{
					case 0:
						// north
						rotateTo(-315);
						currentTargetAngle = -315;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 1:
						// north-east
						rotateTo(0);
						currentTargetAngle = 0;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 2:
						// east
						rotateTo(-45);
						currentTargetAngle = -45;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 3:
						// south-east
						rotateTo(-90);
						currentTargetAngle = -90;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 4:
						// south
						rotateTo(-135);
						currentTargetAngle = -135;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 5:
						// south-west
						rotateTo(-180);
						currentTargetAngle = -180;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 6:
						// west
						rotateTo(-225);
						currentTargetAngle = -225;
						System.out.println("PRINT PROTECTION");
						moveStraight(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
					case 7:
						// north-west
						rotateTo(-270);
						currentTargetAngle = -270;
						System.out.println("PRINT PROTECTION");
						moveDiagonal(gPath.get(i).y);
						System.out.println("PRINT PROTECTION");
						break;
				}
			}
		}
		System.out.println("PRINT PROTECTION");
		
		mRight.stop(true);
		mLeft.stop();
		
		System.out.println("PRINT PROTECTION");
	}
	
	
		// Rotation management between movements
	public static void rotateTo(float target)
	{
		
		System.out.println("ROTATE CALLED");
		gyroMode.fetchSample(gyroSample, 0);
		
		if ((target - gyroSample[0]) > 0)
		{
			// rotate anticlockwise
			while (gyroSample[0] < target)
			{	
				mRight.forward();
				mLeft.backward();
				
				mRight.setSpeed(100);
				mLeft.setSpeed(100);
				
				gyroMode.fetchSample(gyroSample, 0);
			}
			
		}
		else if ((target - gyroSample[0]) < 0)
		{
			// rotate clockwise
			while (gyroSample[0] > target)
			{	
				mRight.backward();
				mLeft.forward();
				
				mRight.setSpeed(100);
				mLeft.setSpeed(100);
				
				gyroMode.fetchSample(gyroSample, 0);
			}			
		}
		else {}
		
		mRight.stop(true);
		mLeft.stop();
		
		mRight.setSpeed(200);
		mLeft.setSpeed(200);
		
		System.out.print("PRINT PROTECTION");
		
		mRight.stop(true);
		mLeft.stop();
	}
	
	
		// Rotation management between movements
	public static void rotateTo2(float target)
	{
		// an experimental version of rotateTo() with slower motor speeds, this might give better accuracy
		System.out.println("ROTATE CALLED");
		gyroMode.fetchSample(gyroSample, 0);
		
		if ((target - gyroSample[0]) > 0)
		{
			// rotate anticlockwise
			while (gyroSample[0] < target)
			{	
				mRight.forward();
				mLeft.backward();
				
				mRight.setSpeed(50);
				mLeft.setSpeed(50);
				
				gyroMode.fetchSample(gyroSample, 0);
			}
			
		}
		else if ((target - gyroSample[0]) < 0)
		{
			// rotate clockwise
			while (gyroSample[0] > target)
			{	
				mRight.backward();
				mLeft.forward();
				
				mRight.setSpeed(50);
				mLeft.setSpeed(50);
				
				gyroMode.fetchSample(gyroSample, 0);
			}			
		}
		else {}
		
		mRight.stop(true);
		mLeft.stop();
		
		mRight.setSpeed(200);
		mLeft.setSpeed(200);
		
		System.out.print("PRINT PROTECTION");
		
		mRight.stop(true);
		mLeft.stop();
	}

	
		// Correction application
	public static void bayesianMove()
	{
		if (totalBayesianMoves % 5 == 0)
		{
			// APPLY CORRECTION
			rotateTo(0);
		}
		
		mRight.stop(true);
		mLeft.stop();
		
		mRight.rotate(68, true);
		mLeft.rotate(68);
		
		totalBayesianMoves++;

		mRight.stop(true);
		mLeft.stop();
	}
		
	
		// Sub-movement direction vertical/horizontal
	public static void moveStraight(int noOfMoves)
	{
		System.out.println("PRINT PROTECTION");
		
		for (int i = 0; i < noOfMoves; i++)
		{				
			mRight.stop(true);
			mLeft.stop();
			
			mRight.rotate(155, true);										// ~~~ 40mm
			mLeft.rotate(155);
						
			mRight.stop(true);
			mLeft.stop();
			
			System.out.println("PRINT PROTECTION");
			
			rotateTo(currentTargetAngle);
			totalStraightMoves++;
		}
		
		System.out.println("PRINT PROTECTION"); 
		
		mRight.stop(true);
		mLeft.stop();
		
		System.out.print("PRINT PROTECTION");
	}
	
			
		// Sub-movement direction diagonal
	public static void moveDiagonal(int noOfMoves)
	{
		System.out.println("PRINT PROTECTION");
		
		for (int i = 0; i < noOfMoves; i++)
		{		
			mRight.stop(true);
			mLeft.stop();
			
			mRight.rotate(215, true);							// ~~~ 5.65685424949mm
			mLeft.rotate(215);
						
			mRight.stop(true);
			mLeft.stop();
			
			System.out.println("PRINT PROTECTION"); 
			
			rotateTo(currentTargetAngle);
			totalDiagonalMoves++;			
		}
		
		System.out.println("PRINT PROTECTION");
		
		mRight.stop(true);
		mLeft.stop();
		
		System.out.print("PRINT PROTECTION");		
	}
	
		
		// Logic for robot parking
	public static void park()
	{
		mRight.setSpeed(250);
		mLeft.setSpeed(250);
		
		touchMode.fetchSample(touchSample,0);
		while (touchSample[0] == 0.0)
		{
			mRight.forward();
			mLeft.forward();
			
			touchMode.fetchSample(touchSample,0);
		}
	
		mRight.stop(true);
		mLeft.stop();
		
		Sound.beep();

		// Fetch obstacle color
		colorMode.fetchSample(colorSample,0);
		
		if (colorSample[0] == 2.0)
		{
			// Green
			colorDetect = 2;
		}
		else if (colorSample[0] == 0.0)
		{
			// Red
			colorDetect = 1;
		}
		else {}
		
		mRight.setSpeed(200);
		mLeft.setSpeed(200);
	}
	
	
		// Logic for reversing out of parking
	public static void reverse()
	{
		// reversing 5 grid spaces back
		for (int i = 0; i < 5; i++)
		{
			mRight.rotate(-155, true);										// ~~~ 40mm
			mLeft.rotate(-155);
			
			totalStraightMoves++;
		}
		
		mRight.setSpeed(200);
		mLeft.setSpeed(200);
		
		mRight.forward();
		mLeft.forward();
		
		System.out.print("reverse end, trying for 80% now");
		
		mRight.stop(true);
		mLeft.stop();
		
		System.out.print("PRINT PROTECTION");
	}

	
		// Once end of path, initiate final sequence
	public static void end()
	{
		while (gyroSample[0] > -181 && gyroSample[0] < -183) {}
		
		System.out.println("begin end(), begin attempt at 90%");
		
		mRight.stop(true);
		mLeft.stop();
		
		mRight.setSpeed(350);
		mLeft.setSpeed(350);
		
		mRight.stop(true);
		mLeft.stop();
		
		touchMode.fetchSample(touchSample,0);
		while (touchSample[0] == 0.0)
		{
			mRight.forward();
			mLeft.forward();
			
			touchMode.fetchSample(touchSample,0);
		}
	
		mRight.stop(true);
		mLeft.stop();
		
		Sound.beep();
		Sound.beep();
	}
			
	
	public static void main(String[] args)
	{
		Sound.setVolume(100);
		
		mRight.setSpeed(200);
		mLeft.setSpeed(200);
		
		// Fill 'points' ArayList
		points = new ArrayList<ArrayList<Pair>>(limit + 1);
		for (int i = 0; i < limit + 1; i++)
		{
			ArrayList<Pair> y = new ArrayList<Pair>(limit + 1);
			
			for (int j = 0; j < (limit + 1); j++)
			{
				y.add(new Pair(i, j));
			}
			points.add(y);
		}
				
		Button.waitForAnyPress();
		
		
		// initialise gyro
		gyroSensor = new EV3GyroSensor(SensorPort.S3);
		gyroMode = gyroSensor.getAngleMode();
		gyroSample = new float[gyroMode.sampleSize()];
				
		int local = bayesian();
		rotateTo(0);
		ArrayList<Pair> gPath1 = new ArrayList<Pair>();
		
		if (local == 16 || local == 15 || local == 17 )
		{
			if (obA)
			{
				gPath1 = groupUpPath(pathPlan(8,8,3,20));
			}
			else
			{
				gPath1 = groupUpPath(pathPlan(8,8,6,16));

			}
		}
		else if (local == 22 || local == 21 || local == 23)
		{
			if (obA)
			{
				gPath1 = groupUpPath(pathPlan(10,10,3,20));
			}
			else
			{
				gPath1 = groupUpPath(pathPlan(10,10,6,16));

			}
		}
		else
		{
			System.out.println("OFF BAYESIAN");
			Sound.beep();
			Sound.beep();
			Sound.beep();
			while (true) {}
		}
		
		move(gPath1);
		
		ArrayList<Pair> gPathY = new ArrayList<Pair>();
		if (obA)
		{
			gPathY = groupUpPath(pathPlan(3,20,15,27));
		}
		else
		{
			gPathY = groupUpPath(pathPlan(6,16,15,27));

		}
		
		move(gPathY);
		System.out.println("PRINT PROTECTION");
		
		rotateTo(-43); // -45
		park();
		System.out.println("PRINT PROTECTION");
		reverse();
		System.out.println("PRINT PROTECTION");
		
		mRight.setSpeed(200);
		mLeft.setSpeed(200);
		
		ArrayList<Pair> gPath2 = new ArrayList<Pair>();
		if (colorDetect == 1)
		{
			gPath2 = groupUpPath(pathPlan(15,27,25,9));
		}
		else if (colorDetect == 2)
		{
			gPath2 = groupUpPath(pathPlan(15,27,22,13));

		}
		else
		{
			gPath2 = groupUpPath(pathPlan(15,27,25,9));
		}
		
		move2(gPath2);
		System.out.println("PRINT PROTECTION");
		
		ArrayList<Pair> gPathEND = new ArrayList<Pair>();
		if (colorDetect == 1)
		{
			///*
			ArrayList<Pair> gPathZ3 = groupUpPath(pathPlan(25,9,19,3));	
			move2(gPathZ3);
			System.out.println("PRINT PROTECTION");
			
			if (obA == true)
			{
				System.out.println("PRINT PROTECTION");
				gPathEND = groupUpPath(pathPlan(19,3,8,5));
			}
			else 
			{
				System.out.println("PRINT PROTECTION");
				gPathEND = groupUpPath(pathPlan(19,3,8,5));
			}
		}
		else if (colorDetect == 2)
		{
			gPathEND = groupUpPath(pathPlan(22,13,8,5));	

		}
		else
		{
			gPathEND = groupUpPath(pathPlan(22,13,8,5));
		}
		
		move2(gPathEND);
		System.out.println("PRINT PROTECTION");
		
		rotateTo(-182);
		end();
	}
}