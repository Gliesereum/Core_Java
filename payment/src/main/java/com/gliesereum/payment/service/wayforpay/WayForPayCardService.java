package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WayForPayCardEntity;
import com.gliesereum.share.common.model.dto.payment.UserCardDto;
import com.gliesereum.share.common.model.dto.payment.WayForPayCardDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WayForPayCardService extends DefaultService<WayForPayCardDto, WayForPayCardEntity> {

    List<UserCardDto> getMyCards();

    List<UserCardDto> doFavorite(UUID idCard);
}
