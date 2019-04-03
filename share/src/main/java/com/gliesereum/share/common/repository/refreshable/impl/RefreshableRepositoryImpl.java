package com.gliesereum.share.common.repository.refreshable.impl;

import com.gliesereum.share.common.repository.refreshable.RefreshableRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public class RefreshableRepositoryImpl implements RefreshableRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void refresh(Object entity) {
        if (entity != null) {
            entityManager.refresh(entity);
        }
    }
}
