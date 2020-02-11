package com.spiralforge.ForXTransfer.service;

import com.spiralforge.ForXTransfer.dto.ExchangeResponseDto;

public interface TransferService {

	ExchangeResponseDto previewExchangeAmount(Integer customerId, String base, String quote, Double amount);

}
