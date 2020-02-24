package com.mybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybank.exception.AccountOperationException;
import com.mybank.model.TransferPayload;
import com.mybank.service.AccountService;
import com.mybank.service.AccountServiceImpl;

import java.math.BigDecimal;

import static java.lang.Integer.valueOf;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

public class BankingApp {
	private static final int HTTP_BAD_REQUEST = 400;
	private static final int HTTP_CREATED = 201;
	private static final int HTTP_OK = 200;
	private static final int HTTP_INTERNAL_SERVER_ERROR = 500;
	private static final String NEW_BALANCE = "New Balance : ";

	private static AccountService accountService = new AccountServiceImpl();

	public static void main(String[] args) {

		get("/api/v1.0/account/:id", (request, response) -> {

			try {
				BigDecimal result = accountService.getAccountBalance(valueOf(request.params(":id")));
				response.status(HTTP_OK);
				return String.format("Balance : %s", result);

			} catch (Exception e) {
				response.status(HTTP_BAD_REQUEST);
				return e.getMessage();
			}
		});

		post("/api/v1.0/account", (request, response) -> {
			int id = accountService.createAccount();
			response.status(HTTP_CREATED);
			return String.format("Account with id %s created successfully.", id);
		});

		delete("/api/v1.0/account/:id", (request, response) -> {
			try {
				accountService.deleteAccount(valueOf(request.params(":id")));
				response.status(HTTP_OK);
				return "SUCCESS";
			} catch (Exception e) {
				response.status(HTTP_BAD_REQUEST);
				return e.getMessage();
			}
		});

		put("/api/v1.0/account/:id/withdraw/:amount", (request, response) -> {
			try {
				final BigDecimal result = accountService.withdraw(valueOf(request.params(":id")), new BigDecimal(request.params(":amount")));
				response.status(HTTP_OK);
				return String.format("%s%s", NEW_BALANCE, result);
			} catch (Exception e) {
				response.status(HTTP_BAD_REQUEST);
				return e.getMessage();
			}
		});

		put("/api/v1.0/account/:id/deposit/:amount", (request, response) -> {
			try {
				final BigDecimal result = accountService.deposit(valueOf(request.params(":id")), new BigDecimal(request.params(":amount")));
				response.status(HTTP_OK);
				return String.format("%s%s", NEW_BALANCE, result);
			} catch (Exception e) {
				response.status(HTTP_BAD_REQUEST);
				return e.getMessage();
			}
		});

		put("/api/v1.0/account/:id/transfer", (request, response) -> {
			try {
				ObjectMapper mapper = new ObjectMapper();
				TransferPayload payload = mapper.readValue(request.body(), TransferPayload.class);

				if (!payload.isValid()) {
					response.status(HTTP_BAD_REQUEST);
					return "";
				}

				boolean result = accountService.transfer(valueOf(request.params(":id")), payload.getToAccount(), payload.getAmount());
				if (result) {
					response.status(HTTP_OK);
					return "SUCCESS";
				} else {
					response.status(HTTP_INTERNAL_SERVER_ERROR);
					return "ERROR";
				}

			} catch (AccountOperationException e) {
				response.status(HTTP_BAD_REQUEST);
				return e.getMessage();
			} catch (Exception e) {
				response.status(HTTP_INTERNAL_SERVER_ERROR);
				return e.getMessage();
			}
		});
	}
}
