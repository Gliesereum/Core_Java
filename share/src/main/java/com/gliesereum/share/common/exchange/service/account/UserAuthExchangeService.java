package com.gliesereum.share.common.exchange.service.account;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

public interface UserAuthExchangeService {
	
	AuthDto auth(String phone, String code);
}
