package com.gliesereum.lendinggallery.model.repository.jpa.information;

import com.gliesereum.lendinggallery.model.entity.information.InformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface InformationRepository extends JpaRepository<InformationEntity, UUID> {

    List<InformationEntity> findByTagOrderByIndex(String tag);

    List<InformationEntity> findByTagAndIsoCodeOrderByIndex(String tag, String isoCode);

    List<InformationEntity> findByOrderByIndex();
}