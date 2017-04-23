package com.booxware.test.impl;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.keygen.KeyGenerators;

import com.booxware.test.AccountServiceException;

public class PasswordService {

    private static final String ALGORITHM_NAME = "PBKDF2WithHmacSHA1";
    private static final int ITERATION_COUNT = 1024;
    private static final int KEY_LENGTH = 160;

    public boolean isExpectedPassword(String password, String salt, byte[] encryptedPassword) throws AccountServiceException {
        return Arrays.equals(encryptedPassword, encryptPassword(password, salt));
    }

    public byte[] encryptPassword(String password, String salt) throws AccountServiceException {
        if(StringUtils.isEmpty(password)) {
            throw new AccountServiceException("password is empty");
        }
        if(StringUtils.isEmpty(salt)) {
            throw new AccountServiceException("salt is empty");
        }
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), ITERATION_COUNT, KEY_LENGTH);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM_NAME);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AccountServiceException("Error while hashing a password: " + e.getMessage());
        } finally {
            spec.clearPassword();
        }
    }

    public String getNextSalt() {
        return KeyGenerators.string().generateKey();
    }
}
