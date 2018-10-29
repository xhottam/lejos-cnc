package com.cnc.milling.nxt.comunication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Home extends Sendable{
	
	public int x_home_degree, y_home_degree, z_home_degree;
	
	public Home(){
		
	}

	@Override
	public Home writeTo(DataOutputStream dos) throws IOException {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Home readFrom(DataInputStream dis) throws IOException {
		// TODO Auto-generated method stub
		
	    x_home_degree  = dis.readInt();
	    z_home_degree  = dis.readInt();
	    y_home_degree  = dis.readInt();	    
	    
		return this;
	}

}
