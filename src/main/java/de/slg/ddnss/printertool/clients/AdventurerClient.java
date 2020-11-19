package de.slg.ddnss.printertool.clients;

import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_PREPARE_PRINT;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_PRINT;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_SAVE_FILE;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;
import de.slg.ddnss.printertool.util.Util;

public class AdventurerClient extends TcpPrinterClient {

	public AdventurerClient(String hostname) throws UnknownHostException, IOException {
		super(hostname);
	}

	public boolean print(Path file) {
		try {
			byte[] readAllLines = Files.readAllBytes(file);
			String filename = file.getFileName().toString();
			System.out.println("File: " + filename + "/" + readAllLines.length + "byte");

			System.out.println(sendCommand(CMD_PREPARE_PRINT.replaceAll("%%size%%", "" + readAllLines.length)
															.replaceAll("%%filename%%", filename)));

			try {
				List<byte[]> gcode = Util.prepareRawData(readAllLines);
				System.out.println(sendRawData(gcode));
				System.out.println(sendCommand(CMD_SAVE_FILE));
				System.out.println(sendCommand(CMD_PRINT.replaceAll("%%filename%%", filename)));
				return true;
			} catch (FlashForgePrinterTransferException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
