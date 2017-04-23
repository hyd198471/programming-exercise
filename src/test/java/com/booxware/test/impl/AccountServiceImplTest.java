package com.booxware.test.impl;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.booxware.test.Account;
import com.booxware.test.AccountServiceException;
import com.booxware.test.PersistenceInterface;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private PersistenceInterface persistence;
    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private String username= "test";

    private String password = "test1";

    private String email = "test1@test.com";

    private String salt = "fdaf3erafdasg";

    private Date date = new Date(1492867957);

    private String encryptedPassword = "asjfkdlsafdf#j,mncvoa";

    private Account account =new Account(1,
            username,
            encryptedPassword.getBytes(),
            salt,
            email);;

    private byte[] encryptedPasswordBytes = encryptedPassword.getBytes();


    @Test(expected = AccountServiceException.class)
    public void shouldNotLoginIfAccountNotExist() throws Exception {
        //given
        when(persistence.findByName(eq(username))).thenReturn(null);

        //when
        accountService.login(username, password);

    }

    @Test(expected = AccountServiceException.class)
    public void shouldNotLoginIfPasswordNotMatch() throws Exception {
        //given
        when(persistence.findByName(eq(username))).thenReturn(account);
        when(passwordService.isExpectedPassword(anyString(), anyString(), any(byte[].class))).thenReturn(false);

        //when
        accountService.login(username, password);

    }

    @Test
    public void shouldLogin() throws Exception {
        //given
        when(persistence.findByName(eq(username))).thenReturn(account);
        when(passwordService.isExpectedPassword(anyString(), anyString(), any(byte[].class))).thenReturn(true);

        //when
        Account login = accountService.login(username, password);

        //then
        Assert.assertEquals(new Date(), login.getLastLogin());

    }

    @Test(expected = AccountServiceException.class)
    public void shouldNotRegisterIfAccountExists() throws Exception {
        //given
        when(persistence.findByName(eq(username))).thenReturn(account);

        //when
        accountService.register(username, email, password);

    }

    @Test
    public void shouldRegister() throws Exception {
        //given
        when(persistence.findByName(eq(username))).thenReturn(null, account);
        when(passwordService.getNextSalt()).thenReturn(salt);
        when(passwordService.encryptPassword(eq(password), eq(salt))).thenReturn(encryptedPasswordBytes);
        when(passwordService.isExpectedPassword(eq(password), eq(salt), eq(encryptedPassword.getBytes()))).thenReturn(true);

        //when
        accountService.register(username, email, password);

        // then
        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);
        verify(persistence).save(argumentCaptor.capture());

        Account actualAccount = argumentCaptor.getValue();
        Assert.assertThat( actualAccount.getUsername(), is(account.getUsername()));
        Assert.assertThat( actualAccount.getEmail(), is(account.getEmail()));
        Assert.assertThat( actualAccount.getEncryptedPassword(), is(account.getEncryptedPassword()));
    }

    @Test(expected = AccountServiceException.class)
    public void shouldNotDeleteAccountIfAccountNotExist() throws Exception {
        when(persistence.findByName(eq(username))).thenReturn(null);

        accountService.deleteAccount(username);
    }

    @Test(expected = AccountServiceException.class)
    public void accountNotLogin() throws Exception {
        when(persistence.findByName(eq(username))).thenReturn(null);

        accountService.hasLoggedInSince(username,date);
    }

    @Test
    public void shouldLoginAccountSinceNow() throws Exception {
        when(persistence.findByName(eq(username))).thenReturn(account);

        boolean hasLogged = accountService.hasLoggedInSince(username, null);

        Assert.assertTrue(hasLogged);
    }

    @Test
    public void shouldLoginAccountSinceSpecificDate() throws Exception {
        when(persistence.findByName(eq(username))).thenReturn(account);
        account.setLastLogin(new Date());

        boolean hasLogged = accountService.hasLoggedInSince(username, date);

        Assert.assertTrue(hasLogged);
    }

    @Test
    public void shouldDeleteAccount() throws Exception {
        when(persistence.findByName(eq(username))).thenReturn(account);

        accountService.deleteAccount(username);

        verify(persistence).delete(account);
    }

}