package com.gliesereum.karma.service.karma.impl;

import com.gliesereum.karma.model.entity.karma.KarmaEntity;
import com.gliesereum.karma.model.repository.jpa.karma.KarmaRepository;
import com.gliesereum.karma.service.karma.KarmaService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.karma.KarmaDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Service
public class KarmaServiceImpl extends DefaultServiceImpl<KarmaDto, KarmaEntity> implements KarmaService {

    private static final Class<KarmaDto> DTO_CLASS = KarmaDto.class;
    private static final Class<KarmaEntity> ENTITY_CLASS = KarmaEntity.class;

    @Autowired
    public KarmaServiceImpl(KarmaRepository karmaRepository, DefaultConverter defaultConverter) {
        super(karmaRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }
}
