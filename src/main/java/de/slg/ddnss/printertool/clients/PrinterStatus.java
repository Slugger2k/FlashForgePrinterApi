package de.slg.ddnss.printertool.clients;

public class PrinterStatus {

	private String status;
	private int extruderTemp;
	private int bedTemp;
	private int x;
	private int y;
	private int z;
	
	public PrinterStatus() {
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getExtruderTemp() {
		return extruderTemp;
	}
	public void setExtruderTemp(int extruderTemp) {
		this.extruderTemp = extruderTemp;
	}
	public int getBedTemp() {
		return bedTemp;
	}
	public void setBedTemp(int bedTemp) {
		this.bedTemp = bedTemp;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}


	@Override
	public String toString() {
		return "PrinterStatus [status=" + status + ", extruderTemp=" + extruderTemp + ", bedTemp=" + bedTemp + ", x="
				+ x + ", y=" + y + ", z=" + z + "]";
	}
	
}
