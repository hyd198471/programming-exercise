package com.booxware.test.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.booxware.test.Account;
import com.booxware.test.AccountServiceException;

public class PersistenceImplTest {

    private PersistenceImpl persistence = new PersistenceImpl();

    private String username= "test";

    private String password = "test1";

    private String email = "test1@test.com";

    private String salt = "fdaf3erafdasg";

    private String encryptedPassword = "asjfkdlsafdf#j,mncvoa";

    private int id = 1;

    private byte[] encryptedPasswordBytes = encryptedPassword.getBytes();

    private Account account =new Account(id,
            username,
            encryptedPassword.getBytes(),
            salt,
            email);





    @Test
    public void shouldSaveAccount() {
        // when
        persistence.save(account);

        //then
        assertTrue(persistence.getIdMap().containsValue(account));
        assertTrue(persistence.getUsernameMap().containsValue(account));
        assertTrue(persistence.getIdMap().containsKey(account.getId()));
        assertTrue(persistence.getUsernameMap().containsKey(account.getUsername()));

    }

    @Test
    public void shouldFindAccountById() throws AccountServiceException {
        //given
        persistence.getIdMap().put(account.getId(), account);

        //when
        Account expectedAccount = persistence.findById(id);


        //then
        assertEquals(expectedAccount, account);
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionWhenAccountIdNotExist() throws AccountServiceException {
        persistence.findById(id);
    }

    @Test
    public void shouldFindAccountByName() throws AccountServiceException {
        //given
        persistence.getUsernameMap().put(account.getUsername(), account);

        //when
        Account expectedAccount = persistence.findByName(username);


        //then
        assertEquals(expectedAccount, account);
    }

    @Test(expected = AccountServiceException.class)
    public void shouldThrowExceptionWhenAccountNameNotExist() throws AccountServiceException {
        persistence.findByName(username);
    }

    @Test
    public void shouldDeleteAccount() throws AccountServiceException {
        //given
        persistence.getIdMap().put(account.getId(), account);
        persistence.getUsernameMap().put(account.getUsername(), account);

        //when
        persistence.delete(account);


        //then
        assertTrue(persistence.getIdMap().isEmpty());
        assertTrue(persistence.getUsernameMap().isEmpty());
    }

}