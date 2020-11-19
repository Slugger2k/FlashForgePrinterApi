package de.slg.ddnss.printertool.util;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import de.slg.ddnss.printertool.exceptions.FlashForgePrinterTransferException;

public class Util {
	
	// Packet-length = 4112 = 4096 + 16
	private static final int PACKETSIZE = 4096;

	public static byte[] concatenateArrays(byte[] a, byte[] b) {
		byte[] concat = new byte[a.length + b.length];

		int i = 0;
		for (byte aa : a) {
			concat[i] = aa;
			i++;
		}

		for (byte bb : b) {
			concat[i] = bb;
			i++;
		}
		return concat;
	}
	
	/**
	 * generate the header of each packageBundle(4096) with a counter, a CRC32 checksum and packetsize
	 * 
	 * @param packetBundleCounter
	 * @param crc32Checksum
	 * @param lastPackage
	 * @return
	 * @throws FlashForgePrinterTransferException 
	 * @throws DecoderException
	 */
	private static byte[] generatePrintPrePayload(int packetBundleCounter, int packetSize, String crc32Checksum, boolean lastPackage) throws FlashForgePrinterTransferException {
		final byte[] printPrePayload = { (byte) 0x5a, (byte) 0x5a, (byte) 0xa5, (byte) 0xa5, // ZZ¥¥
								   (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // 4 bytes counter 
								   (byte) 0x00, (byte) 0x00, (byte) 0x10, (byte) 0x00, // packetSize full = 0x00 0x00 0x10 0x00 = 4096
								   (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00  // 4 bytes CRC32 checksum
								   };
		
		addPacketBundleCounter(packetBundleCounter, printPrePayload);
		addPacketSize(packetSize, printPrePayload);
		addChecksum(crc32Checksum, printPrePayload);
        
		return printPrePayload;
	}

	public static void addPacketBundleCounter(int packetBundleCounter, byte[] printPrePayload) {
		byte[] packetBundleCounterArray = ByteBuffer.allocate(4).putInt(packetBundleCounter).array();
		printPrePayload[4] = packetBundleCounterArray[0];
		printPrePayload[5] = packetBundleCounterArray[1];
		printPrePayload[6] = packetBundleCounterArray[2];
		printPrePayload[7] = packetBundleCounterArray[3];
	}

	public static void addPacketSize(int packetSize, byte[] printPrePayload) {
		byte[] packetSizeByteArray = ByteBuffer.allocate(4).putInt(packetSize).array();
		printPrePayload[8] = packetSizeByteArray[0];
		printPrePayload[9] = packetSizeByteArray[1];
		printPrePayload[10] = packetSizeByteArray[2];
		printPrePayload[11] = packetSizeByteArray[3];
	}

	public static void addChecksum(String crc32Checksum, byte[] printPrePayload) throws FlashForgePrinterTransferException {
		try {
			byte[] decodeHex = null;
			decodeHex = Hex.decodeHex("00000000".substring(crc32Checksum.length()) + crc32Checksum);
			printPrePayload[15] = decodeHex[3];
			printPrePayload[14] = decodeHex[2];
			printPrePayload[13] = decodeHex[1];
			printPrePayload[12] = decodeHex[0];			
		} catch (Exception e) {
			throw new FlashForgePrinterTransferException("Error while decoding String checksum to byte array!");
		}
	}

	public static String calcChecksum(byte[] fileToPrintArray) {
		Checksum checksum = new CRC32();
        checksum.update(fileToPrintArray, 0, fileToPrintArray.length);
        long checksumValue = checksum.getValue();
        String crc32Checksum = Long.toHexString(checksumValue);
		return crc32Checksum;
	}
	
	public static List<byte[]> prepareRawData(byte[] readAllLines) throws FlashForgePrinterTransferException {
		List<byte[]> gcode = new ArrayList<byte[]>();

		byte[] array = null;
		int i = 0;
		int packetCounter = 0;
		
		for (byte b : readAllLines) {

			if (i == 0) {
				array = new byte[PACKETSIZE];
			}

			array[i] = b;
			
			i++;
			if (i == PACKETSIZE) {
				String crc32Checksum = calcChecksum(array);
				array = Util.concatenateArrays(generatePrintPrePayload(packetCounter, i, crc32Checksum, false), array);
				packetCounter++;
				gcode.add(array);
				i = 0;
			}
		}
		String crc32Checksum = calcChecksum(Arrays.copyOfRange(array, 0, i));
		array = Util.concatenateArrays(generatePrintPrePayload(packetCounter, i, crc32Checksum, true), array);
		gcode.add(array);
		return gcode;
	}
}