package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpResponseCodeEntity;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpResponseCodeDto;
import com.gliesereum.share.common.service.DefaultService;

/**
 * @author vitalij
 * @since 9/5/19
 */
public interface WfpResponseCodeService extends DefaultService<WfpResponseCodeDto, WfpResponseCodeEntity> {

    WfpResponseCodeDto getByCode(Integer code);

}