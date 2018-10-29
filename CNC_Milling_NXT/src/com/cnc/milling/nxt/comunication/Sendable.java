package com.cnc.milling.nxt.comunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;



/**
 * Clase para definir un estándar para enviar objetos a través del socket que
 * comunica el PC con el robot.
 * <p>
 * Una clase que se necesite enviar a través de un DataOutputStream ( y
 * recibirse en el otro extremo usando un DataInputStream) debe extender de esta
 * clase.
 * <p>
 * Deben implementarse los métodos readFrom y writeTo (cada clase se encarga de
 * definir la lectura/escritura en el socket; es decir, la serialización no es
 * automática).
 * <p>
 * Esta clase esta diseñada para funcionar con las clases {@link Segment} y
 * {@link EndOfMessage} pero puede extenderse de la siguiente forma, para
 * soportar una cantidad arbitraria de clases Sendable:
 * 
 * <pre>
 * class SuperSendable extends Sendable {
 * 	&#064;Overrides
 * 	public int getType() {
 * 		if (this instanceof Clase1)
 * 			return 0;
 * 		if (this instanceof Clase2)
 * 			return 1;
 * 		if (this instanceof Clase3)
 * 			return 2;
 * 		// agregar mas clases (incluso, pueden ignorarse Segment y EndOfMessage)
 * 		return -1;
 * 	}
 * 
 * 	public static Sendable readSendable(DataInputStream dis) throws IOException {
 * 		int kind = dis.readInt();
 * 		if (kind == 0)
 * 			return new Clase1().readFrom(dis);
 * 		if (kind == 1)
 * 			return new Clase2().readFrom(dis);
 * 		if (kind == 2)
 * 			return new Clase3().readFrom(dis);
 * 		throw new IOException(&quot;unrecognized sendable to read&quot;);
 * 	}
 * }
 * </pre>
 * 
 * @see Segment
 * @see EndOfMessage
 * @author Rene Tapia - rene128x.insa@gmail.com
 */
public abstract class Sendable {

	
	/**
	 * Lee un objeto Sendable desde el stream, realizando el cast
	 * correspondiente. Primero se lee el id de la clase y luego, según esa id
	 * se llama al readFrom de la clase de ese id.
	 * 
	 * @param dis
	 *            Stream desde el que leer cada componente del Sendable.
	 * @return Sendable, objeto de la clase correspondiente a id.
	 * @throws IOException
	 *             Si ocurre algún error de comunicación en el stream
	 *             subyacente.
	 * @throws InterruptedException 
	 */
	public static Sendable readSendable(DataInputStream dis) throws IOException, InterruptedException {
		

		
		int kind = dis.readInt();		
		Thread.sleep(250);
		
		if (kind == 0)
			return  new Home().readFrom(dis);
		if (kind == 1)
			return new Segmento().readFrom(dis);		
		if (kind == 2)
			return new EndOfMessage().readFrom(dis);

		throw new IOException("unrecognized sendable to read");
	}

	/**
	 * Escribe un objeto sendable en el stream, escribiendo primero el id de la
	 * clase (para que el otro extremo pueda reconocer la información que
	 * sigue).
	 * 
	 * @param dos
	 *            Stream donde se escribirá la información de sen.
	 * @param sen
	 *            Objeto que se escribirá a sí mismo.
	 * @throws IOException
	 */
	public static void writeSendable(DataOutputStream dos, Sendable sen)
			throws IOException {
	
/*		int kind = sen.getType();
		if (kind >= 0) {
			dos.writeInt(kind);
			sen.writeTo(dos);
			dos.flush();
		} else {
			throw new IOException("unrecognized sendable to write");
		}*/
		dos.writeBoolean(true);
		dos.flush();
	}

	/**
	 * Escribe cada field del objeto en el stream.
	 * 
	 * @param dos
	 *            Stream en el que escribir cada field de la clase.
	 * @return Referencia al mismo objeto (para encadenar métodos).
	 * @throws IOException
	 */
	public abstract Object writeTo(DataOutputStream dos) throws IOException;

	/**
	 * Lee cada field del objeto, desde el stream. No se verifica que la
	 * información en el stream sea correcta, solo se lee.
	 * 
	 * @param dis
	 *            Stream desde el cual leer cada field del objeto.
	 * @return Referencia al mismo objeto (para encadenar métodos).
	 * @throws IOException
	 */
	public abstract Object readFrom(DataInputStream dis) throws IOException;
}
