package de.slg.ddnss.printertool.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterStatus {
	
	private final static Logger log = LoggerFactory.getLogger(PrinterStatus.class);

	private String status;
	private int extruderTemp;
	private int bedTemp;
	private int x;
	private int y;
	private int z;
	
	public PrinterStatus() {
	}
	
	/*
	 * 
	 * CMD M119 Received.
     * Endstop: X-max:1 Y-max:0 Z-max:0
     * MachineStatus: READY
     * MoveMode: READY
     * Status: S:1 L:0 J:0 F:0
     * ok
	 */
	public PrinterStatus(String replay) {
		log.info(replay);
		String[] status = replay.split("\\n");
		setStatus(status[2].split(":")[1].trim());
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
