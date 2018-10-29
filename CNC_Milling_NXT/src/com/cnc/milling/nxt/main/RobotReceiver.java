package com.cnc.milling.nxt.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

import com.cnc.milling.nxt.comunication.CommFactory;
import com.cnc.milling.nxt.comunication.Communicable;
import com.cnc.milling.nxt.comunication.Sendable;




/**
 * <p>
 * Clase prinicipal para el manejo del robot-impresora. Se encarga de crear una
 * conección bidireccional con el computador, mantenerla abierta y cerrarla
 * apropiadamente.
 * <p>
 * Esta clase implementa la interfaz {@link Communicable}, para objetos que
 * realizan un proceso de comunicación. Los métodos de esta clase muestran en el
 * display del ladrillo el estado actual de la comunicación.
 * <p>
 * El programa maneja una conección distinta para cada imagen que el computador
 * requiera imprimir. Es decir, se cierra la conección cuando termina la
 * impresión y se pone a la espera de una nueva conección entrante.
 * <p>
 * Una vez iniciada la impresora, puede ser "apagada" pulsando el botón ESCAPE
 * del ladrillo. Por apagada se entiende terminado el programa que realiza
 * impresiones (el sistema operativo del ladrillo seguirá funcionando).
 * <p>
 * Cada mensaje {@link Sendable} recibido en la rutina principal de la interfaz
 * que implementa esta clase, es pasada a un objeto de la clase {@link Actuator}
 * para poder ser interpretado. En esta clase no se chequea el fin de la cola de
 * mensajes. La clase es la que discrimina el fin de la cola e indica que se
 * debe terminar la conección.
 * <p>
 * El método main de esta clase, inicia un Thread paralelo (una instancia de la
 * clase {@link KillerDaemon} para cerrar el programa en casos de emergencia.
 * <p>
 * 
 * @see Communicable
 * @see Actuator
 * @see KillerDaemon
 * @see Sendable
 * 
 * @author René Tapia - rene128x.insa@gmail.com
 */
public class RobotReceiver implements Communicable {

	// referencia para la conección inalámbrica y permite recuperar los extremos
	// del canal de comunicación
	private BTConnection pc;

	// extremos de la conección establecida
	private DataInputStream dis;
	private DataOutputStream dos;

	// objeto que realiza las acciones propias del robot (mueve motores, lee
	// sensores, etc etc)
	private Actuator actuator;

	/**
	 * Inicia el programa en la impresora.
	 */
	public static void main(String[] args) {
		// iniciar el thread de salida de emergencia
		new KillerDaemon().start();

		// objeto que permite establecer una comunicación
		RobotReceiver mediator = new RobotReceiver();
		

		// ejecuta el ciclo de vida completo del RobotReceiver
		while (true) {
			try {
				// ejecutar los métodos de la instancia (los 4, en orden)
				CommFactory.run(mediator);
			} catch (Exception e) {
				// Sound.beep();
				System.exit(0);
			}
		}
	}

	public RobotReceiver() {

	}

	/**
	 * Espera una nueva conección entrante (desde el PC).
	 */
	public void accept() throws Exception {
		LCD.clearDisplay();
		LCD.drawString("Esperando...", 1, 1);
		pc = Bluetooth.waitForConnection();
	}

	/**
	 * Con la conección establecida, se recuperan los extremos del canal de
	 * comunicación.
	 */
	public void onAccepted() throws Exception {
		LCD.drawString("Conectado.", 1, 2);
		Thread.sleep(1000);
		dis = pc.openDataInputStream();
		dos = pc.openDataOutputStream();
		
	}

	/**
	 * El proceso de comunicación como tal, es simple. Mientras se reciban
	 * {@link Sendable}'s, se intepretan, por ejemplo, dibujando un segmento. Si
	 * es el final de la lista de mensajes el método process de la clase
	 * {@link Actuator} retorna false.
	 */
	public void mainRutine() throws Exception {
		LCD.drawString("Conexion activa...", 1, 3);
		Thread.sleep(1000);
		LCD.clear();
		actuator = new Actuator();		
		
		// resetear la posición en el eje x del riel
		//actuator.railReachesBorder(actuator.blueSensor());

		// pedir mensaje, intepretarlo y devolverlo (se requiere un nuevo
		// mensaje)
		
		Sendable s;
		while (actuator.process(s = Sendable.readSendable(dis)))
			Sendable.writeSendable(dos, s);
	

		// se levanta el lapiz para dejarlo en la posición de "guardado"
		//actuator.setPencil(Actuator.NOT_WRITING);
	}

	/**
	 * Cuando se termina la comunicación, se cierran las conecciones (para
	 * iniciar otra).
	 */
	public void onClosed() throws Exception {
		try {
			LCD.drawString("Coneccion terminada.", 1, 4);
		} catch (Exception e) {
			System.exit(0);
		}
	}
}
