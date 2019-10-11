package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpCardEntity;
import com.gliesereum.share.common.model.dto.payment.payment.UserCardDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpCardDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WfpCardService extends DefaultService<WfpCardDto, WfpCardEntity> {

    List<UserCardDto> getMyCards();

    List<WfpCardDto> getCardsByOwnerId(UUID ownerId);

    List<UserCardDto> doFavorite(UUID idCard);
}
