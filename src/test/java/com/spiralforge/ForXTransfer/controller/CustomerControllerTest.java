package com.spiralforge.ForXTransfer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spiralforge.ForXTransfer.dto.AccountResponseDto;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.AccountNotFoundException;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.service.CustomerService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerControllerTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomerControllerTest.class);

	@InjectMocks
	CustomerController customerController;

	@Mock
	CustomerService customerService;

	AccountResponseDto responseDto = new AccountResponseDto();
	List<AccountResponseDto> responseList = new ArrayList<>();

	LoginRequestDto loginRequestDto = null;
	LoginResponseDto loginResponse = null;
	Customer user = null;

	@Before
	public void before() {
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("muthu123");

		loginResponse = new LoginResponseDto();
	}

	@Test
	public void testAccountListPositive() throws CustomerNotFoundException, AccountNotFoundException {
		logger.info("Entered into accountList method in Testcontroller");
		Mockito.when(customerService.accountList(1L)).thenReturn(responseList);
		ResponseEntity<List<AccountResponseDto>> responseList = customerController.accountList(1L);
		assertEquals(200, responseList.getStatusCodeValue());
	}

	@Test
	public void testCheckLoginPositive() throws CustomerNotFoundException {
		Mockito.when(customerService.checkLogin(loginRequestDto)).thenReturn(loginResponse);
		ResponseEntity<LoginResponseDto> response = customerController.checkLogin(loginRequestDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
