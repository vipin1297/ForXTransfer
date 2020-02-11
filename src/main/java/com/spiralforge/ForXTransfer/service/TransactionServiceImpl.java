package com.spiralforge.ForXTransfer.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spiralforge.ForXTransfer.dto.ExchangeResponseDto;
import com.spiralforge.ForXTransfer.dto.XchangeDto;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
import com.spiralforge.ForXTransfer.repository.TransactionRepository;
import com.spiralforge.ForXTransfer.utill.Utility;

@Service
public class TransactionServiceImpl implements TransactionService {

	Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	CustomerRepository customerReopsitory;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ExchangeResponseDto previewExchangeAmount(Integer customerId, String base, String quote, Double amount) {
		ExchangeResponseDto exchangeResponseDto = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		String url = "https://api.exchangeratesapi.io/latest?base=" + base;

		XchangeDto xchangeDto = restTemplate.exchange(url, HttpMethod.GET, entity, XchangeDto.class).getBody();
		if (!Objects.isNull(xchangeDto))
			exchangeResponseDto = getExchangeAmount(customerId, base, quote, amount, xchangeDto);
		logger.info(xchangeDto.toString());
		return exchangeResponseDto;
	}

	private ExchangeResponseDto getExchangeAmount(Integer customerId, String base, String quote, Double amount, XchangeDto xchangeDto) {
		
		ExchangeResponseDto exchangeResponseDto = null;
		HashMap<String, Double> xchangeRates = xchangeDto.getRates();
		Optional<Double> xchangeRate = xchangeRates.entrySet().stream()
				.filter(key -> key.getKey().equalsIgnoreCase(quote)).map(mapper -> mapper.getValue()).findAny();

		if(xchangeRate.isPresent()) {
			exchangeResponseDto= new ExchangeResponseDto();
			Double charges=Utility.calculateChareges(amount);
			Double transferredAmount=(double) (Math.round(xchangeRate.get()*amount * 100.0) / 100.0);
			exchangeResponseDto.setCharges(charges);
			exchangeResponseDto.setTransferredAmount(transferredAmount);
			exchangeResponseDto.setTotalAmount(amount+charges);
			exchangeResponseDto.setCustomerId(customerId);
		}
		logger.info(xchangeRate.toString());

		return exchangeResponseDto;
	}
}
