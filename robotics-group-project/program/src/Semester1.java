import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

import java.lang.Math;

/*
	NAMING CONVENTION::
		- Classes		--> UpperCamelCase
		- Methods		--> lowerCamelCase
		- Variables		--> lowerCamelCase

	NOTES::
		"_-_TUNE_-_" --> Specified variable/value must be tested and tuned using sensors before deployment in new environment.

	TO-DO::
		N/A
*/

public class Semester1 {

	// colour
	private static EV3ColorSensor colorSensor;
	private static SensorMode reflection;					// fetch sample
	private static RegulatedMotor mA;						// LEFT	
	private static RegulatedMotor mB;						// RIGHT
	private static SensorMode redSensor;

	private static float black;
	private static float white;
	private static float lineMidpoint = 0;					// 0 = ph


	// ultra (ultrasonic)
	private static EV3MediumRegulatedMotor mC;				// Ultrasonic
	private static EV3UltrasonicSensor ultraSensor;
	private static SampleProvider distance;					// fetch sample
	private static float obMidpoint = 0.11f;				// _-_TUNE_-_ 
	

	// both
	private static float[] sample;							// line follower sample array
	private static float[] redSample;						// red detect sample array
	private static float[] obSample;						// obstacle trace sample array
	private static GraphicsLCD g;
		

