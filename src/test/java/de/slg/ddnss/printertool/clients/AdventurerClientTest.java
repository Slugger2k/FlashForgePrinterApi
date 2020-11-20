package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;

class AdventurerClientTest {

	private static String printerAddress;

	@BeforeAll
	static void init() {
		try {
			printerAddress = new FlashForgeDiscoverClient().getPrinterAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void printTest() throws FlashForgePrinterException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean print = client.print(Paths.get("20mm_Box.gx"));
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
	void unknownHostExceptionTest() {
		Assertions.assertThrows(FlashForgePrinterException.class, () -> {
			new AdventurerClient("0.0.0.a");
		});
		Assertions.assertThrows(FlashForgePrinterException.class, () -> {
			new AdventurerClient("0.0.0.0");
		});
	}

	@Test
	void printStopTest() throws FlashForgePrinterException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean replay = client.stopPrinting();
		client.close();
		assertTrue(replay);
	}

}
