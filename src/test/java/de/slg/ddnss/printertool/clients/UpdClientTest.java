package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.DatagramPacket;
import java.net.InetAddress;

import org.junit.jupiter.api.Test;

class UpdClientTest {

	@Test
	void test() {
		UdpDiscoveryClient client = new UdpDiscoveryClient();
		
		System.out.println("Send broadcast msg: " + client.sendMessage("c0a800de46500000"));
		
		DatagramPacket receiveMessage = client.receiveMessage();
		InetAddress printerAddress = receiveMessage.getAddress();
		System.out.println("Receive Answer form " + printerAddress.getHostAddress() + ": " + new String(receiveMessage.getData(), 0, receiveMessage.getData().length));
		
		client.close();
		
		assertNotNull(receiveMessage);
	}

}
