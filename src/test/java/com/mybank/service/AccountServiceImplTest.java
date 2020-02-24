package com.mybank.service;

import com.mybank.exception.AccountOperationException;
import com.mybank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static java.math.BigDecimal.TEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountServiceImplTest {

	@InjectMocks
	private AccountServiceImpl underTest;

	@BeforeEach
	void setUp() {

		MockitoAnnotations.initMocks(this);

		Account fromAccount = Account.builder().id(1).balance(TEN).build();
		Account toAccount = Account.builder().id(2).balance(TEN).build();
		AccountServiceImpl.accountMap.put(1, fromAccount);
		AccountServiceImpl.accountMap.put(2, toAccount);
	}

	@Test
	void testCreateAccount() {
		Integer accountId = underTest.createAccount();
		assertNotNull(accountId);
		assertEquals(3, accountId);
		assertEquals(3, AccountServiceImpl.accountMap.size());
	}

	@Test
	void testDeleteAccount() {
		assertTrue(underTest.deleteAccount(1));
	}

	@Test
	void testGetBalance() {
		assertEquals(TEN, underTest.getAccountBalance(1));
	}

	@Test
	public void testTransfer_MoneyBetweenAccounts() {
		assertTrue(underTest.transfer(1, 2, TEN), "Transfer failed!");
	}

	@Test
	public void testTransfer_WhenNotEnoughBalance() {
		AccountOperationException exception = assertThrows(AccountOperationException.class, () -> underTest.transfer(1, 2, BigDecimal.valueOf(21)),
				"Expected exception not thrown");
		assertEquals("Account does not have sufficient balance to perform operation", exception.getMessage());
	}

	@Test
	void testGetBalance_WhenAccountIsNotPresent() {
		assertThrows(NoSuchElementException.class, () -> underTest.getAccountBalance(5), "Expected Exception not thrown!");
	}
}