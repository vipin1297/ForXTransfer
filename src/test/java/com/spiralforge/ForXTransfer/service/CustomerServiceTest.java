package com.spiralforge.ForXTransfer.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.spiralforge.ForXTransfer.dto.AccountResponseDto;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.entity.Account;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.AccountNotFoundException;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerServiceTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

	@InjectMocks
	CustomerServiceImpl customerServiceImpl;

	@Mock
	CustomerRepository customerRepository;

	@Mock
	AccountRepository accountRepository;

	LoginRequestDto loginRequestDto = null;
	Customer customer = null;

	Customer customers = new Customer();
	Account account = new Account();
	List<Account> accountList = new ArrayList<>();
	AccountResponseDto responseDto = new AccountResponseDto();
	List<AccountResponseDto> responseList = new ArrayList<>();

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

		account.setAccountNumber(1234567L);
		account.setAccountType("Savings");
		account.setBalance(20000D);
		account.setCustomer(customer);
		accountList.add(account);

		BeanUtils.copyProperties(accountList, responseDto);
		responseList.add(responseDto);
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

	@Test
	public void testAccountListPositive() throws CustomerNotFoundException, AccountNotFoundException {
		Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customers));
		Mockito.when(accountRepository.findAccountByCustomer(Optional.of(customers))).thenReturn(accountList);
		logger.info("Got the account details");
		List<AccountResponseDto> responseList = customerServiceImpl.accountList(1L);
		assertEquals(1, responseList.size());
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testAccountListNegative() throws CustomerNotFoundException, AccountNotFoundException {
		Mockito.when(customerRepository.findById(2L)).thenReturn(Optional.of(customers));
		logger.error("customer not found exception occurred");
		customerServiceImpl.accountList(1L);
	}

	@Test(expected = AccountNotFoundException.class)
	public void testAccountListNegativeException() throws CustomerNotFoundException, AccountNotFoundException {
		List<Account> accountLists = new ArrayList<>();
		Mockito.when(customerRepository.findById(1L)).thenReturn(Optional.of(customers));
		Mockito.when(accountRepository.findAccountByCustomer(Optional.of(customers))).thenReturn(accountLists);
		logger.error("Account not found");
		customerServiceImpl.accountList(1L);
	}
}
