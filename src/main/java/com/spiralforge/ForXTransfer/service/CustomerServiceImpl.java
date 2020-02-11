package com.spiralforge.ForXTransfer.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
import com.spiralforge.ForXTransfer.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository customerReopsitory;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) {
		log.info("For checking whether the credentials are valid or not");
		Customer customer=new Customer();
		return null;
	}
}
