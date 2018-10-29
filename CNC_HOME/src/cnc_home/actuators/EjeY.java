package cnc_home.actuators;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import cnc_home_data_bridge.TLBDataBrigde;



public class EjeY extends Thread{
	private TLBDataBrigde TLBDB;
	private final NXTRegulatedMotor ejeY = Motor.B;
	private final TouchSensor ts3 = new TouchSensor(SensorPort.S3);
	
	
	public EjeY (TLBDataBrigde _TLBDB){
		TLBDB = _TLBDB;
		ejeY.setSpeed(500);		
		
	}
	
	public void run(){		
		ejeY.backward();
		while (!ts3.isPressed()) {
			TLBDB.setyTachoCount(ejeY.getTachoCount());
		}
		ejeY.resetTachoCount();
		TLBDB.setyHome(true);
	}

}
