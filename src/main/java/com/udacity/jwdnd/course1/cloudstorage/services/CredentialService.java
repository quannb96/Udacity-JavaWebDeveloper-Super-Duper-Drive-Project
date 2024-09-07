package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsByUserid(int userid) {
        return credentialMapper.getCredentialsByUserid(userid);
    }

    public void addCredential(Credential credential, int userId) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        Credential newCredential = new Credential();
        newCredential.setPassword(encryptedPassword);
        newCredential.setUrl(credential.getUrl());
        newCredential.setUsername(credential.getUsername());
        newCredential.setKey(encodedKey);
        newCredential.setUserid(userId);

        credentialMapper.addCredential(newCredential);
    }

    public void updateCredential(Credential credential) {
        Credential selectedCredential = credentialMapper.getCredentialById(credential.getCredentialid());

        credential.setKey(selectedCredential.getKey());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);

        credentialMapper.updateCredential(credential);
    }

    public int deleteCredential(int credentialid) {
        return credentialMapper.deleteCredential(credentialid);
    }

}
