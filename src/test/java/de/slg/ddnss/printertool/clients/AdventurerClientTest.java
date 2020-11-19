package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class AdventurerClientTest {

	@Test
	void printTest() throws UnknownHostException, IOException {
		AdventurerClient client = new AdventurerClient(new FlashForgeDiscoverClient().getPrinterAddress());
		boolean print = client.print(Paths.get("20mm_Box.gx"));
		assertTrue(print);
	}

	
	@Test
	void commandTest() throws UnknownHostException, IOException {
		AdventurerClient client = new AdventurerClient(new FlashForgeDiscoverClient().getPrinterAddress());
		String replay = client.sendCommand(AdventurerCommands.CMD_INFO_FW);
		assertNotNull(replay);
	}
}
