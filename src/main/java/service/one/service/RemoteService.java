package service.one.service;

import service.one.model.ImportantData;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface RemoteService {
    ImportantData process(String redisId) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}
