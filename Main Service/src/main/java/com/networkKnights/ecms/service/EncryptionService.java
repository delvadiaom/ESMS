package com.networkKnights.ecms.service;
public interface EncryptionService {
    public String encrypt(String data);
    public String decrypt(String data);
}