package com.spiralforge.ForXTransfer.service;

import javax.validation.Valid;

import com.spiralforge.ForXTransfer.dto.LoginRequestDto;
import com.spiralforge.ForXTransfer.dto.LoginResponseDto;

public interface CustomerService {

	LoginResponseDto checkLogin(@Valid LoginRequestDto loginRequestDto);

}
