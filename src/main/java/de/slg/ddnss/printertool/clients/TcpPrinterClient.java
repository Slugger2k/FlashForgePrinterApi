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
import java.net.UnknownHostException;
import java.util.List;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;

public class TcpPrinterClient implements Closeable {

	private Socket socket;
	private final int port = 8899;
	private final int timeout = 25000;

	public TcpPrinterClient(String hostname) throws UnknownHostException, IOException {
		socket = new Socket(hostname, port);
		socket.setSoTimeout(timeout);
	}

	public String sendCommand(String cmd) {
		System.out.println("Send Command: " + cmd);
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output, true);
			writer.println(cmd);
			return receiveMultiLineReplay(socket);
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	public String sendRawData(List<byte[]> rawData) throws FlashForgePrinterTransferException {
		try {
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			for (byte[] bs : rawData) {
				System.out.println("Send data: " + bs.length + " bytes");
				dos.write(bs);
				String receiveAnswer = receiveSingleLineReplay(socket);
				System.out.println(receiveAnswer);
				if (receiveAnswer.matches("N\\d{4,}\serror.")) {
					throw new FlashForgePrinterTransferException("Error while transfering the file to the printer.");
				}
			}
			return "";
		} catch (NoRouteToHostException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return "Error";
	}

	public String receiveMultiLineReplay(Socket socket) {
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
			e.printStackTrace();
		}

		return answer.toString().trim();
	}

	public String receiveSingleLineReplay(Socket socket) {
		BufferedReader reader = null;
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			
			String line = reader.readLine();
			return line.trim();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}
}