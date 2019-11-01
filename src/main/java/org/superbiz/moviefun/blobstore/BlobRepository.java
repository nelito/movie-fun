package org.superbiz.moviefun.blobstore;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;

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
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaDelete<BlobEntity> query = builder.createCriteriaDelete(BlobEntity.class);
        query.from(BlobEntity.class);
        entityManager.createQuery(query).executeUpdate();
    }
}
