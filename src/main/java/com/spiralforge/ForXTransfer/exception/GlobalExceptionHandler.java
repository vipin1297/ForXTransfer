package com.spiralforge.ForXTransfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.dto.ErrorDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ErrorDto> customerNotFoundExceptionException() {
		ErrorDto errorDto = new ErrorDto();
		errorDto.setMessage(ApplicationConstants.CUSTOMER_NOTFOUND_MESSAGE);
		errorDto.setStatusCode(ApplicationConstants.NOTFOUND_CODE);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
	}

}