		// Initial colour calibration
	public static void calibrateColor()						
	{
		// calibrate colour WHITE
		g.drawString("scan white", 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
		Button.waitForAnyPress();
		g.clear();
		reflection.fetchSample(sample,0);
		white = sample[0];									// WHITE ~ 1.0
		
		// calibrate colour BLACK
		g.drawString("scan black", 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
		Button.waitForAnyPress();
		g.clear();
		reflection.fetchSample(sample,0);
		black = sample[0];									// BLACK ~ 0.0
	}

	
		// Black line follower, using PID controller
	public static void followLine()
	{
		// set LCD
		g = BrickFinder.getDefault().getGraphicsLCD();
		
		// colour/midpoint calibration
		colorSensor = new EV3ColorSensor(SensorPort.S1);
		reflection = colorSensor.getRedMode();
		sample = new float[reflection.sampleSize()];
		calibrateColor();
		lineMidpoint= (white - black)/2 + black;
		// red detect declaration
		redSensor = colorSensor.getColorIDMode();
		redSample = new float[redSensor.sampleSize()];
		
		// ultra calibration
		ultraSensor = new EV3UltrasonicSensor(SensorPort.S2);
		distance = ultraSensor.getDistanceMode();
		obSample = new float[distance.sampleSize()];
				
		// no keys, push to start
		Button.waitForAnyPress();	

		// assign motors
		mA = new EV3LargeRegulatedMotor(MotorPort.A);				// LEFT
		mB = new EV3LargeRegulatedMotor(MotorPort.B);				// RIGHT
		mC = new EV3MediumRegulatedMotor(MotorPort.C);				// Ultra Sensor

		// dec sync
		mA.synchronizeWith(new RegulatedMotor[] {mB});

		// vars - PID
		int tp = 150;												// _-_TUNE_-_ 
		float integral = 0;	
		float lastError = 0;	
		float derivative = 0;	
		
		// vars - Ks , to use as temp multipliers
		int kp = 3500;												// _-_TUNE_-_
		int ki = 250;												// _-_TUNE_-_
		int kd = 60000;												// _-_TUNE_-_
		
		// set initial speeds
		mA.setSpeed(tp);
		mB.setSpeed(tp);
		
		// MAIN loop
		while(true) 
		{			
			// set vars - PID
			reflection.fetchSample(sample, 0);	
			float lightValue = sample[0];	
			if (lightValue >= 1) {														// [COLOUR = white || red] == true
				redSensor.fetchSample(redSample, 0);
				float redVal = redSample[0];
				if (redVal == 0.0f) {													// [colour = red] == true
					mA.startSynchronization();
					mB.stop(true);
					mA.stop(true);
					mA.endSynchronization();
					
					mA.waitComplete();
					mB.waitComplete();
				}
			}
			else 																		// following line (black or white, NOT RED)
			{
				// ultra & midpoint calibration
				distance.fetchSample(obSample, 0);
				float distanceFrom = obSample[0];
				if (distanceFrom < 0.2) {												// _-_TUNE_-_	,	detects obstacle
					// stop
					mB.setSpeed(0);
					mA.setSpeed(0);
					
					lineToOb();															// obstacle detected, start obstacle avoidance/follower sequence
					followOb();
					obToLine();
				}
				
				float error = lightValue - lineMidpoint;
				integral = ((3/4)*integral) + error;									// _-_TUNE_-_
				derivative = error - lastError;	
				float turn = (kp * error) + (ki * integral) + (kd * derivative);		// amount to turn, uses Ks as multipliers	--	PID
				
				// release kp multiplier
				turn = turn / 10;														// _-_TUNE_-_ 
				
				// adjust speeds 	
				int speedA = Math.round(tp + turn);
				if (speedA < 10) {
					mA.backward();
					speedA = 100; 
				} else {
					mA.forward();
				}
				
				int speedB = Math.round(tp - turn);
				if (speedB < 10) {
					mB.backward();
					speedB = 100; 
				} else {
					mB.forward();
				}
				
				mA.setSpeed(speedA);	
				mB.setSpeed(speedB);
								
				// save cur error
				lastError = error;
				
				g.clear();
				g.drawString(Float.toString(lightValue), 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.RIGHT);
			}
		}
	}


		// Intermediary control after obstacle detected in followLine()
	public static void lineToOb()
	{
		// set LCD
		g = BrickFinder.getDefault().getGraphicsLCD();

		// stop when obstacle detected
		mA.setSpeed(0);
		mB.setSpeed(0);
		// move ultra sensor, left (slow)
		mC.setAcceleration(500);
		mC.rotateTo(-90);	
		// move right, while [loop] find obstacle
		mA.setSpeed(50);
		mB.setSpeed(50);
		mB.backward();
		mA.forward();
		
		while(true)
		{
			distance.fetchSample(obSample, 0);
			float distanceFrom = obSample[0];
			g.clear();
			g.drawString(Float.toString(distanceFrom), 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
			if(distanceFrom < 0.3f) {							// _-_TUNE_-_
				mA.setSpeed(0);
				mB.setSpeed(0);
				return;											// control --> followOb()
			}
		}
	}

	
		// Obstacle follower, using P controller
	public static void followOb()
	{			
		// set LCD
		g = BrickFinder.getDefault().getGraphicsLCD();
		
		// initial stop	
		mA.setSpeed(0);
		mB.setSpeed(0);

		// All declarations set in followLine()
		
		// const initial speed
		int tp = 150;													// _-_TUNE_-_ 
		
		// var - K , to use as multipliers
		int kp = 500;													// _-_TUNE_-_


		// set initial speeds
		mA.setSpeed(tp);
		mB.setSpeed(tp);
		
		// MAIN loop
		while(true)
		{
			// colour
			reflection.fetchSample(sample, 0);	
			float lightValue = sample[0];
			if (lightValue < 0.3) {										// detects black line
				mB.stop();
				mA.stop();
				return;													// control --> obToLine()
			}
			
			Delay.msDelay(500);											// _-_TUNE_-_
			
			// set vars - PID
			distance.fetchSample(obSample, 0);	
			float distanceValue = obSample[0];							// value between 0.0 and 1.0
			float error = distanceValue - obMidpoint;					// calc error, with ultra and offset/midpoint			,	if -ve closer
			float turn = kp * error;									// amount to turn, uses kp as multiplier		-- P	,	if -ve closer
		
			// adjust speeds 
			int speedA = 0;
			int speedB = 0;
			if(distanceValue < 0.35 && distanceValue > 0.02) {			// if within range	,	 _-_TUNE_-_ 				
				speedA = Math.round(tp - turn);
				speedB = Math.round(tp + turn);
			}
			else {														// if out of range, infinity
				speedA = 150;											// _-_TUNE_-_
				speedB = 25;											// _-_TUNE_-_
			}

			mA.setSpeed(speedA);	
			mB.setSpeed(speedB);	

			// direction after speed
			mA.forward();
			mB.forward();
			
			g.clear();
			g.drawString(Float.toString(distanceValue), 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
		}
	}


		// Intermediary control after black line detected in followOb()
	public static void obToLine()
	{		
		// set LCD
		g = BrickFinder.getDefault().getGraphicsLCD();
		
		// stop
		mA.setSpeed(0);
		mB.setSpeed(0);
		// move ultra sensor, front (slow)
		mC.setAcceleration(500);
		mC.rotateTo(0);		
		// black line detected --> move backwards
		mA.setSpeed(25);
		mB.setSpeed(25);
		mB.backward();
		mA.backward();
		// backward movement amount
		Delay.msDelay(1000);									// _-_TUNE_-_

		// turn right (towards line, ~ 45dg)
		mA.setSpeed(50);
		mB.setSpeed(50);
		mB.backward();
		mA.forward();
		// turn right amount
		Delay.msDelay(2500);									// _-_TUNE_-_

		while (true) {
			// turn left, towards line (slow)
			mA.setSpeed(10);									// _-_TUNE_-_
			mB.setSpeed(20);									// _-_TUNE_-_
			mB.forward();
			mA.forward();
			reflection.fetchSample(sample, 0);	
			float lightValue = sample[0];	
			g.clear();
			g.drawString(Float.toString(lightValue), 0, 0, GraphicsLCD.VCENTER | GraphicsLCD.LEFT);
			if (lightValue < 0.3) {								// if black line detected
				mA.setSpeed(0);
				mB.setSpeed(0);

				return;											// control --> followLine()
			}
		}
	}
	

	public static void main(String[] args) {
		followLine();
	}
}