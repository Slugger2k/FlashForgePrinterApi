package de.slg.ddnss.printertool.clients;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;
import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;

public class TcpPrinterClient implements Closeable {

	private Socket socket;
	private final int port = 8899;
	private final int timeout = 25000;
	private String hostname;

	public TcpPrinterClient(String hostname) {
		this.hostname = hostname;
	}

	public String sendCommand(String cmd) throws FlashForgePrinterException {
		System.out.println("Send Command: " + cmd);
		try {
			checkSocket();
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(cmd);
			return receiveMultiLineReplay(socket);
		} catch (NoRouteToHostException e) {
			throw new FlashForgePrinterTransferException("Error while connecting. No route to host ["  + socket.getInetAddress().getHostAddress() + "].");
		} catch (UnknownHostException e) {
			throw new FlashForgePrinterTransferException("Error while connecting. Unknown host ["  + socket.getInetAddress().getHostAddress() + "].");
		} catch (IOException e) {
			throw new FlashForgePrinterException("Error while building or writing outputstream.", e);
		}
	}

	public void sendRawData(List<byte[]> rawData) throws FlashForgePrinterException {
		try {
			checkSocket();
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			for (byte[] bs : rawData) {
				System.out.println("Send data: " + bs.length + " bytes");
				dos.write(bs);
				String replay = receiveSingleLineReplay(socket);
				System.out.println(replay);
				if (replay.matches("N\\d{4,}\\serror.")) {
					throw new FlashForgePrinterTransferException("Error while transfering data.");
				}
			}
		} catch (NoRouteToHostException e) {
			throw new FlashForgePrinterTransferException("Error while connecting. No route to host ["  + socket.getInetAddress().getHostAddress() + "].");
		} catch (UnknownHostException e) {
			throw new FlashForgePrinterTransferException("Error while connecting. Unknown host ["  + socket.getInetAddress().getHostAddress() + "].");
		} catch (IOException e) {
			throw new FlashForgePrinterException("Error while building or writing outputstream.", e);
		}
	}

	private void checkSocket() throws UnknownHostException, IOException, SocketException {
		if (socket == null || socket.isClosed()) {
			System.out.println("Socket null or closed");
			socket = new Socket(hostname, port);
			socket.setSoTimeout(timeout);
		}
	}

	public String receiveMultiLineReplay(Socket socket) throws FlashForgePrinterException {
		StringBuffer answer = new StringBuffer();
		BufferedReader reader = null;
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line + "\n");
				if (line.equalsIgnoreCase("ok")) {
					break;
				}
			}
		} catch (IOException e) {
			throw new FlashForgePrinterException("Error while building or reading inputstream.", e);
		}

		return answer.toString().trim();
	}

	public String receiveSingleLineReplay(Socket socket) throws FlashForgePrinterException {
		BufferedReader reader = null;
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			String line = reader.readLine();
			return line.trim();
		} catch (IOException e) {
			throw new FlashForgePrinterException("Error while building or reading inputstream.", e);
		}
	}

	@Override
	public void close() {
		try {
			socket.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}