package com.spiralforge.ForXTransfer.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeResponseDto implements Serializable {

	private Integer customerId;
	private Double transferredAmount;
	private Double charges;
	private Double totalAmount;
	private Integer statusCode;
	private String message;
}
