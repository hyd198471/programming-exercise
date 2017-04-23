package com.booxware.test.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.booxware.test.Account;
import com.booxware.test.AccountServiceException;
import com.booxware.test.AccountServiceInterface;
import com.booxware.test.PersistenceInterface;

public class AccountServiceImpl implements AccountServiceInterface {

    private PersistenceInterface persistence = new PersistenceImpl();
    private PasswordService passwordService = new PasswordService();

    private static final Logger LOG = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    public Account login(String username, String password) throws AccountServiceException {
        Account account = validateAccount(username);
        if(passwordService.isExpectedPassword(password, account.getSalt(), account.getEncryptedPassword())) {
            account.setLastLogin(new Date());
            return account;
        } else {
            throw new AccountServiceException(String.format("Access Denied. Incorrect password for %s", username));
        }
    }

    @Override
    public Account register(String username, String email, String password) throws AccountServiceException {
        Account account = persistence.findByName(username);
        if(account != null) {
            throw new AccountServiceException(String.format("Account for %s was found! Please user another username!", username));
        }
        String salt = passwordService.getNextSalt();
        account = new Account(IdGenerator.INSTANCE.getNextId(),
                username,
                passwordService.encryptPassword(password, salt),
                salt,
                email);
        account.setLastLogin(new Date());
        persistence.save(account);
        return login(username,password);
    }

    @Override
    public void deleteAccount(String username) throws AccountServiceException {
        Account account = validateAccount(username);
        persistence.delete(account);
    }

    @Override
    public boolean hasLoggedInSince(String username, Date date) throws AccountServiceException {
        Account account = validateAccount(username);
        return date == null || date.before(account.getLastLogin());
    }

    private Account validateAccount(String username) throws AccountServiceException {
        Account account = persistence.findByName(username);
        if (account == null) {
            throw new AccountServiceException(String.format("Account for username = %s was not found, so cannot be deleted!", username));
        }
        return account;
    }


}
