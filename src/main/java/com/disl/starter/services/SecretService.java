package com.disl.starter.services;

import com.disl.starter.entities.Secret;
import com.disl.starter.enums.UserTokenPurpose;
import com.disl.starter.repository.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecretService {

    @Autowired
    private SecretRepository secretRepository;

    public Secret findSecretByUserId(Long userid) {
        return secretRepository.findSecretByUserId(userid).orElse(null);
    }

    public Secret findSecretByUserIdAndUserTokenPurpose(Long userid, UserTokenPurpose tokenPurpose) {
        return secretRepository.findByUserIdAndUserTokenPurpose(userid, tokenPurpose).orElse(null);
    }

    public Secret findByUserTokenAndUserTokenPurpose (String token, UserTokenPurpose purpose){
        return secretRepository.findByUserTokenAndUserTokenPurpose(token, purpose).orElse(null);
    }

    public Secret saveSecret(Secret secret) {
        return this.secretRepository.save(secret);
    }

    public Boolean deleteSecret(Secret secret) {
        try {
            this.secretRepository.delete(secret);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void deleteAllByUserId(long userId){
        secretRepository.deleteAllByUserId(userId);
    }
}