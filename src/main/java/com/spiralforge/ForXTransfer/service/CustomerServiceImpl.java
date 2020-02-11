package com.spiralforge.ForXTransfer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.controller.CustomerController;
import com.spiralforge.ForXTransfer.dto.AccountResponseDto;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.entity.Account;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.AccountNotFoundException;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
import com.spiralforge.ForXTransfer.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sri Keerthna.
 * @author Muthu.
 * @author Sujal.
 * @since 2020-02-11.
 */
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	
	/**
	 * The Constant log.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	CustomerRepository customerReopsitory;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) {
		log.info("For checking whether the credentials are valid or not");
		Customer customer = customerReopsitory.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword());
		return null;
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
	@Override
	public List<AccountResponseDto> accountList(Long customerId)
			throws CustomerNotFoundException, AccountNotFoundException {
		Optional<Customer> customer = customerReopsitory.findById(customerId);
		if (!customer.isPresent()) {
			logger.error("customer not found exception occurred");
			throw new CustomerNotFoundException(ApplicationConstants.CUSTOMER_NOTFOUND_MESSAGE);
		}
		List<Account> accounts = accountRepository.findAccountByCustomer(customer);
		if (accounts.isEmpty()) {
			logger.error("Account not found");
			throw new AccountNotFoundException(ApplicationConstants.ACCOUNT_NOTFOUND_MESSAGE);
		}
		List<AccountResponseDto> accountResponseDto = new ArrayList<>();
		accounts.forEach(account -> {
			AccountResponseDto responseDto = new AccountResponseDto();
			BeanUtils.copyProperties(account, responseDto);
			accountResponseDto.add(responseDto);
		});
		logger.info("Got the account details");
		return accountResponseDto;
	}
}
