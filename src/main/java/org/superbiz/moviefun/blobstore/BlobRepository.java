package org.superbiz.moviefun.blobstore;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class BlobRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(BlobEntity blob) {
        entityManager.remove(blob);
        entityManager.persist(blob);
    }

    public BlobEntity find(String name) {
        return entityManager.find(BlobEntity.class, name);
    }

    @Transactional
    public  void deleteAll() {
        Query q1 = entityManager.createQuery("DELETE FROM blob");
        q1.executeUpdate();
    }
}
