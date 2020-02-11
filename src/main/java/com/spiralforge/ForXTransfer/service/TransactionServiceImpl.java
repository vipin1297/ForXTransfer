package com.spiralforge.ForXTransfer.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.dto.ExchangeResponseDto;
import com.spiralforge.ForXTransfer.dto.XchangeDto;
import com.spiralforge.ForXTransfer.entity.Account;
import com.spiralforge.ForXTransfer.entity.FundTransfer;
import com.spiralforge.ForXTransfer.exception.FundTransferEmptyException;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
import com.spiralforge.ForXTransfer.repository.FundTransferRepository;
import com.spiralforge.ForXTransfer.repository.TransactionRepository;
import com.spiralforge.ForXTransfer.utill.Utility;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableScheduling
public class TransactionServiceImpl implements TransactionService {

	Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

	@Autowired
	CustomerRepository customerReopsitory;
	
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private FundTransferRepository fundTransferRepository;


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

	@Scheduled(cron = "0 0/30 * * * ?")
	public void scheduleTransactionStatus() throws FundTransferEmptyException {
		List<FundTransfer> fundTransferList = fundTransferRepository
				.findAllByTransferStatus(ApplicationConstants.STATUS_PENDING_MESSAGE);
		if (fundTransferList.isEmpty()) {
			log.error(ApplicationConstants.FUNDTRANSFER_LIST_EMPTY_MESSAGE);
			throw new FundTransferEmptyException(ApplicationConstants.FUNDTRANSFER_LIST_EMPTY_MESSAGE);
		}
		fundTransferList.forEach(transferDetails -> {
			Long toAccount = transferDetails.getToAccount();
			Optional<Account> creditAccount = accountRepository.findById(toAccount);
			if (!(creditAccount.isPresent())) {
				log.info(ApplicationConstants.ACCOUNT_INVALID);
				transferDetails.setTransferStatus(ApplicationConstants.ACCOUNT_INVALID);
				fundTransferRepository.save(transferDetails);
			}
			Long fromAccount = transferDetails.getAccount().getAccountNumber();
			Account debitAccount = accountRepository.findByAccountNumber(fromAccount);
			if (Objects.isNull(debitAccount)) {
				log.info(ApplicationConstants.ACCOUNT_INVALID);
				transferDetails.setTransferStatus(ApplicationConstants.ACCOUNT_INVALID);
				fundTransferRepository.save(transferDetails);
			}
			if (creditAccount.equals(debitAccount.getAccountNumber())) {
				log.info(ApplicationConstants.TRANSACTION_FAILED);
				transferDetails.setTransferStatus(ApplicationConstants.TRANSACTION_FAILED);
				fundTransferRepository.save(transferDetails);
			}
			Double transferAmount = transferDetails.getTransferAmount();
			Double chargeAmount = transferDetails.getChargeAmount();

		});
	}
}
