package de.slg.ddnss.printertool.clients;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UdpDiscoveryClient {
	
	private final static Logger log = LoggerFactory.getLogger(UdpDiscoveryClient.class);
	
	private static final String HOSTNAME = "225.0.0.9";
	private final InetAddress address;
	private static final int PORT = 19000;
	private static final int TIMEOUT = 1000;
	private DatagramSocket socket;
	private byte[] buf;

	public UdpDiscoveryClient() throws FlashForgePrinterException {
		try {
			address = InetAddress.getByName(HOSTNAME);
		} catch (UnknownHostException e) {
			throw new FlashForgePrinterException("UnknownHostException for host: " + HOSTNAME, e);
		}
	}

	public DatagramPacket sendMessage(String msg) throws FlashForgePrinterException {
		try {
			buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
			socket = new DatagramSocket();
			socket.setSoTimeout(TIMEOUT);
			log.info("Broadcast: " + msg + " => " + buf.length + "Bytes to " + address.getHostAddress() + ":" + PORT);
			socket.send(packet);
			return receiveMessage();
		} catch (IOException e) {
			throw new FlashForgePrinterException("Error while send or receive broadcast.", e);
		}
	}
	
	private DatagramPacket receiveMessage() throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		log.info("Replay from " + packet.getAddress().getHostAddress() + ": " + new String(packet.getData()));
		socket.close();
		return packet;
	}

}