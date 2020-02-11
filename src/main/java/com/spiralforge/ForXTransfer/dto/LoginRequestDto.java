package com.spiralforge.ForXTransfer.dto;

import javax.validation.constraints.NotBlank;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
	private Long mobileNumber;
	@NotBlank(message = ApplicationConstants.EMPTY_PASSWORD_MESSAGE)
	private String password;
}
