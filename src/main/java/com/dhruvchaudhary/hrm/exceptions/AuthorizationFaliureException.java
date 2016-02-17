package com.dhruvchaudhary.hrm.exceptions;

public class AuthorizationFaliureException extends InvalidRequestException {

	private static final long serialVersionUID = -4905609904830695782L;

	public String toString() {
		return "You are not authorized to perform this action";
	}
}
