package com.mybank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferPayload {
	BigDecimal amount;
	Integer toAccount;

	public boolean isValid() {
		return amount != null && toAccount != null;
	}
}
