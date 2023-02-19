package com.ashu.banking.service.userserviceimpl;

import static com.ashu.banking.util.AppConstants.EXTERNAL_AUDIT_URI_DEPOSIT;
import static com.ashu.banking.util.AppConstants.EXTERNAL_AUDIT_URI_WITHDRAW;
import static com.ashu.banking.util.AppConstants.EXTERNAL_AUDIT_URL;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ashu.banking.dao.PrimaryAccountDao;
import com.ashu.banking.dao.SavingsAccountDao;
import com.ashu.banking.domain.PrimaryAccount;
import com.ashu.banking.domain.PrimaryTransaction;
import com.ashu.banking.domain.SavingsAccount;
import com.ashu.banking.domain.SavingsTransaction;
import com.ashu.banking.domain.User;
import com.ashu.banking.service.AccountService;
import com.ashu.banking.service.TransactionService;
import com.ashu.banking.service.UserService;
@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger L = LogManager.getLogger(AccountServiceImpl.class);

	private static int nextAccountNumber = 11223145;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private UserService userService;
    
    @Autowired
    private TransactionService transactionService;

    public PrimaryAccount createPrimaryAccount() {
    		int intAccNum = 0;
    		boolean blnAccountExists = true;
    		List<PrimaryAccount> lstFindByAccountNumber;
    		while (blnAccountExists) {
    			intAccNum = accountGen();
        		lstFindByAccountNumber = primaryAccountDao.findByAccountNumber(intAccNum);
    			if (CollectionUtils.isEmpty(lstFindByAccountNumber))
    				blnAccountExists = false;
    		}

        PrimaryAccount primaryAccount = new PrimaryAccount();
        primaryAccount.setAccountBalance(new BigDecimal("0.0"));
        primaryAccount.setAccountNumber(intAccNum);

        primaryAccountDao.save(primaryAccount);

        L.debug("63 : Primary account number = {}", primaryAccount.getAccountNumber());
        lstFindByAccountNumber = primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());
        if (!CollectionUtils.isEmpty(lstFindByAccountNumber))
        		return lstFindByAccountNumber.get(0);
        else
        		return null;
    }

    public SavingsAccount createSavingsAccount() {
		int intAccNum = 0;
		boolean blnAccountExists = true;
		List<SavingsAccount> lstFindByAccountNumber;
		while (blnAccountExists) {
			intAccNum = accountGen();
    			lstFindByAccountNumber = savingsAccountDao.findByAccountNumber(intAccNum);
			if (CollectionUtils.isEmpty(lstFindByAccountNumber))
				blnAccountExists = false;
		}

        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal("0.0"));
        savingsAccount.setAccountNumber(intAccNum);

        savingsAccountDao.save(savingsAccount);

        L.debug("88 : Savings account number = {}", savingsAccount.getAccountNumber());
        lstFindByAccountNumber = savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
        if (!CollectionUtils.isEmpty(lstFindByAccountNumber))
        		return lstFindByAccountNumber.get(0);
        else
        		return null;
    }
    
    public void deposit(String accountType, double amount, Principal principal) {
    	L.debug("Start : AccountServiceImpl.deposit(...) : accountType = {}, amount = {}", accountType, amount);
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);

           // callBofaDeposit(amount);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);

           // callBofaDeposit(amount);
        }
        L.debug("End : AccountServiceImpl.deposit(...) : accountType = {}, amount = {}", accountType, amount);
    }

    public void withdraw(String accountType, double amount, Principal principal) {
    	L.debug("Start : AccountServiceImpl.withdraw(...) : accountType = {}, amount = {}", accountType, amount);
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
           // callBofaWithdraw(amount);
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
           // callBofaWithdraw(amount);
        }
        L.debug("End : AccountServiceImpl.withdraw(...) : accountType = {}, amount = {}", accountType, amount);
    }

    private int accountGen() {
        return ++nextAccountNumber;
    }

	private void callBofaDeposit(double amount) {
		L.debug("Start : AccountServiceImpl.callBofaDeposit() : amount = {}", amount);
		try {
			RestTemplate rest = new RestTemplate();
			String strResponse = rest.getForObject(EXTERNAL_AUDIT_URL + EXTERNAL_AUDIT_URI_DEPOSIT + "/" + amount, String.class);
			L.info("Bofa online deposit response: " + strResponse);
		} catch (RestClientException e) {
			L.error("Rest call to Bofa-online deposit service failed : RestClientException e = {}", e);
			throw e;
		}
		L.debug("End : AccountServiceImpl.callBofaDeposit() : amount = {}", amount);
	}

	private void callBofaWithdraw(double amount) {
		L.debug("Start : AccountServiceImpl.callBofaWithdraw(), amount = {}", amount);
		try {
			RestTemplate rest = new RestTemplate();
			String str = rest.getForObject(EXTERNAL_AUDIT_URL + EXTERNAL_AUDIT_URI_WITHDRAW + "/" + amount, String.class);
			L.info("Bofa online withdraw response: " + str);
		} catch (RestClientException e) {
			L.error("Rest call to Bofa-online withdraw service failed : RestClientException e = {}", e);
			throw e;
		}
		L.debug("End : AccountServiceImpl.callBofaWithdraw() : amount = {}", amount);
	}
}
