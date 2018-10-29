package com.cnc.milling.nxt.main;


import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.PFMate;


import com.cnc.milling.actuators.eje;
import com.cnc.milling.nxt.comunication.EndOfMessage;
import com.cnc.milling.nxt.comunication.Home;
import com.cnc.milling.nxt.comunication.Segmento;
import com.cnc.milling.nxt.comunication.Sendable;





//import lejos.util.Delay;

/**
 * Clase encargada de realizar los movimientos del robot, en base a la
 * información suministrada a través de un Sendable.
 * <p>
 * Las funciones específicas de esta clase son mover los motores para alcanzar
 * una posición del plano cartesiano del papel y mover el motor del lápiz. Los
 * métodos de movimiento, automáticamente remapean el segmento (que tiene
 * unidades de medida en pixeles) a las medidas necesarias para poder distinguir
 * el dibujo sobre el papel.
 * <p>
 * Los parámetros específicos para esta impresora, están asignados en las
 * variables de instancia de la clase directamente y no pueden cambiarse (son
 * constantes).
 * <p>
 * 
 * @see Sendable
 * @see Segment
 * @see EndOfMessage
 * @author René Tapia - rene128x.insa@gmail.com
 */
public class Actuator {
	
	TouchSensor ts1 = new TouchSensor(SensorPort.S1);
	TouchSensor ts4 = new TouchSensor(SensorPort.S4);
	
	NXTRegulatedMotor ejeX  = Motor.A;
	NXTRegulatedMotor ejeY = Motor.B;
	NXTRegulatedMotor ejeZ = Motor.C;
	
	
	private eje objEjeX;
	private eje objEjeY;
	private eje objEjeZ;

	
	
	private static PFMate pfObj;	
	private final int drillSpeed = 4;	
	private static final int PFDelayCMD = 50; // ms.
	
	/**
	 * Establece algunos parámetros que no pueden ser asignados como variables.
	 */
	public Actuator() {						

		
		
		objEjeX = new eje(ejeX,"HOME");
		objEjeY = new eje(ts1,ejeY,"HOME");
		objEjeZ = new eje(ts4,ejeZ,"HOME");
		
		
		
		objEjeX.start();
		objEjeY.start();
		objEjeZ.start();
			
		while (objEjeX.isAlive() || objEjeY.isAlive() || objEjeZ.isAlive());
		
		ejeX.resetTachoCount();
		ejeY.resetTachoCount();
		ejeZ.resetTachoCount();
	}

	/**
	 * Identifica el tipo del {@link Sendable} y llama al dibujador de
	 * {@link Segment}'s o retorna false (en caso de tratarse del fin de mensaje
	 * de la cola).
	 * 
	 * @param sendable
	 *            Sendable recibido por el socket de la clase
	 *            {@link RobotReceiver}, listo para ser interpretado.
	 * @return True, si el Sendable es válido (es decir, se puede dibujar).
	 *         Retorna false si corresponde a una instancia de
	 *         {@link EndOfMessage}
	 * @throws Exception
	 *             Si el Sendable no es reconocido (el robot no sabe como
	 *             tratarlo).
	 */
	public boolean process(Sendable sendable) throws Exception {
		// hay que dibujar un segmento
		if (sendable instanceof Segmento) {
			processSegment((Home) sendable);
			return true;
		}else if (sendable instanceof Home) {
			processSegment((Segmento) sendable);
			return true;
		}else		
		// si es el último mensaje de la cola, notificar a la clase
		// RobotReceiver que debe terminar y cerrar las conecciones
		if (sendable instanceof EndOfMessage){
			return false;
	     }else {
	    	 throw new Exception("Sendable no recognize.");	 
	     }

		
	}

	/**
	 * Dibuja la línea, trazando desde un origen del segmento hasta el otro
	 * origen.
	 * <p>
	 * Se levanta el lápiz, se va al origen, se baja el lápiz, se va al
	 * destino (con el lápiz abajo, dibujando) y finalmente se levanta el
	 * lápiz.
	 * 
	 * @param segment
	 *            {@link Segment} que sera dibujado. El movimiento se calcula
	 *            tomando en cuenta la posición actual y la posición de los
	 *            extremos del Segment.
	 * @throws InterruptedException
	 */
	private void processSegment(Segmento segmento) throws InterruptedException {
		
		
		
		
		
		
		objEjeX = new eje(ejeX,"SEGMENTO_HOME",segmento.x_first_degree);
		objEjeZ = new eje(ejeZ,"SEGMENTO_HOME",segmento.z_first_degree);
		
		objEjeX.start();
		while (objEjeX.isAlive());
		objEjeZ.start();
		while (objEjeZ.isAlive());	


		
		
				
		for (int y = 0; y < segmento.y_degree_steps.size(); y++) {
			
			if (y == 0){
				// para el primer punto no calculamos relacion velocidad distancia.
				
				objEjeZ = new eje(ejeZ,"SEGMENTO",(float) 500,segmento.z_degree_steps.get(y));
				objEjeY = new eje(ejeY,"SEGMENTO",(float) 500,segmento.y_degree_steps.get(y));				
				objEjeZ.start();
				objEjeY.start();
				
			}else {
				
				objEjeY = new eje(ejeY,"SEGMENTO",segmento.y_degree_steps_speed.get(y-1),segmento.y_degree_steps.get(y));
				objEjeZ = new eje(ejeZ,"SEGMENTO",segmento.z_degree_steps_speed.get(y-1),segmento.z_degree_steps.get(y));
				objEjeY.start();
				objEjeZ.start();
				
			}
			
			
			
			while (objEjeY.isAlive() || objEjeZ.isAlive());
			
			/*ejeY.setSpeed(segmento.y_degree_steps_speed.get(y));
			ejeY.rotate(segmento.y_degree_steps.get(y), true);
			ejeZ.setSpeed(segmento.z_degree_steps_speed.get(y));
			ejeZ.rotate(segmento.z_degree_steps.get(y), true);
			
			while(ejeY.isMoving() || ejeZ.isMoving());*/
		}



	}

	private void processSegment(Home segmento) throws InterruptedException {
		
		LCD.clear();
		LCD.drawString("HomeSegmento X", 1, 1);
		LCD.drawInt(segmento.x_home_degree, 1, 10);
		LCD.drawString("HomeSegmento Y", 2, 1);
		LCD.drawInt(segmento.z_home_degree, 2, 10);
		LCD.drawString("HomeSegmento Z", 3, 1);
		LCD.drawInt(segmento.y_home_degree, 3, 10);		
		
		
		
		
		
		objEjeX = new eje(ejeX,"SEGMENTO_HOME",segmento.x_home_degree);
		
		objEjeZ = new eje(ejeZ,"SEGMENTO_HOME",segmento.z_home_degree);
		
		objEjeY = new eje(ejeY,"SEGMENTO_HOME",segmento.y_home_degree);
		
	
		
		pfObj.A.setSpeed(drillSpeed);
		pfObj.A.stop();
		try {Thread.sleep(PFDelayCMD);} catch (Exception e) {}
		pfObj.A.forward();
		
		objEjeX.start();
		while (objEjeX.isAlive());
		objEjeZ.start();
		while (objEjeZ.isAlive());
		objEjeY.start();
		while (objEjeZ.isAlive());
					
		
	}

}
