package com.spiralforge.ForXTransfer.service;

import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;
import com.spiralforge.ForXTransfer.entity.Customer;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;
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
	@Override
	public LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) throws CustomerNotFoundException {
		log.info("For checking whether the credentials are valid or not");
		Customer customer = customerReopsitory.findByMobileNumberAndPassword(loginRequestDto.getMobileNumber(),
				loginRequestDto.getPassword());
		if (Objects.isNull(customer)) {
			log.error(ApplicationConstants.CUSTOMER_NOTFOUND_MESSAGE);
			throw new CustomerNotFoundException(ApplicationConstants.CUSTOMER_NOTFOUND_MESSAGE);
		}
		LoginResponseDto loginResponseDto = new LoginResponseDto();
		BeanUtils.copyProperties(customer, loginResponseDto);
		return loginResponseDto;
	}
}
