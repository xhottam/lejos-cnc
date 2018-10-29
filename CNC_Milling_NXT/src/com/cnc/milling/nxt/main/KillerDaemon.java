package com.cnc.milling.nxt.main;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

/**
 * Clase que se usa como Thread para detectar una salida de emergencia en el
 * robot (el botón ESCAPE del ladrillo).
 * <p>
 * Se usa para terminar abruptamente el programa del robot (por ejemplo, cuando
 * se traban hojas, ruedas, engranajes, etc) y no esperar a que termine el
 * software normalmente.
 * <p>
 * Es recomendable iniciar una instancia de esta clase, una sola vez, al
 * inicio del método main del programa principal del robot.
 * 
 * @author René Tapia - rene128x.insa@gmail.com
 */
public class KillerDaemon extends Thread {
	public KillerDaemon() {
		// para que este thread se detenga junto con el main
		setDaemon(true);
	}

	@Override
	public void run() {
		
		Button.ESCAPE.waitForPressAndRelease();
		System.exit(0);
	}
}