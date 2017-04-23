package com.booxware.test;

/**
 * Persistence can be very simple, for example an in memory hash map.
 * 
 */
public interface PersistenceInterface {

	public void save(Account account);

	public Account findById(int id) throws AccountServiceException;

	public Account findByName(String name) throws AccountServiceException;

	public void delete(Account account) throws AccountServiceException;

}
