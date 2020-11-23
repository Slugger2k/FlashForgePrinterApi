package de.slg.ddnss.printertool.clients;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterException;

class FlashForgeDiscoverClientTest {

	@Test
	@Timeout(value = 5)
	void discoverTest() throws FlashForgePrinterException {
		FlashForgeDiscoverClient client = new FlashForgeDiscoverClient();
		assertNotNull(client.discoverPrinter().getPrinterAddress());
		assertNotNull(client.getPrinterAddress());
		assertNotNull(client.getPrinterName());
	}

}
