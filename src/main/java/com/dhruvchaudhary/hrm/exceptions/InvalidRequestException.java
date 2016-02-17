package com.dhruvchaudhary.hrm.exceptions;

public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = -7796815028279085052L;
	
	private String message;
	
	public InvalidRequestException() {
		this.message = "Invalid Request";
	}
	
	public InvalidRequestException(String message) {
		this.message = message;
	}
	
	public String toString() {
		return message;
	}

}
