package com.spiralforge.ForXTransfer.controller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.spiralforge.ForXTransfer.entity.Account;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.AccountNotFoundException;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
import com.spiralforge.ForXTransfer.service.CustomerServiceImpl;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CustomerServiceTest {

	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceTest.class);

	@InjectMocks
	CustomerServiceImpl customerService;

	@Mock
	CustomerRepository customerReopsitory;

	@Mock
	AccountRepository accountRepository;

	Customer customer = new Customer();
	Account account = new Account();
	List<Account> accountList = new ArrayList<>();
	AccountResponseDto responseDto = new AccountResponseDto();
	List<AccountResponseDto> responseList = new ArrayList<>();

	@Before
	public void setUp() {
		customer.setCustomerId(1L);

		account.setAccountNumber(1234567L);
		account.setAccountType("Savings");
		account.setBalance(20000D);
		account.setCustomer(customer);
		accountList.add(account);

		BeanUtils.copyProperties(accountList, responseDto);
		responseList.add(responseDto);
	}

	@Test
	public void testAccountListPositive() throws CustomerNotFoundException, AccountNotFoundException {
		Mockito.when(customerReopsitory.findById(1L)).thenReturn(Optional.of(customer));
		Mockito.when(accountRepository.findAccountByCustomer(Optional.of(customer))).thenReturn(accountList);
		logger.info("Got the account details");
		List<AccountResponseDto> responseList = customerService.accountList(1L);
		assertEquals(1, responseList.size());
	}

	@Test(expected = CustomerNotFoundException.class)
	public void testAccountListNegative() throws CustomerNotFoundException, AccountNotFoundException {
		Mockito.when(customerReopsitory.findById(2L)).thenReturn(Optional.of(customer));
		logger.error("customer not found exception occurred");
		customerService.accountList(1L);
	}

	@Test(expected = AccountNotFoundException.class)
	public void testAccountListNegativeException() throws CustomerNotFoundException, AccountNotFoundException {
		List<Account> accountLists = new ArrayList<>();
		Mockito.when(customerReopsitory.findById(1L)).thenReturn(Optional.of(customer));
		Mockito.when(accountRepository.findAccountByCustomer(Optional.of(customer))).thenReturn(accountLists);
		logger.error("Account not found");
		customerService.accountList(1L);
	}
}
