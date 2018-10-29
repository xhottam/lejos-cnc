package cnc_home_data_bridge;

public class TLBDataBrigde {

	private boolean yHome = false;
	private boolean zHome = false;
	private boolean xHome = false;
	
	private int yTachoCount = 0;
	private int zTachoCount = 0;
	private int xTachoCount = 0;
	
	public boolean isyHome() {
		return yHome;
	}
	public boolean iszHome() {
		return zHome;
	}
	public int getyTachoCount() {
		return yTachoCount;
	}
	public int getzTachoCount() {
		return zTachoCount;
	}
	public void setyHome(boolean yHome) {
		this.yHome = yHome;
	}
	public void setzHome(boolean zHome) {
		this.zHome = zHome;
	}
	public void setyTachoCount(int yTachoCount) {
		this.yTachoCount = yTachoCount;
	}
	public void setzTachoCount(int zTachoCount) {
		this.zTachoCount = zTachoCount;
	}
	public void setxTachoCount(int xTachoCount) {
		// TODO Auto-generated method stub
		this.xTachoCount = xTachoCount;
		
	}
	public void setxHome(boolean xHome) {
		// TODO Auto-generated method stub
		this.xHome = xHome;	
	}
	public boolean isxHome() {
		// TODO Auto-generated method stub
		return xHome;
	}
	public int getxTachoCount() {
		// TODO Auto-generated method stub
		return xTachoCount;
	}
	
	
	

}
