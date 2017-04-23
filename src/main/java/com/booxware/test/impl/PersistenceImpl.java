package com.booxware.test.impl;

import java.util.HashMap;
import java.util.Map;

import com.booxware.test.Account;
import com.booxware.test.AccountServiceException;
import com.booxware.test.PersistenceInterface;

public class PersistenceImpl implements PersistenceInterface {

    private Map<Integer, Account> idMap = new HashMap<>();
    private Map<String, Account> usernameMap = new HashMap<>();

    @Override
    public void save(Account account) {
        idMap.put(account.getId(), account);
        usernameMap.put(account.getUsername(), account);
    }

    @Override
    public Account findById(int id) throws AccountServiceException {
        if(idMap.containsKey(id)) {
            return idMap.get(id);
        }
        throw new AccountServiceException(String.format("Account with id %d is not found", id));
    }

    @Override
    public Account findByName(String name) throws AccountServiceException {
        if(usernameMap.containsKey(name)) {
            return usernameMap.get(name);
        }
        throw new AccountServiceException(String.format("Account with username %s is not found", name));

    }

    @Override
    public void delete(Account account) throws AccountServiceException {
        if(idMap.containsKey(account.getId())) {
            idMap.remove(account.getId());
        }

        if(usernameMap.containsKey(account.getUsername())) {
            usernameMap.remove(account.getUsername());
        }

    }
    /* test purpose only*/
    public Map<Integer, Account> getIdMap() {
        return idMap;
    }

    public Map<String, Account> getUsernameMap() {
        return usernameMap;
    }
}
