package com.cnc.pc.client.geometry;

import java.util.ArrayList;

public class PointVO {

	int lineasx = 0;
	int lineasy = 0;
	String x_mm = null, xz_mm = null;

	ArrayList<String> y_mm = new ArrayList<String>();
	ArrayList<String> z_mm = new ArrayList<String>();
	ArrayList<Float> y_degrees = new ArrayList<Float>();
	ArrayList<Float> z_degrees = new ArrayList<Float>();
	public ArrayList<Integer> y_degrees_int = new ArrayList<Integer>();
	public ArrayList<Integer> z_degrees_int = new ArrayList<Integer>();
	
	

	ArrayList<Float> y_mm_steps = new ArrayList<Float>();
	ArrayList<Float> z_mm_steps = new ArrayList<Float>();
	
	ArrayList<Float> y_degree_steps = new ArrayList<Float>();
	ArrayList<Float> z_degree_steps = new ArrayList<Float>();
	ArrayList<Integer> y_degree_steps_int = new ArrayList<Integer>();
	ArrayList<Integer> z_degree_steps_int = new ArrayList<Integer>();
	
	public ArrayList<Float> y_degree_steps_speed = new ArrayList<Float>();
	public ArrayList<Float> z_degree_steps_speed = new ArrayList<Float>();

	public int x_first_degree, z_first_degree, y_first_degree, yz_first_degree;

	static float degreePERmm_X = (float) 35.734463276836158192090395480226;
	static float degreePERmm_Y = (float) 33.366303989449390042861852950874;
	static float degreePERmm_Z = (float) 124.31761786600496277915632754342;
	
	private final float speedX = (float) 750.0;
	private final float speedY = (float) 750.0;	
	private final float speedZ = (float) 750.0;
	
	private final float real_speedY =   (float) 0.8667;	// 52 rpm
	private final float real_speedZ_F = (float) 3.5; // 210 rpm
	private final float real_speedZ_B = (float) 3.433; // 206 rpm 
	
	

	public void setLineaX(int _lineasx) {
		// TODO Auto-generated method stub
		lineasx = _lineasx;
	}

	public void setLineaY(int _lineasy) {
		// TODO Auto-generated method stub
		lineasy = _lineasy;
	}

	public void setX_mm(String _x) {
		// TODO Auto-generated method stub
		x_mm = _x;
	}

	public void setY_mm(ArrayList<String> _y_mm) {
		// TODO Auto-generated method stub
		y_mm = _y_mm;
	}

	public void setyZ_mm(ArrayList<String> _z_mm) {
		// TODO Auto-generated method stub
		z_mm = _z_mm;
	}

	public void setxZ_mm(String _z) {
		// TODO Auto-generated method stub
		xz_mm = _z;
	}

	public void XYZ_transform() {

		getXYZ_mmTOdegree();
		getYZ_mmPERstep();
		getYZ_degreesPERstep();

	}

	private void getXYZ_mmTOdegree() {

		z_first_degree = Math.round(Float.parseFloat(xz_mm) * degreePERmm_Z);
		x_first_degree = Math.round(Float.parseFloat(x_mm) * degreePERmm_X);

		/**
		 * ListIterator<String> iterY = y_mm.listIterator();
		 * ListIterator<String> iterZ = z_mm.listIterator(); while
		 * (iterY.hasNext()){
		 * y_degrees.add(Math.round(Float.parseFloat(iterY.next
		 * ()))*mmPERdegree_Y);
		 * z_degrees.add(Math.round(Float.parseFloat(iterZ.next
		 * ()))*mmPERdegree_Z); }
		 */
		int y = y_mm.size();
		for (int x = 0; x < y; x++) {
			if (x == 0) {
				y_first_degree = Math.round(Float.parseFloat(y_mm.get(x))* degreePERmm_Y);
				yz_first_degree = Math.round(Float.parseFloat(z_mm.get(x))* degreePERmm_Z);
				//float para calcular speed
				y_degrees.add((Float.parseFloat(y_mm.get(x))* degreePERmm_Y));
				z_degrees.add((Float.parseFloat(z_mm.get(x))* degreePERmm_Z));
				//int para calcular  grados a rotar
				y_degrees_int.add(Math.round(Float.parseFloat(y_mm.get(x))* degreePERmm_Y));
				z_degrees_int.add(Math.round(Float.parseFloat(z_mm.get(x))* degreePERmm_Z));
			} else {
				//float para calcular speed
				y_degrees.add((Float.parseFloat(y_mm.get(x))* degreePERmm_Y));
				z_degrees.add((Float.parseFloat(z_mm.get(x))* degreePERmm_Z));
				//int para calcular  grados a rotar
				y_degrees_int.add(Math.round(Float.parseFloat(y_mm.get(x))* degreePERmm_Y));
				z_degrees_int.add(Math.round(Float.parseFloat(z_mm.get(x))* degreePERmm_Z));
			}
		}
	}

