package de.slg.ddnss.printertool.clients;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpDiscoveryClient {
	private DatagramSocket socket;
	private InetAddress address;

	private byte[] buf;

	public UdpDiscoveryClient() {
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName("225.0.0.9");
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

	public boolean sendMessage(String msg) {
		buf = msg.getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 19000);
		try {
			System.out.println("Broadcast: " + msg + " => " + buf.length + "Bytes to " + address.getHostAddress() + ":19000");
			socket.send(packet);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public DatagramPacket receiveMessage() {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
			System.out.println("Replay from " + packet.getAddress().getHostAddress() + ": " + new String(packet.getData()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return packet;
	}

	public void close() {
		socket.close();
	}
}