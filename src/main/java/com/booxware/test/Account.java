package com.booxware.test;

import java.io.Serializable;
import java.util.Date;

/**
 * The encryption can be very simple, we don't put much emphasis on the
 * encryption algorithm.
 */
public class Account implements Serializable {

	private int id;

	private String username;

	private byte[] encryptedPassword;

	private String salt;

	private String email;

	private Date lastLogin;

	public Account(int id, String username, byte[] encryptedPassword, String salt, String email) {
		this.id = id;
		this.username = username;
		this.encryptedPassword = encryptedPassword;
		this.salt = salt;
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(byte[] encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
}
