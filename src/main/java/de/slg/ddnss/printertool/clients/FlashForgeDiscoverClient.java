package de.slg.ddnss.printertool.clients;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;

import java.net.DatagramPacket;

public class FlashForgeDiscoverClient extends UdpDiscoveryClient {

	private final String discoveryPayload = "c0a800de46500000";
	private DatagramPacket receiveMessage;

	public FlashForgeDiscoverClient() throws FlashForgePrinterException {
		super();
	}

	public FlashForgeDiscoverClient discoverPrinter() throws FlashForgePrinterException {
		receiveMessage = sendMessage(discoveryPayload);
		return this;
	}
	
	public String getPrinterAddress() {
		return receiveMessage.getAddress().getHostAddress();
	}

	public String getPrinterName() {
		return new String(receiveMessage.getData());
	}
}
