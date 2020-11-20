package de.slg.ddnss.printertool.clients;

public class AdventurerCommands {

	/**
	 * Commands for FlashForge Adventurer 3 Printer, 
	 * maybe there work on other models, but i only have access to a Adventurer 3.
	 * 
	 * IDLE-SEQUENZ: M601 S1, M115, M650, M115, M114, M27, M119, M105
	 * PRINT-SEQUNEZ: M28 size filename, Send Data with header, M29, M23 filename
	 */
	public static final String CMD_HELLO = "~M601 S1";
	public static final String CMD_BYE = "~M602";
	public static final String CMD_INFO = "~M119";
	public static final String CMD_INFO_STATUS = "~M115";
	public static final String CMD_INFO_XYZAB = "~M114";
	public static final String CMD_INFO_CAL = "~M650";
	public static final String CMD_TEMP = "~M105";
	public static final String CMD_LED_ON = "~M146 r255 g255 b255 F0";
	public static final String CMD_LED_OFF = "~M146 r0 g0 b0 F0";
	public static final String CMD_PRINT_STATUS = "~M27";
	public static final String CMD_SAVE_FILE = "~M29\r";
	public static final String CMD_PREPARE_PRINT = "~M28 %%size%% 0:/user/%%filename%%\r";
	public static final String CMD_PRINT_START = "~M23 0:/user/%%filename%%\r";
	public static final String CMD_PRINT_STOP = "~M26\r";

}
