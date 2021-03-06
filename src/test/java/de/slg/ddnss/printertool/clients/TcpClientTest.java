package de.slg.ddnss.printertool.clients;

import static de.slg.ddnss.printertool.clients.AdventurerCommands.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;
import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;
import de.slg.ddnss.printertool.util.Util;

class TcpClientTest {

	private static TcpPrinterClient client;
	private static String printerAddress;

	@BeforeAll
	static void init() {
		try {
			FlashForgeDiscoverClient flashForgeDiscoverClient = new FlashForgeDiscoverClient();
			flashForgeDiscoverClient.discoverPrinter();
			printerAddress = flashForgeDiscoverClient.getPrinterAddress();
			assertNotNull(printerAddress);
			client = new TcpPrinterClient(printerAddress);
			assertNotNull(client);
		} catch (FlashForgePrinterException e) {
			e.printStackTrace();
		}
	}

	@Test
	void m601test() throws FlashForgePrinterException {
		String sendOk = client.sendCommand(CMD_HELLO);
		System.out.println(sendOk);
		assertEquals("CMD M601 Received.\nControl Success.\nok", sendOk);
	}

	@Test
	void m115test() throws FlashForgePrinterException {
		String sendOk = client.sendCommand(CMD_INFO_STATUS);
		System.out.println(sendOk);
		assertEquals("CMD M115 Received.\nMachine Type: FlashForge Adventurer III\n" + "Machine Name: My 3D Printer\n"
				+ "Firmware: v1.1.7\n" + "SN: SNFFAD229083\n" + "X: 150 Y: 150 Z: 150\n" + "Tool Count: 1\n"
				+ "Mac Address: 88:A9:A7:90:77:A5\n\nok", sendOk);
	}

	@Test
	void m650test() throws FlashForgePrinterException {
		String sendOk = client.sendCommand(CMD_INFO_CAL);
		System.out.println(sendOk);
		assertEquals("CMD M650 Received.\nX: 1.0 Y: 0.5\nok", sendOk);
	}

	@Test
	void m114test() throws FlashForgePrinterException {
		String sendOk = client.sendCommand(CMD_INFO_XYZAB);
		System.out.println(sendOk);
	}

	@Test
	void m119test() throws FlashForgePrinterException {
		String sendOk = client.sendCommand(CMD_INFO);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));
	}

	@Test
	void m27test() throws FlashForgePrinterException {
		String sendOk;
		sendOk = client.sendCommand(CMD_PRINT_STATUS);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));
	}

	@Test
	void m105test() throws FlashForgePrinterException {
		String sendOk;
		sendOk = client.sendCommand(CMD_TEMP);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));
	}

	@Test
	void ledTest() throws FlashForgePrinterException {
		String sendOk;
		sendOk = client.sendCommand(CMD_LED_OFF);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));

		sendOk = client.sendCommand(CMD_LED_ON);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));
	}

	@Test
	void printTest() throws FlashForgePrinterException {
		try {
			Path fileToPrint = Paths.get("20mm_Box.gx");
			byte[] readAllLines = Files.readAllBytes(fileToPrint);
			String filename = fileToPrint.getFileName().toString();
			System.out.println("File: " + filename + "/" + readAllLines.length + "byte");

			System.out.println(client.sendCommand(CMD_PREPARE_PRINT.replaceAll("%%size%%", "" + readAllLines.length)
					.replaceAll("%%filename%%", filename)));

			try {
				List<byte[]> gcode = Util.prepareRawData(readAllLines);
				client.sendRawData(gcode);
				System.out.println(client.sendCommand(CMD_SAVE_FILE));
				System.out.println(client.sendCommand(CMD_PRINT_START.replaceAll("%%filename%%", filename)));

				System.out.println(client.sendCommand(CMD_PRINT_STATUS));
			} catch (FlashForgePrinterTransferException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void unknownHostExceptionTest() {
		Assertions.assertThrows(FlashForgePrinterTransferException.class, () -> {
			new TcpPrinterClient("0.0.0.a");
		});
	}

	@AfterAll
	static void printStopTest() throws FlashForgePrinterException {
		String sendOk = client.sendCommand(CMD_PRINT_STOP);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));

		sendOk = client.sendCommand(CMD_BYE);
		System.out.println(sendOk);
		assertTrue(sendOk.equals("CMD M602 Received.\nControl Release.\nok"));
		client.close();
	}

}
