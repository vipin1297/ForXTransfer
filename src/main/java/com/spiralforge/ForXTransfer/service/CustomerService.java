package com.spiralforge.ForXTransfer.service;

import java.util.List;

import javax.validation.Valid;

import com.spiralforge.ForXTransfer.dto.AccountResponseDto;
import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;

import com.spiralforge.ForXTransfer.exception.AccountNotFoundException;
import com.spiralforge.ForXTransfer.exception.CustomerNotFoundException;

public interface CustomerService {

	LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto) throws CustomerNotFoundException;

	List<AccountResponseDto> accountList(Long customerId) throws CustomerNotFoundException, AccountNotFoundException;

}
