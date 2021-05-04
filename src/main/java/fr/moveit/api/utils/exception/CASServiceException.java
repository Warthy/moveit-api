package fr.moveit.api.utils.exception;

public class CASServiceException extends RuntimeException {
	public CASServiceException(String message) {
		super(message);
	}

	public CASServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
