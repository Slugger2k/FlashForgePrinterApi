package de.slg.ddnss.printertool.clients;


public class PrinterInfo {
	
	private String mashineType;
	private String mashineName;
	private String firmwareVersion;
	private String seriesNr;
	private String dimesions;
	private int toolCount;
	private String mac;
	
	
	public PrinterInfo() {
	}
	
	/*
	 * parses a string like:
	 * 
	 * Machine Type: FlashForge Adventurer III\n
	 * Machine Name: My 3D Printer\n
	 * Firmware: v1.1.7\n
	 * SN: SNFFAD229083\n
	 * X: 150 Y: 150 Z: 150\n
	 * Tool Count: 1\n
	 * Mac Address: 88:A9:A7:90:77:A5\n
	 * \n
	 * ok
	 */
	public PrinterInfo(String replay) {
		String[] split = replay.split("\\n");
		setMashineType(split[1].split(":")[1].trim());
		setMashineName(split[2].split(":")[1].trim());
		setFirmwareVersion(split[3].split(":")[1].trim());
		setSeriesNr(split[4].split(":")[1].trim());
		setDimesions(split[5].trim());
		setToolCount(Integer.valueOf(split[6].split(":")[1].trim()));
		setMac(split[7].trim());
	}
	
	public String getMashineType() {
		return mashineType;
	}
	public void setMashineType(String mashineType) {
		this.mashineType = mashineType;
	}
	public String getMashineName() {
		return mashineName;
	}
	public void setMashineName(String mashineName) {
		this.mashineName = mashineName;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getSeriesNr() {
		return seriesNr;
	}
	public void setSeriesNr(String seriesNr) {
		this.seriesNr = seriesNr;
	}
	public String getDimesions() {
		return dimesions;
	}
	public void setDimesions(String dimesions) {
		this.dimesions = dimesions;
	}
	public int getToolCount() {
		return toolCount;
	}
	public void setToolCount(int toolCount) {
		this.toolCount = toolCount;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public String toString() {
		return "PrinterInfo [mashineType=" + mashineType + ", mashineName=" + mashineName + ", firmwareVersion="
				+ firmwareVersion + ", seriesNr=" + seriesNr + ", dimesions=" + dimesions + ", toolCount=" + toolCount
				+ ", mac=" + mac + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dimesions == null) ? 0 : dimesions.hashCode());
		result = prime * result + ((firmwareVersion == null) ? 0 : firmwareVersion.hashCode());
		result = prime * result + ((mac == null) ? 0 : mac.hashCode());
		result = prime * result + ((mashineName == null) ? 0 : mashineName.hashCode());
		result = prime * result + ((mashineType == null) ? 0 : mashineType.hashCode());
		result = prime * result + ((seriesNr == null) ? 0 : seriesNr.hashCode());
		result = prime * result + toolCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrinterInfo other = (PrinterInfo) obj;
		if (dimesions == null) {
			if (other.dimesions != null)
				return false;
		} else if (!dimesions.equals(other.dimesions))
			return false;
		if (firmwareVersion == null) {
			if (other.firmwareVersion != null)
				return false;
		} else if (!firmwareVersion.equals(other.firmwareVersion))
			return false;
		if (mac == null) {
			if (other.mac != null)
				return false;
		} else if (!mac.equals(other.mac))
			return false;
		if (mashineName == null) {
			if (other.mashineName != null)
				return false;
		} else if (!mashineName.equals(other.mashineName))
			return false;
		if (mashineType == null) {
			if (other.mashineType != null)
				return false;
		} else if (!mashineType.equals(other.mashineType))
			return false;
		if (seriesNr == null) {
			if (other.seriesNr != null)
				return false;
		} else if (!seriesNr.equals(other.seriesNr))
			return false;
		if (toolCount != other.toolCount)
			return false;
		return true;
	}
	
}
