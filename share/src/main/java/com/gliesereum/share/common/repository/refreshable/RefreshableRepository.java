package com.gliesereum.share.common.repository.refreshable;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface RefreshableRepository {

    void refresh(Object entity);
}
