package cnc_home.actuators;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import cnc_home_data_bridge.TLBDataBrigde;



public class EjeX extends Thread{
	private TLBDataBrigde TLBDB;
	private final NXTRegulatedMotor ejeX = Motor.A;
	private final TouchSensor ts1 = new TouchSensor(SensorPort.S1);
	
	
	public EjeX (TLBDataBrigde _TLBDB){
		TLBDB = _TLBDB;
		ejeX.setSpeed(500);		
		
	}
	
	public void run(){		
		ejeX.backward();
		while (!ts1.isPressed()) {
			TLBDB.setxTachoCount(ejeX.getTachoCount());
		}
		ejeX.resetTachoCount();
		TLBDB.setxHome(true);
	}

}
