package com.spiralforge.ForXTransfer.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.dto.ExchangeResponseDto;
import com.spiralforge.ForXTransfer.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transactions")
@Slf4j
public class TransactionController {

	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;

	@GetMapping("/customers/{customerId}/transfer/preview")
	public ResponseEntity<ExchangeResponseDto> previewExchangeAmount(@PathVariable("customerId") Integer customerId ,@RequestParam("base") String base,
			@RequestParam("quote") String quote, @RequestParam("amount") Double amount) {
		logger.info("For preview exchange amount");
		ExchangeResponseDto exchangeResponseDto = transactionService.previewExchangeAmount(customerId, base, quote, amount);
		if (Objects.isNull(exchangeResponseDto)) {
			exchangeResponseDto = new ExchangeResponseDto();
			exchangeResponseDto.setMessage(ApplicationConstants.FAILED);
			exchangeResponseDto.setStatusCode(405);
		} else {
			exchangeResponseDto.setMessage(ApplicationConstants.SUCCESS);
			exchangeResponseDto.setStatusCode(205);
		}
		return new ResponseEntity<>(exchangeResponseDto, HttpStatus.OK);
	}

}
