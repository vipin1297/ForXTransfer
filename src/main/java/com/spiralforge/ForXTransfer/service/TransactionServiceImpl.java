package com.spiralforge.ForXTransfer.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spiralforge.ForXTransfer.constants.ApplicationConstants;
import com.spiralforge.ForXTransfer.entity.Account;
import com.spiralforge.ForXTransfer.entity.FundTransfer;
import com.spiralforge.ForXTransfer.exception.FundTransferEmptyException;
import com.spiralforge.ForXTransfer.repository.AccountRepository;
import com.spiralforge.ForXTransfer.repository.CustomerRepository;
import com.spiralforge.ForXTransfer.repository.FundTransferRepository;

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
	private FundTransferRepository fundTransferRepository;

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
