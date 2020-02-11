package com.spiralforge.ForXTransfer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/customers")
@Slf4j
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class CustomerController {
	@Autowired
	CustomerService customerService;

	/**
	 * @author Muthu
	 * 
	 *         Method is used to check whether he/she is valid customer or not
	 * 
	 * 
	 * @param loginRequestDto which takes the input parameter as mobile number and
	 *                        password
	 * @return LoginResponseDto which returns the customer id and his/her name
	 * @throws CustomerNotFoundException thrown when the customer credentials are
	 *                                   invalid
	 */
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> checkLogin(@Valid @RequestBody LoginRequestDto loginRequestDto)
			throws CustomerNotFoundException {
		log.info("For checking whether the person is staff or a customer");
		LoginResponseDto loginResponse = customerService.checkLogin(loginRequestDto);
		log.info(ApplicationConstants.LOGIN_SUCCESSMESSAGE);
		loginResponse.setMessage(ApplicationConstants.LOGIN_SUCCESSMESSAGE);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}
}
