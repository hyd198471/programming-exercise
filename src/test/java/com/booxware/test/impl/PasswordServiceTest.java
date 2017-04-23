package com.booxware.test.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.booxware.test.AccountServiceException;

public class PasswordServiceTest {

    private PasswordService passwordService = new PasswordService();

    private String password = "hyd198471";

    private String salt;

    @Test
    public void shouldEncryptPassword() throws AccountServiceException {
        salt = passwordService.getNextSalt();

        byte[] encryptPassword = passwordService.encryptPassword(password, salt);

        assertNotNull(encryptPassword);
    }

    @Test(expected = AccountServiceException.class)
    public void shouldNotEncryptPasswordIfPasswordIsEmpty() throws AccountServiceException {
        salt = passwordService.getNextSalt();
        password="";

       passwordService.encryptPassword(password, salt);

    }

    @Test(expected = AccountServiceException.class)
    public void shouldNotEncryptPasswordIfSaltIsEmpty() throws AccountServiceException {
        salt="";

        passwordService.encryptPassword(password, salt);

    }

    @Test
    public void twoEncryptedPasswordShouldEqual() throws AccountServiceException {
        //given
        salt = passwordService.getNextSalt();
        byte[] encryptedPassword = passwordService.encryptPassword(password, salt);

        //when
        boolean isExpectedPassword = passwordService.isExpectedPassword(password, salt, encryptedPassword);

        //then
        assertTrue(isExpectedPassword);
    }


}