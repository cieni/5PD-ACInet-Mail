package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BD {
    
    private static EntityManagerFactory instance;
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (instance == null) {
            instance = Persistence.createEntityManagerFactory("WebmailPU");
        }
            
        return instance;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    
}
