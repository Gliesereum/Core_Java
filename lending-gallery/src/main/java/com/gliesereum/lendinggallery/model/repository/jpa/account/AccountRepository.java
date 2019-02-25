package com.gliesereum.lendinggallery.model.repository.jpa.account;

import com.gliesereum.lendinggallery.model.entity.account.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
}
