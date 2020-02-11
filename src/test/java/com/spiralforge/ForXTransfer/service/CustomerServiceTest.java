package com.spiralforge.ForXTransfer.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerServiceTest {
	LoginRequestDto loginRequestDto = null;
	Customer customer = null;

	@InjectMocks
	CustomerServiceImpl customerServiceImpl;

	@Mock
	CustomerRepository customerRepository;

	@Before
	public void before() {
		loginRequestDto = new LoginRequestDto();
		loginRequestDto.setMobileNumber(9876543210L);
		loginRequestDto.setPassword("muthu123");

		customer = new Customer();
		customer.setCustomerId(1L);
		customer.setCustomerName("Muthu");
		customer.setEmail("muthu@gmail.com");
		customer.setMobileNumber(9876543210L);
		customer.setPassword("muthu123");
	}

	@Test
	public void testCheckLoginPositive() throws CustomerNotFoundException {
		Long mobileNumber = loginRequestDto.getMobileNumber();
		String password = loginRequestDto.getPassword();
		Mockito.when(customerRepository.findByMobileNumberAndPassword(mobileNumber, password)).thenReturn(customer);
		LoginResponseDto response = customerServiceImpl.checkLogin(loginRequestDto);
		assertEquals(customer.getCustomerName(), response.getCustomerName());
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testCheckLoginException() throws CustomerNotFoundException {
		Mockito.when(customerRepository.findByMobileNumberAndPassword(98765L, "muthu")).thenReturn(customer);
		customerServiceImpl.checkLogin(loginRequestDto);
	}
}
