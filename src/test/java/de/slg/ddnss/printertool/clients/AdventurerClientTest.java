package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AdventurerClientTest {
	
	private static String printerAddress;

	@BeforeAll
	static void init() {
		try {
			printerAddress = new FlashForgeDiscoverClient().getPrinterAddress();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

//	@Test
	void printTest() throws UnknownHostException, IOException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		boolean print = client.print(Paths.get("20mm_Box.gx"));
		client.close();
		assertTrue(print);
	}

	
	@Test
	void commandTest() throws UnknownHostException, IOException {
		AdventurerClient client = new AdventurerClient(printerAddress);
		String replay = client.sendCommand(AdventurerCommands.CMD_INFO_FW);
		System.out.println(replay);
		client.close();
		assertNotNull(replay);
	}
	
}
