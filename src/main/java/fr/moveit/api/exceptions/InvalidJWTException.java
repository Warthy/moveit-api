package fr.moveit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidJWTException extends RuntimeException {
	public InvalidJWTException(String message) {
		super(message);
	}

	public InvalidJWTException(String message, Throwable cause) {
		super(message, cause);
	}
}
