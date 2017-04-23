package com.booxware.test.impl;

public enum IdGenerator {
    INSTANCE;

    private int id;

    public int getNextId() {
        return  ++id;
    }
}
