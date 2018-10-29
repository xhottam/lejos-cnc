package com.cnc.milling.actuators;




import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;

public class eje extends Thread {
	
	private TouchSensor ts;
	private NXTRegulatedMotor motor;
	
	private String accion = null;
	private float speedFordward = 750 ;
	private float speedBackward = 300 ;
	private float speed = 750 ;
	private int  degrees ;
	
	private String TLBDB;
	
	
	public eje (TouchSensor tsObj, NXTRegulatedMotor mObj, String _TLBDB ){
		TLBDB = _TLBDB;
		ts = tsObj ;
		motor = mObj;		
			
	}
	public eje(NXTRegulatedMotor mObj, String _TLBDB) {
		// TODO Auto-generated constructor stub
		TLBDB = _TLBDB;		
		motor = mObj;		
	}
	
	public eje(NXTRegulatedMotor mObj, String _TLBDB, int home_degree) {
		TLBDB = _TLBDB;		
		motor = mObj;
		degrees = home_degree;
		// TODO Auto-generated constructor stub
	}

	public eje(NXTRegulatedMotor mObj, String _TLBDB, Float _speed,Integer _degrees) {
		// TODO Auto-generated constructor stub
		TLBDB = _TLBDB;		
		motor = mObj;
		speed = _speed;
		degrees = _degrees;
		// TODO Auto-generated constru
		
		
	}
	public void run(){
		if (TLBDB == "HOME"){
			motor.setSpeed(speedBackward);
			motor.backward();
			if (ts != null){
				while (!ts.isPressed());								
			}
			motor.stop();
			
		}else if (TLBDB == "SEGMENTO_HOME"){
			motor.setSpeed(speedFordward);
			motor.rotateTo(degrees);						
		}else if (TLBDB == "SEGMENTO"){
			motor.setSpeed(speed);
			motor.rotateTo(degrees);				
		}			
	}

}
