package org.hyperledger.indy.sdk;

public class SovrinException extends Exception {

	private static final long serialVersionUID = 2650355290834266477L;

	private ErrorCode errorCode;
	
	public SovrinException(String message, ErrorCode errorCode) {
		super(message);		
		this.errorCode = errorCode;
	}

	public static SovrinException fromErrorCode(ErrorCode errorCode, int err) {

		return new SovrinException("" + (errorCode == null ? null : errorCode.name()) + ": " + (errorCode == null ? null : errorCode.value()) + " (" + Integer.toString(err) + ")", errorCode);
	}
	
	public ErrorCode getErrorCode(){
		return errorCode;
	}
}
