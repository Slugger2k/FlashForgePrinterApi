package de.slg.ddnss.printertool.exceptions;

public class FlashForgePrinterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FlashForgePrinterException(String message) {
		super(message);
	}

	public FlashForgePrinterException(String message, Throwable e) {
		super(message, e);
	}
}
