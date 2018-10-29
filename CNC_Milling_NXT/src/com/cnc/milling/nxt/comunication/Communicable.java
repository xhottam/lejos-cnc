package com.cnc.milling.nxt.comunication;

/**
 * Interfaz para ordenar el código del ciclo de vida de un programa que se
 * comunica mediante sockets.
 * <p>
 * Los métodos deben implementarse según la función que cumplen en el ciclo de
 * vida del programa:
 * <ul>
 * <li>accept(): código que espera una conección válida
 * <li>onAccepted(): código que se ejecuta luego de aceptar la conección, sirve
 * para tomar un extremo del socket y linkearlo a un objeto stream.
 * <li>mainRutine(): suponiendo que los streams son válidos, ejecutar el
 * programa principal, enviando/recibiendo información desde los streams.
 * <li>onClosed(): código que cierra los sockets y finaliza el ciclo de vida del
 * programa.
 * </ul>
 * 
 * @author René Tapia - rene128x.insa@gmail.com
 * @see CommFactory
 */

public interface Communicable {
	public void accept() throws Exception;

	public void onAccepted() throws Exception;

	public void mainRutine() throws Exception;

	public void onClosed() throws Exception;
}
