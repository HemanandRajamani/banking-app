package com.mybank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
public class Account {
	private Integer id;
	private BigDecimal balance;

	public synchronized Account withdraw(BigDecimal amount) {
		this.setBalance(this.getBalance().subtract(amount));
		return this;
	}

	public synchronized Account deposit(BigDecimal amount) {
		this.setBalance(this.getBalance().add(amount));
		return this;
	}
}
