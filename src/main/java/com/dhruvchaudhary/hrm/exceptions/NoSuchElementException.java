package com.dhruvchaudhary.hrm.exceptions;

public class NoSuchElementException extends InvalidRequestException {

	private static final long serialVersionUID = 3976078611827454550L;
	private String message;
	
	public NoSuchElementException(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return message;
	}
}
