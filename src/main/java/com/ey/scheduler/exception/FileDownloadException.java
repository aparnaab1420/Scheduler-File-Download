package com.ey.scheduler.exception;

public class FileDownloadException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileDownloadException() {

	}

	public FileDownloadException(String message) {
		super(message);
	}

	public FileDownloadException(String message, Throwable cause) {
		super(message, cause);
	}
}
