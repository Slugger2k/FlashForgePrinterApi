package de.slg.ddnss.printertool.clients;

import java.net.DatagramPacket;

public class FlashForgeDiscoverClient extends UdpDiscoveryClient {

	private final String discoveryPayload = "c0a800de46500000";
	private DatagramPacket receiveMessage;
	
	public FlashForgeDiscoverClient() {
		super();
		sendMessage(discoveryPayload);
		receiveMessage = receiveMessage();
		close();
	}
	
	public FlashForgeDiscoverClient discoverPrinter() {
		sendMessage(discoveryPayload);
		receiveMessage = receiveMessage();
		close();
		return this;
	}
	
	public String getPrinterAddress() {
		return receiveMessage.getAddress().getHostAddress();
	}
	
	public String getPrinterName() {
		return new String(receiveMessage.getData());
	}
}
