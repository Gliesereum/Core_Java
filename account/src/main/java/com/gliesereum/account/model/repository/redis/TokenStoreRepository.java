package com.gliesereum.account.model.repository.redis;

import com.gliesereum.account.model.domain.TokenStoreDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Repository
public interface TokenStoreRepository extends CrudRepository<TokenStoreDomain, String> {
}
