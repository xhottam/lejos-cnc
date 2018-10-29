import lejos.nxt.Button;
import lejos.nxt.LCD;
import cnc_home.actuators.EjeX;
import cnc_home.actuators.EjeY;
import cnc_home.actuators.EjeZ;
import cnc_home_data_bridge.TLBDataBrigde;



public class CNC_Home {
	
	
	
	private static EjeY objejeY;
	private static EjeZ ojbejeZ;
	private static EjeX ojbejeX;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TLBDataBrigde TLBDB = new TLBDataBrigde();
		
		objejeY = new EjeY(TLBDB);
		ojbejeZ = new EjeZ(TLBDB);
		ojbejeX = new EjeX(TLBDB);
		
		objejeY.start();
		ojbejeZ.start();
		ojbejeX.start();
		
		while (!Button.ESCAPE.isDown()){
			
			LCD.drawInt(TLBDB.getxTachoCount(), 1, 1);
			LCD.drawInt(TLBDB.getyTachoCount(), 1, 2);
			LCD.drawInt(TLBDB.getzTachoCount(), 1, 3);
			if (TLBDB.isyHome()){
				LCD.drawString("Y esta en HOME", 5, 2);
			}
			if (TLBDB.iszHome()){
				LCD.drawString("Z esta en HOME", 5, 3);
			}
			if (TLBDB.isxHome()){
				LCD.drawString("X esta en HOME", 5, 1);
			}
			LCD.refresh();
		}
		LCD.drawString("Finished", 0, 5);
		LCD.refresh();
		System.exit(0);
		
	}

}
