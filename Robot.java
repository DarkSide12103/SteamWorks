package org.usfirst.frc.team5084.robot;
//AUTON TEST
//Beca's robot
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.buttons.Button;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;;


public class Robot extends SampleRobot {
	Spark lDrive = new Spark (0);
	Spark rDrive = new Spark (1);
	RobotDrive myRobot = new RobotDrive(lDrive , rDrive);
	Joystick stick = new Joystick(0);
	
	Spark shoot = new Spark (4);
	Spark gear = new Spark (2);
	Spark lift = new Spark (3);
	Relay shootdoor = new Relay (3, Relay.Direction.kBoth);
	Encoder lEncode = new Encoder (0, 1, false);
	Encoder rEncode = new Encoder (2, 3, true);
	DigitalInput GearSwitch = new DigitalInput(4);
	double lDist;
	double rDist;
	double aDist;
	double Flag1 = 0;
	double auto2 = 0;
	double auto1 = 0;
	double y;
	double zTwist;
	double yAxis;
	final String DefaultAuto = "Default Auto";
	final String OffAuto = "Off Auto";
	final String LeftAuto = "Left Autonomous";
	final String RightAuto = "Right Autonomous";
	final String CenterAuto = "Center Autonomous";
	SendableChooser<String> chooser = new SendableChooser<>();


	public Robot() {
		myRobot.setExpiration(0.002);
	}

	@Override
	public void robotInit() {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(320, 240);
		
		chooser.addDefault("Default Auto", DefaultAuto);
		chooser.addObject("Off Auto", OffAuto);
		chooser.addObject("Left Autonomous", LeftAuto);
		chooser.addObject("Right Autonomuos", RightAuto);
		chooser.addObject("Center Autonomous", CenterAuto);
		SmartDashboard.putData("Auto modes", chooser);
		
	    myRobot.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
	    myRobot.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
	    
		lEncode.setDistancePerPulse(0.00873);		//1440 PULSES PER REVOLUTION
		rEncode.setDistancePerPulse(0.00873); 
		
	}
	
	

	@Override
	public void autonomous() {
		String autoSelected = chooser.getSelected();
		System.out.println("Auto selected: " + autoSelected);
		lEncode.reset();
		rEncode.reset();

		switch (autoSelected) {
		
		case OffAuto:
			while(this.isAutonomous() && isEnabled()){
			Timer.delay(1);	
			System.out.println("Right Encoder: " + rEncode.get() +", Right Distance: " + rDist);
			System.out.println("Left Encoder: " + lEncode.get() +", Left Distance: " + lDist);
			System.out.println("Average Encoder: " + ((rEncode.get()+lEncode.get())/2) +", Average Distance: " + aDist);
			System.out.println("Auto1: "+ auto1 + " Auto2: " + auto2 + " Switch: " + GearSwitch.get());
			System.out.println("   ");
		    lDist = lEncode.getDistance();
			rDist = rEncode.getDistance();

			myRobot.setSafetyEnabled(false);
			
			myRobot.drive(0.0, 0.0); 
			
			}

			break;
			
		case DefaultAuto:
		default:
			while(this.isAutonomous() && isEnabled()){
				System.out.println("Right Encoder: " + rEncode.get() +", Right Distance: " + rDist);
				System.out.println("Left Encoder: " + lEncode.get() +", Left Distance: " + lDist);
				System.out.println("Average Encoder: " + ((rEncode.get()+lEncode.get())/2) +", Average Distance: " + aDist);
				System.out.println("Auto1: "+ auto1 + ", Auto2: " + auto2 + ", Switch: " + GearSwitch.get() );
				System.out.println("   ");
			    lDist = lEncode.getDistance();
				rDist = rEncode.getDistance();
				aDist = ((lDist + rDist)/2)	;
				
				if (aDist < 25){		
					//drive forward 95 inches
					myRobot.drive(-0.5, 0);
								}
				else{
						myRobot.drive(0.0, 0);
				}
				Timer.delay(.01);
			}

			break;
		case LeftAuto:
			while(this.isAutonomous() && isEnabled()){
			
			
			System.out.println("Encoder: "+rEncode.get()+ " Distance: "+rDist);
			Timer.delay(0.01);
			}
			break;
			
		case RightAuto:
			myRobot.setSafetyEnabled(false);	
			lEncode.reset();
			rEncode.reset();
			while(this.isAutonomous() && isEnabled()){
			Timer.delay(0.01);
			if((lDist < 19)){
				myRobot.drive(-0.25, 0);
			}
			
			if((lDist < 21.2) && (lDist > 19)){
				rDrive.set(-0.25);
			}
			
			if((lDist > 21.2) && (lDist < 25.66)){
				myRobot.drive(-0.25, 0.0);
			}

			if(lDist > 25.66){
				gear.set(-0.5);								
				myRobot.drive(0.0, 0.0);
				Timer.delay(0.5);
				auto1 = 3;
			}
			else if((GearSwitch.get() == true) && (auto1 == 3)){
				myRobot.drive(0.0, 0.0);
				gear.set(0.0);
			}
	
			else{
				myRobot.drive(0, 0);
			}
			}
			break;
			
		case CenterAuto:
			auto1 = 0;	
			myRobot.setSafetyEnabled(false);
			lEncode.reset();
			rEncode.reset();
			

			Timer timer = new Timer();
			timer.start();
			
			while(this.isAutonomous() && isEnabled()){
				
				Timer.delay(.01);
				
				
			    lDist = lEncode.getDistance();
				rDist = rEncode.getDistance();
				aDist = ((lDist + rDist)/2)	;
				
				System.out.println("Right Encoder: " + rEncode.get() +", Right Distance: " + rDist);
				System.out.println("Left Encoder: " + lEncode.get() +", Left Distance: " + lDist);
				System.out.println("Average Encoder: " + ((rEncode.get()+lEncode.get())/2) +", Average Distance: " + aDist);
				System.out.println("Auto1: "+ auto1 + " Auto2: " + auto2 + ", Switch: " + GearSwitch.get());
				System.out.println("   ");
				
				if((aDist > 20) && (auto1 == 0)){
					auto1 = 1;
				}else if((timer.get() < 6) && (auto1 == 1)){
					auto1 = 2;
				}
				
				
				
				if ((aDist < 20 && timer.get() < 5.0) && auto1 == 0){			//drive forward 
					
					myRobot.drive(-0.3, 0.0);
					gear.set(0.0);
					
				}
				else if(timer.get() < 5 && (auto1 == 1)){		//Stop robot once it is finished 

//				else if((aDist > 18.5) && (auto1 == 1)){		//Stop robot once it is finished 
					myRobot.drive(0, 0);
					gear.set(0.0);
				}
				else if(auto1 == 2){
					gear.set(-0.75);								
					myRobot.drive(0.0, 0.0);
					Timer.delay(0.5);
					auto1 = 3;

				}
				
				else if((GearSwitch.get() == true) && (auto1 == 3)){
					myRobot.drive(0.0, 0.0);
					gear.set(0.0);
				}
				
			}

			break;
		}
	}

