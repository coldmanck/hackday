package com.theta360.lib.rexif;

/**
 * Exceptional EXIF reading class
 */
public class ExifReadException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates exception
	 * 
	 * @param e
	 *            Cause
	 */
	public ExifReadException(Exception e) {
		super(e);
	}

	/**
	 * Creates exception
	 * 
	 * @param message
	 *            Detailed message
	 */
	public ExifReadException(String message) {
		super(message);
	}
}
