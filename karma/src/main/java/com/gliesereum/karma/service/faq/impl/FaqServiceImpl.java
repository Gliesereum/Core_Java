package com.gliesereum.karma.service.faq.impl;

import com.gliesereum.karma.model.entity.faq.FaqEntity;
import com.gliesereum.karma.model.repository.jpa.faq.FaqRepository;
import com.gliesereum.karma.service.faq.FaqService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.faq.FaqDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FaqServiceImpl extends DefaultServiceImpl<FaqDto, FaqEntity> implements FaqService {

    private static final Class<FaqDto> DTO_CLASS = FaqDto.class;
    private static final Class<FaqEntity> ENTITY_CLASS = FaqEntity.class;

    private final FaqRepository faqRepository;

    @Autowired
    public FaqServiceImpl(FaqRepository faqRepository, DefaultConverter defaultConverter) {
        super(faqRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.faqRepository = faqRepository;
    }

    @Override
    public List<FaqDto> getAll() {
        List<FaqEntity> entities = faqRepository.findByOrderByIndex();
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public FaqDto create(FaqDto dto) {
        return super.create(dto);
    }

    @Override
    @Transactional
    public FaqDto update(FaqDto dto) {
        return super.update(dto);
    }

    @Transactional
    public void delete(UUID id) {
        super.delete(id);
    }
}