	@Override
	public void operatorControl() 
	 {
		
        myRobot.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled())
          {
//        	zTwist = stick.getRawAxis(5);
        	//Drive system with a deadzone of 0.1
            myRobot.arcadeDrive(yAxis, zTwist); 
            
        	if((stick.getRawAxis(5) > 0.1) || (stick.getRawAxis(5) < -0.1)){
        		zTwist = ( stick.getRawAxis(5) / 1.25 );
        	}else{
        		zTwist = 0;
        	}
        	
        	if((stick.getY() > 0.05) || (stick.getY() < -0.05)){
        		yAxis = stick.getY();
        	}else{
        		yAxis = 0;
        	}
        	System.out.println("Z Axis: " + zTwist + ", YAxis: " + stick.getY());
        	System.out.println("Right Motor: "+rDrive.get() + ", Left Motor: "+lDrive.get());
          //Gear Lift 
           // System.out.println("GearSwitch: " + Flag1 + ", Switch: " + GearSwitch.get() + " Gear Deploy: " + gear.get());
            if((Flag1 == 0) && (stick.getRawButton(8))){
            	Flag1 = 1;
            }else if((Flag1 == 1) && (GearSwitch.get() == false)){
            	Flag1 = 2;
            }else if((Flag1 == 2) && (GearSwitch.get() == true)){
            	Flag1 = 0;
            }
            if((Flag1 == 1) || (Flag1 == 2)){
            	gear.set(-0.75);
            }else if(Flag1 == 0){
            	gear.set(0.0);
            }
            
          //Shooter Wheel
            //System.out.println(stick.getRawButton(5));
            
			if(stick.getRawButton(1)){
				shoot.set(.9);
	           
				if (stick.getRawButton(5))  {
	                
	                shootdoor.set(Relay.Value.kReverse);
	              } else {
	                shootdoor.set(Relay.Value.kOff);
	              }

			}else if(stick.getRawButton(6)){
				shoot.set(-1);
			}else{
				shoot.set(0);
			}
			//Robot Lifting Mechanism
			if(stick.getRawButton(2)){
		           lift.set(-.15);    

		    }else if(stick.getRawButton(7)){
		    	   lift.set(1);
		    }else{
		    	   lift.set(0);
		    }


            Timer.delay(0.01);
          }
		}
      
  

@Override
        public void test()
      {
      }
  }
