package com.cnc.milling.nxt.comunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.LCD;
import lejos.util.Delay;

public class Segmento extends Sendable {

	
	
	
	
	public int x_first_degree, z_first_degree, y_first_degree, yz_first_degree;
	
	public ArrayList<Integer> y_degree_steps = new ArrayList<Integer>();
	public ArrayList<Integer> z_degree_steps = new ArrayList<Integer>();
	public ArrayList<Float> y_degree_steps_speed = new ArrayList<Float>();
	public ArrayList<Float> z_degree_steps_speed = new ArrayList<Float>();
	public int contadorY, contadorZ = 0;
	
	/**
	 * Segmento vacío (útil cuando se lee desde un flujo).5,
	 */
	public Segmento() {

	} 
	
	
	public Segmento readFrom(DataInputStream dis) throws IOException {
				
		    x_first_degree  = dis.readInt();
		    z_first_degree  = dis.readInt();
		    /**y_first_degree  = dis.readInt();
		    yz_first_degree = dis.readInt();*/
			
		    contadorY = dis.readInt();
			contadorZ = dis.readInt();
			
			for (int y = 0 ; y < contadorY; y++){
				y_degree_steps_speed.add(dis.readFloat());
				y_degree_steps.add(dis.readInt());
				z_degree_steps_speed.add(dis.readFloat());
				z_degree_steps.add(dis.readInt());
		
			}
					
					
			return this;
		}
	public Segmento writeTo(DataOutputStream dos) throws IOException {
	
		return this;
	}
	
}
