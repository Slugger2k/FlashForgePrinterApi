package de.slg.ddnss.printertool.clients;

import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_BYE;
import static de.slg.ddnss.printertool.clients.AdventurerCommands.CMD_PRINT_STOP;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;

class AdventurerClientTest {

	private static String printerAddress;

	@BeforeAll
	static void init() {
		try {
			printerAddress = new FlashForgeDiscoverClient().discoverPrinter().getPrinterAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void printMoveXYZ() throws FlashForgePrinterException, IOException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean print = client.moveHome();
		print &= client.setAbsoluteMove();
		print &= client.moveXY(20, 20);
		print &= client.moveXZ(30, 30);
		print &= client.moveYZ(40, 40);
		print &= client.moveX(50);
		print &= client.moveY(50);
		print &= client.moveZ(50);
		print &= client.moveXYZ(10, 10, 10);
		print &= client.setRelativeMove();
		print &= client.moveXY(10, 10);
		print &= client.moveXZ(10, 10);
		print &= client.moveYZ(10, 10);
		print &= client.moveX(10);
		print &= client.moveY(10);
		print &= client.moveZ(10);
		client.close();
		assertTrue(print);
	}

	@Test
	void printMoveHome() throws FlashForgePrinterException, IOException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean print = client.moveHome();
		client.close();
		assertTrue(print);
	}

	@Test
	void printTest() throws FlashForgePrinterException, IOException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean print = client.print("20mm_Box.gx", Files.readAllBytes(Paths.get("20mm_Box.gx")));
		client.close();
		assertTrue(print);
	}

	@Test
	void ledTest() throws FlashForgePrinterException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean replay = client.setLed(false);
		System.out.println(replay);
		client.close();
		assertTrue(replay);
	}

	@Test
	void printStopTest() throws FlashForgePrinterException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean replay = client.stopPrinting();
		client.close();
		assertTrue(replay);
	}
	
	@Test
	void getPrinterInfoTest() throws FlashForgePrinterException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		PrinterInfo printerInfo = client.getPrinterInfo();
		System.out.println(printerInfo);
		client.close();
	}
	
	@AfterAll
	static void cleanup() throws FlashForgePrinterException {
		AdventurerClient adventurerClient = new AdventurerClient(printerAddress);
		String sendOk = adventurerClient.sendCommand(CMD_PRINT_STOP);
		System.out.println(sendOk);
		assertTrue(sendOk.contains("Received"));
		assertTrue(sendOk.contains("ok"));
		
		sendOk = adventurerClient.sendCommand(CMD_BYE);
		System.out.println(sendOk);
		assertTrue(sendOk.equals("CMD M602 Received.\nControl Release.\nok"));
		
		adventurerClient.close();
	}


}
