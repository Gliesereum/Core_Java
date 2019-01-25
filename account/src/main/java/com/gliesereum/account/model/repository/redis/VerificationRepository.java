package com.gliesereum.account.model.repository.redis;

import com.gliesereum.account.model.domain.VerificationDomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author vitalij
 */
@Repository
public interface VerificationRepository extends CrudRepository<VerificationDomain, String> {
}
