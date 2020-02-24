package com.mybank.service;

import com.mybank.exception.AccountOperationException;
import com.mybank.model.Account;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountServiceImpl implements AccountService {

	private Lock accountLock;
	public static Map<Integer, Account> accountMap = new ConcurrentHashMap<>();

	public AccountServiceImpl() {
		accountLock = new ReentrantLock();
	}

	public boolean transfer(Integer fromAccountId, Integer toAccountId, BigDecimal value) {
		accountLock.lock();
		Account fromAccount = this.getAccountById(fromAccountId);
		Account toAccount = this.getAccountById(toAccountId);
		try {
			checkIfBalanceIsSufficient(fromAccount, value);
			fromAccount.withdraw(value);
			toAccount.deposit(value);
			return true;
		} finally {
			accountLock.unlock();
		}
	}

	@Override
	public Integer createAccount() {
		accountLock.lock();
		try {
			Account account = Account.builder().id(getNextId()).balance(BigDecimal.ZERO).build();
			this.insertAccount(account);
			return account.getId();
		} finally {
			accountLock.unlock();
		}
	}

	private void checkIfBalanceIsSufficient(Account account, BigDecimal amount) {
		if (account.getBalance().compareTo(amount) < 0) {
			throw new AccountOperationException("Account does not have sufficient balance to perform operation");
		}
	}


	private Integer getNextId() {
		return accountMap.size() > 0 ? accountMap.entrySet().stream().max(Entry.comparingByKey()).get().getKey() + 1 : 1000;
	}

	@Override
	public boolean deleteAccount(Integer accountId) {
		this.deleteAccountById(accountId);
		return true;
	}

	@Override
	public BigDecimal getAccountBalance(Integer accountId) {
		accountLock.lock();
		try {
			return this.getAccountById(accountId).getBalance();
		} finally {
			accountLock.unlock();
		}
	}

	@Override
	public BigDecimal deposit(Integer accountId, BigDecimal amount) {
		accountLock.lock();
		try {
			Account account = getAccountById(accountId).deposit(amount);
			accountMap.put(accountId, account);
			return accountMap.get(accountId).getBalance();
		} finally {
			accountLock.unlock();
		}
	}

	@Override
	public BigDecimal withdraw(Integer accountId, BigDecimal amount) {
		accountLock.lock();
		try {
			Account account = getAccountById(accountId);
			checkIfBalanceIsSufficient(account, amount);
			account.withdraw(amount);
			accountMap.put(accountId, account);
			return account.getBalance();
		} finally {
			accountLock.unlock();
		}
	}

	private Account getAccountById(Integer accountId) {
		return Optional.ofNullable(accountMap.get(accountId)).orElseThrow();
	}

	private void insertAccount(Account account) {
		accountMap.put(account.getId(), account);
	}

	private void deleteAccountById(Integer accountId) {
		accountMap.remove(accountId);
	}
}
