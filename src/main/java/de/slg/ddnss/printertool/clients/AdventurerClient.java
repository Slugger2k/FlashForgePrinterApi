package de.slg.ddnss.printertool.clients;

import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_INFO_STATUS;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_LED_OFF;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_LED_ON;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_PREPARE_PRINT;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_PRINT_START;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_PRINT_STOP;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_SAVE_FILE;

import java.util.List;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;
import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;
import de.slg.ddnss.printertool.util.Util;


public class AdventurerClient extends TcpPrinterClient {
	
	public AdventurerClient(String hostname) {
		super(hostname);
	}

	public boolean print(String filename, byte[] readAllLines) throws FlashForgePrinterException {
		System.out.println("File: " + filename + "/" + readAllLines.length + "byte");
		
		System.out.println(sendCommand(CMD_PREPARE_PRINT.replaceAll("%%size%%", "" + readAllLines.length)
				.replaceAll("%%filename%%", filename)));
		
		try {
			List<byte[]> gcode = Util.prepareRawData(readAllLines);
			sendRawData(gcode);
			System.out.println(sendCommand(CMD_SAVE_FILE));
			System.out.println(sendCommand(CMD_PRINT_START.replaceAll("%%filename%%", filename)));
			return true;
		} catch (FlashForgePrinterTransferException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean setLed(boolean on) throws FlashForgePrinterException {
		String replay = sendCommand(on ? CMD_LED_ON : CMD_LED_OFF);
		System.out.println(replay);
		return replay.contentEquals("CMD M146 Received.\nok");
	}

	public boolean stopPrinting() throws FlashForgePrinterException {
		String replay = sendCommand(CMD_PRINT_STOP);
		System.out.println(replay);
		return replay.trim().contentEquals("CMD M26 Received.\nok");
	}

	public PrinterInfo getPrinterInfo() throws FlashForgePrinterException {
		String replay = sendCommand(CMD_INFO_STATUS).trim();
		PrinterInfo info = new PrinterInfo(replay);
		return info;		
	}

}