	private void getYZ_mmPERstep() {

		int y = y_mm.size() - 1;

		float step_y = 0;
		float step_z = 0;

		for (int x = 0; x < y; x++) {
			step_y = Float.parseFloat(y_mm.get(x))
					- Float.parseFloat(y_mm.get(x + 1));
			y_mm_steps.add(step_y);
			step_z = Float.parseFloat(z_mm.get(x))
					- Float.parseFloat(z_mm.get(x + 1));
			z_mm_steps.add(step_z);
		}

	}

	private void getYZ_degreesPERstep() {

		int y = y_degrees.size() - 1;
	    float step_y = 0;
		float step_z = 0;
		
		int step_y_int = 0;
		int step_z_int = 0;
		
		float y_speed = 0;
		float z_speed = 0;
		
		float y_time = 0;
		float z_time = 0;

		for (int x = 0; x < y; x++) {
			//Float calculo velocidad
			step_y = (y_degrees.get(x)) - (y_degrees.get(x + 1));
			//calculo speed evita valores negativos de velocidad
			if (step_y < 0){
				step_y = (step_y * -1);
			}
			//Integer calcuclo rotacion motor
			step_y_int = (y_degrees_int.get(x)) - (y_degrees_int.get(x + 1)); 
			y_degree_steps_int.add(step_y_int*(-1));
			//Float calculo velocidad
			y_degree_steps.add(step_y);
			if (step_y < 0){
				//y_time = ((step_y*(-1)) / speedY);
				y_time = ((step_y*(-1)) / real_speedY);
			}else{
				//y_time = (step_y / speedY);
				y_time = (step_y / real_speedY);
			}
			//Float calculo velocidad						
			step_z = (z_degrees.get(x)) - (z_degrees.get(x + 1));
			//calculo speed evita valores negativos de velocidad
			if (step_z < 0){
				step_z = (step_z *(-1));
			}
			//Integer calcuclo rotacion motor
			step_z_int = (z_degrees_int.get(x)) - (z_degrees_int.get(x + 1));
			z_degree_steps_int.add(step_z_int*(-1));
			//Float calculo velocidad
			z_degree_steps.add(step_z);
			if (step_z < 0){
				//z_time = ((step_z*(-1)) / speedZ);
				z_time = ((step_z*(-1)) / real_speedZ_F);
			}else{
				//z_time = (step_z / speedZ);
				z_time = (step_z / real_speedZ_F);
			}
			
			
			if (y_time < z_time ){				
				y_speed = (step_y / z_time);
				z_speed = speedZ;
				if (y_speed < 1){
					y_speed = 1;
				}				
			}else if (y_time > z_time){
				z_speed = (step_z / y_time);
				y_speed = speedY;
				if (z_speed < 1){
					z_speed = 1;
				}
			}else{
				y_speed =  speedY;
				z_speed =  speedZ;
			}
			
						
			
			y_degree_steps_speed.add(y_speed);
			z_degree_steps_speed.add(z_speed);
		}

	}

}
