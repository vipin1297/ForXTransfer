package com.spiralforge.ForXTransfer.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.dto.AccountResponseDto;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.exception.AccountNotFoundException;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sri Keerthna.
 * @author Muthu.
 * @author Sujal.
 * @since 2020-02-11.
 */
@RestController
@RequestMapping("/customers")
@Slf4j
@CrossOrigin(allowedHeaders = { "*", "*/" }, origins = { "*", "*/" })
public class CustomerController {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	@Autowired
	CustomerService customerService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> checkLogin(@Valid @RequestBody LoginRequestDto loginRequestDto)
			throws CustomerNotFoundException {
		log.info("For checking whether the person is staff or a customer");
		LoginResponseDto loginResponse = customerService.checkLogin(loginRequestDto);
		log.info(ApplicationConstants.LOGIN_SUCCESSMESSAGE);
		loginResponse.setMessage(ApplicationConstants.LOGIN_SUCCESSMESSAGE);
		return new ResponseEntity<>(loginResponse, HttpStatus.OK);
	}

	/**
	 * @author Sri Keerthna.
	 * @since 2020-02-11. In this method if the customer is having accounts then it
	 *        will fetch the account details of that particular customer.
	 * @param customerId got from the customer.
	 * @return list of accounts.
	 * @throws CustomerNotFoundException if customer is not there then it will throw
	 *                                   this exception.
	 * @throws AccountNotFoundException  if account is not there for that customer
	 *                                   then it will throw this exception.
	 */
	@GetMapping("/{customerId}/accounts")
	public ResponseEntity<List<AccountResponseDto>> accountList(@PathVariable Long customerId)
			throws CustomerNotFoundException, AccountNotFoundException {
		logger.info("Entered into accountList method in controller");
		List<AccountResponseDto> accountResponseList = customerService.accountList(customerId);
		return new ResponseEntity<>(accountResponseList, HttpStatus.OK);
	}
}
