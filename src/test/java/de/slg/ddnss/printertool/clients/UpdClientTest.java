package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.DatagramPacket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class UpdClientTest {

	@Test
	@Timeout(value = 5)
	void test() {
		UdpDiscoveryClient client = new UdpDiscoveryClient();
		client.sendMessage("c0a800de46500000");
		DatagramPacket receiveMessage = client.receiveMessage();
		assertNotNull(receiveMessage);
		client.close();
	}

}
