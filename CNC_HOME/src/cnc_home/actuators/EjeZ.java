package cnc_home.actuators;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import cnc_home_data_bridge.TLBDataBrigde;



public class EjeZ extends Thread{
	private TLBDataBrigde TLBDB;
	private final NXTRegulatedMotor ejeZ = Motor.C;
	private final TouchSensor ts4 = new TouchSensor(SensorPort.S4);
	
	public EjeZ (TLBDataBrigde _TLBDB){
		TLBDB = _TLBDB;
		ejeZ.setSpeed(100);
				
	}
	
	public void run(){
		ejeZ.backward();
		while (!ts4.isPressed()) {
			TLBDB.setzTachoCount(ejeZ.getTachoCount());
		}
		ejeZ.resetTachoCount();
		TLBDB.setzHome(true);
	}

}
