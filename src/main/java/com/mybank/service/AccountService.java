package com.mybank.service;

import java.math.BigDecimal;

public interface AccountService {

	/**
	 * Transfer money between accounts.
	 *
	 * @param fromAccount
	 * @param toAccount
	 * @param amount
	 * @return boolean success or failure.
	 */
	boolean transfer(Integer fromAccount, Integer toAccount, BigDecimal amount);

	/**
	 * Creates a new account.
	 *
	 * @return id of the created account.
	 */
	Integer createAccount();

	/**
	 * Deletes an account by its id.
	 *
	 * @param accountId
	 */
	boolean deleteAccount(Integer accountId);

	/**
	 * Get the account balance by its id.
	 *
	 * @param accountId
	 * @return balance
	 */
	BigDecimal getAccountBalance(Integer accountId);

	/**
	 * Deposits the provided amount to the account.
	 *
	 * @param account
	 * @param amount
	 * @return
	 */
	BigDecimal deposit(Integer account, BigDecimal amount);

	/**
	 * Withdraws the amount from the account.
	 *
	 * @param account
	 * @param amount
	 * @return
	 */
	BigDecimal withdraw(Integer account, BigDecimal amount);
}
