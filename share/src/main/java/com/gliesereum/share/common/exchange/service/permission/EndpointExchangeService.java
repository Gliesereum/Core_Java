package com.gliesereum.share.common.exchange.service.permission;

import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointPackDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface EndpointExchangeService {

    List<EndpointDto> createPack(EndpointPackDto endpointPack);
}
