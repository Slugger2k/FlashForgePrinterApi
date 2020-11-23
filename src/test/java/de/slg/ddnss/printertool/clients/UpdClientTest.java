package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.DatagramPacket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;

class UpdClientTest {
	
	static UdpDiscoveryClient client;
	
	@BeforeAll
	static void init() throws FlashForgePrinterException {
		client = new UdpDiscoveryClient();
	}

	@Test
	@Timeout(value = 5)
	void test() throws FlashForgePrinterException {
		DatagramPacket receiveMessage = client.sendMessage("c0a800de46500000");
		assertNotNull(receiveMessage);
	}
	
}
