package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BD {
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("WebmailPU");
        }
            
        return emf;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    
    public static EntityManager getEntityManager() {
        if (em == null) {
            em = createEntityManager();
        }
        
        return em;
    }
    
}
