package com.cnc.milling.nxt.comunication;

/**
 * Aplica los métodos correspondientes al ciclo de vida de un objeto
 * {@link Communicable}.
 * <p>
 * Fue hecho solo por simplicidad y orden en el código de las clases que
 * implementan {@link Communicable} y un main al mismo tiempo.
 * 
 * @see Communicable
 * @author René Tapia - rene128x.insa@gmail.com
 */
public final class CommFactory {
	public static void run(Communicable com) throws Exception {
		com.accept();
		com.onAccepted();
		com.mainRutine();
		com.onClosed();
	}
}
