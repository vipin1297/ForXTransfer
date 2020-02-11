package com.spiralforge.ForXTransfer.service;

import com.spiralforge.ForXTransfer.dto.ExchangeResponseDto;

public interface TransactionService {

	ExchangeResponseDto previewExchangeAmount(Integer customerId, String base, String quote, Double amount);

}
