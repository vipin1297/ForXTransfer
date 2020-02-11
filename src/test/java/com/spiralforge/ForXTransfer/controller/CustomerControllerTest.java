package com.spiralforge.ForXTransfer.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.service.CustomerService;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerControllerTest {
	LoginRequestDto loginRequestDto = null;
	LoginResponseDto loginResponse = null;
	Customer user = null;

	@InjectMocks
	CustomerController customerController;

	@Mock
	CustomerService customerService;

	@Before
	public void before() {
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("muthu123");

		loginResponse = new LoginResponseDto();
	}

	@Test
	public void testCheckLoginPositive() throws CustomerNotFoundException {
		Mockito.when(customerService.checkLogin(loginRequestDto)).thenReturn(loginResponse);
		ResponseEntity<LoginResponseDto> response = customerController.checkLogin(loginRequestDto);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}

