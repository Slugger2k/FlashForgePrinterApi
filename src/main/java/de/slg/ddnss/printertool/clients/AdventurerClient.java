package de.slg.ddnss.printertool.clients;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;
import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;
import de.slg.ddnss.printertool.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static de.slg.ddnss.printertool.clients.AdventurerCommands.*;


public class AdventurerClient extends TcpPrinterClient {

	private final static Logger log = LoggerFactory.getLogger(AdventurerClient.class);

	public AdventurerClient(String hostname) {
		super(hostname);
	}

	public boolean print(String filename, byte[] readAllLines) throws FlashForgePrinterException {
		log.info("File: {}/{} byte", filename, readAllLines.length);

		log.info(sendCommand(CMD_PREPARE_PRINT.replaceAll("%%size%%", "" + readAllLines.length)
				.replaceAll("%%filename%%", filename)));

		try {
			List<byte[]> gcode = Util.prepareRawData(readAllLines);
			sendRawData(gcode);
			log.info(sendCommand(CMD_SAVE_FILE));
			log.info(sendCommand(CMD_PRINT_START.replaceAll("%%filename%%", filename)));
			return true;
		} catch (FlashForgePrinterTransferException e) {
			log.error(e.getMessage(), e);
		}

		return false;
	}

	public boolean setLed(boolean on) throws FlashForgePrinterException {
		String replay = sendCommand(on ? CMD_LED_ON : CMD_LED_OFF);
		log.info(replay);
		return replay.contentEquals("CMD M146 Received.\nok");
	}

	public boolean stopPrinting() throws FlashForgePrinterException {
		String replay = sendCommand(CMD_PRINT_STOP);
		log.info(replay);
		return replay.trim().contentEquals("CMD M26 Received.\nok");
	}

	public PrinterInfo getPrinterInfo() throws FlashForgePrinterException {
		String replay = sendCommand(CMD_INFO_STATUS).trim();
		return new PrinterInfo(replay);
	}
	
	public PrinterStatus getPrinterStatus() throws FlashForgePrinterException {
		String replay = sendCommand(CMD_INFO).trim();
		return new PrinterStatus(replay);
	}

}
