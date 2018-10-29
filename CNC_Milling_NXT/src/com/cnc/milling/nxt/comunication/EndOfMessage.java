package com.cnc.milling.nxt.comunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Clase utilizada para marcar el fin de la cola de mensajes. La cola de
 * mensajes es una cola de {@link Segment}'s.
 * 
 * @see Segment
 * @see Sendable
 * @author René Tapia - rene128x.insa@gmail.com
 */
public class EndOfMessage extends Sendable {
	@Override
	public EndOfMessage writeTo(DataOutputStream dos) throws IOException {
		return this;
	}

	@Override
	public EndOfMessage readFrom(DataInputStream dis) throws IOException {
		return this;
	}

	@Override
	public String toString() {
		return "EndOfMessage Sendable instance";
	}
}
