package fr.moveit.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	public ForbiddenException(String message) {
		super("Forbidden action: " + message);
	}

	public ForbiddenException(String message, Throwable cause) {
		super(message, cause);
	}
}
