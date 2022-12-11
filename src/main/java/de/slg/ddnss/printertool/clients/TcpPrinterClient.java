package de.slg.ddnss.printertool.clients;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;
import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class TcpPrinterClient implements Closeable {
	
	private final static Logger log = LoggerFactory.getLogger(TcpPrinterClient.class);

	private Socket socket;
	private final int port = 8899;
	private final int timeout = 25000;
	private final String hostname;

	public TcpPrinterClient(String hostname) {
		this.hostname = hostname;
	}

	public String sendCommand(String cmd) throws FlashForgePrinterException {
		log.info("Send Command: " + cmd);
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
				log.info("Send data: " + bs.length + " bytes");
				dos.write(bs);
				String replay = receiveSingleLineReplay(socket);
				log.info(replay);
				if (!replay.matches("N\\d{4,}\\sok.")) {
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

	private void checkSocket() throws IOException {
		if (socket == null || socket.isClosed()) {
			log.info("Socket null or closed");
			socket = new Socket(hostname, port);
			socket.setSoTimeout(timeout);
		}
	}

	public String receiveMultiLineReplay(Socket socket) throws FlashForgePrinterException {
		var answer = new StringBuilder();
		BufferedReader reader;
		try {
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));

			String line;
			while ((line = reader.readLine()) != null) {
				answer.append(line).append("\n");
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
		BufferedReader reader;
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
			log.error(e.getMessage(), e);
		}
	}
}