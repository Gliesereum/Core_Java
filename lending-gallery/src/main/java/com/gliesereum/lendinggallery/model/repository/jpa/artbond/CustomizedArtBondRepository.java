package com.gliesereum.lendinggallery.model.repository.jpa.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.query.lendinggallery.artbond.ArtBondQuery;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CustomizedArtBondRepository {

    List<ArtBondEntity> search(ArtBondQuery query);
}
