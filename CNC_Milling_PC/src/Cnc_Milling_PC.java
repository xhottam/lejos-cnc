import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

import com.cnc.pc.client.geometry.PointVO;

public class Cnc_Milling_PC {

	private static BufferedReader in;
	private static NXTConnector con;
	
	static float _mmPERdegree_X = (float) 34.48275862068966;// speed 500 2000 grados 5,80 cm
	static float _mmPERdegree_Y = (float) 34.48275862068966;// speed 500 2000 grados 5,80 cm
	static float _mmPERdegree_Z = (float) 124.31761786600496277915632754342;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CharSequence X = "X-";
		CharSequence Y = "Y-";
		CharSequence Z = "Z-";
		String x_mm = null, y = null, z = null;
		
		
//sphere
		/*// primer valor G0 X del mapa Sphere
		String G0X = "99.796";
		String oldx = "99.796";
		// primer valor G1 Y del mapa
		String G1Y = "39.400";
		// primer valor G0 Z del mapa.
		String G1Z = "50.00";*/
//sphere X0Y0		
		// primer valor G0 X del mapa Sphere
		String G0X = "0.000";
		String oldx = "0.000";
		// primer valor G1 Y del mapa
		String G1Y = "30.880";
		// primer valor G0 Z del mapa.
		String G1Z = "40.00";
		
		/*// primer valor G0 X del mapa Batman
		String G0X = "67.559";
		String oldx = "67.559";
		// primer valor G1 Y del mapa
		String G1Y = "97.475";
		// primer valor G0 Z del mapa.
		String G1Z = "16.000";*/


		// para saber cuand una linea tiene xz
		// varias lineas xz la ultima se quedaba co z = null ya la siguiente era
		// z= null.
		String oldz = G1Z;
		boolean xz = false;
		boolean x = false;

		int lineasx = 0;
		int lineasy = 0;

		ArrayList<String> y_mm = new ArrayList<String>();
		ArrayList<String> z_mm = new ArrayList<String>();

		ArrayList<PointVO> mapa = new ArrayList<PointVO>();

		try {
			in = new BufferedReader(new FileReader("C:\\Users\\xottam\\Desktop\\Finishing Toolpath  - Top.tap"));
			//in = new BufferedReader(new FileReader("F:\\WIN32_LEGO_CNC_eclipse-SDK-4.2RC3-win32\\workspace\\Finishing Toolpath  - Top.tap"));
			//in = new BufferedReader(new FileReader("C:\\workspace\\Batman-Finishing Toolpath  - Top.tap"));
			String str;
			while ((str = in.readLine()) != null) {
				str = str.trim();

				if (str.contains("X")) {
					if (str.contains("Z")) {
						x_mm = (str.split(Z.toString())[0]);
						x_mm = (x_mm.split(X.toString())[1]);

						// if (x != oldx){
						if (!x_mm.contains(oldx)) {
							// nueva linea
							lineasx++;
							System.out.println(lineasx);
							PointVO point = new PointVO();
							point.setLineaX(lineasx);
							point.setLineaY(lineasy);
							point.setX_mm(oldx);
							if (x) {
								point.setxZ_mm(G1Z);
								x = false;
							} else {
								point.setxZ_mm(z);
							}
							point.setY_mm(y_mm);
							point.setyZ_mm(z_mm);
							point.XYZ_transform();

							mapa.add(point);

							oldx = x_mm;
							lineasy = 0;
							y_mm = new ArrayList<String>();
							z_mm = new ArrayList<String>();

						}
						z = str.split(Z.toString())[1];
						xz = true;
					} else {
						x_mm = (str.split(X.toString())[1]);

						// if (x != oldx){
						if (!x_mm.contains(oldx)) {
							// nueva linea
							lineasx++;
							System.out.println(lineasx);
							PointVO point = new PointVO();
							point.setLineaX(lineasx);
							point.setLineaY(lineasy);
							point.setX_mm(oldx);
							if (!xz) {
								point.setxZ_mm(G1Z);
								xz = false;
							} else {
								point.setxZ_mm(z);								
							}
							point.setY_mm(y_mm);
							point.setyZ_mm(z_mm);
							point.XYZ_transform();

							mapa.add(point);

							oldx = x_mm;
							lineasy = 0;
							y_mm = new ArrayList<String>();
							z_mm = new ArrayList<String>();

						}
						x = true;
					}
				} else {

					if (str.contains("Y") && str.contains("Z")) {
						y = (str.split(Z.toString())[0]);
						y = (y.split(Y.toString())[1]);
						z = str.split(Z.toString())[1];
						y_mm.add(y);
						z_mm.add(z);

					} else {
						y = (str.split(Y.toString())[1]);
						y_mm.add(y);						
						z_mm.add(z);
					}
					lineasy++;
				}
			}
			
			String brickName = "NXT";
			String brickAddress = "001653123C0B";
		 	System.out.println(" connecting to " + brickName + " " + brickAddress);
		    con = new NXTConnector();
		    boolean res = con.connectTo(brickName, brickAddress, NXTCommFactory.BLUETOOTH);
		    System.out.println(" connect result " + res);
		
		    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		    DataInputStream dis = new DataInputStream(con.getInputStream());
		    
		    Iterator iterador = mapa.listIterator();
		    
		    //ENVIAR HOME MAPA //HOME CNC en metodo inidiciar() del actuador.
		    dos.writeInt(0); //kind of package HOME
		    dos.writeInt(( Math.round(Float.parseFloat(G0X) * _mmPERdegree_X))); 
		    dos.writeInt(( Math.round(Float.parseFloat(G1Z) * _mmPERdegree_Z)));
		    dos.writeInt(( Math.round(Float.parseFloat(G1Y) * _mmPERdegree_Y)));
		    
		    dos.flush();
		    while (dis.readBoolean() != true);
		    
		    while (iterador.hasNext()){
		    	PointVO segmento = (PointVO) iterador.next();
		    	dos.writeInt(1); //kind of package NORMAL
		    	dos.writeInt(segmento.x_first_degree);
		    	dos.writeInt(segmento.z_first_degree);
		    	/**dos.writeInt(segmento.y_first_degree);
		    	dos.writeInt(segmento.yz_first_degree);*/
		    	dos.writeInt(segmento.y_degrees_int.size());
		    	dos.writeInt(segmento.z_degrees_int.size());
		    	for (int i = 0; i < segmento.y_degrees_int.size();i++){
	    			dos.writeFloat(segmento.y_degree_steps_speed.get(i));
	    			dos.writeInt(segmento.y_degrees_int.get(i));
	    			dos.writeFloat(segmento.z_degree_steps_speed.get(i));
	    			dos.writeInt(segmento.z_degrees_int.get(i));
	    		}
	    	
		    	
		    	dos.flush();
	    		while (dis.readBoolean() != true);
		    }
		    dos.writeInt(2); //kind of package End_Of_Message
		    dos.flush();
		    while (dis.readBoolean() != true);
		    
			
			System.out.println("FIN");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
