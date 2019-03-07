package com.gliesereum.socket.config.socket;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.auth.AuthExchangeService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.security.model.UserAuthentication;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.socket.facade.SocketSubscribeFacade;
import com.gliesereum.socket.model.subscribe.SubscribeInfo;
import com.gliesereum.socket.model.subscribe.SubscribePoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.gliesereum.share.common.exception.messages.SocketExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Component
@Slf4j
public class WebSocketInboundInterceptor implements ChannelInterceptor {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthExchangeService authService;

    @Autowired
    private Map<String, SocketSubscribeFacade> socketSubscribeFacadeMap;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                processSubscribe(accessor);
            }
            return message;
        } catch (CustomException e) {
            throw new MessageDeliveryException(e.getMessage());
        } catch (Exception e) {
            log.warn("Error while process socket", e);
            throw new MessageDeliveryException(e.getMessage());
        }
    }

    private void processSubscribe(StompHeaderAccessor accessor) {
        processAuthentication(accessor);
        SubscribeInfo subscribeInfo = parseDestination(accessor.getDestination());
        checkSubscribe(subscribeInfo);
    }

    private void checkSubscribe(SubscribeInfo subscribeInfo) {
        if (!subscribeInfo.getSubscribeType().equals("topic")) {
            throw new ClientException(USER_CAN_SUBSCRIBE_ONLY_TO_TOPIC);
        }
        SubscribePoint subscribePoint = SubscribePoint.getByPointName(subscribeInfo.getPointName());
        if (subscribePoint == null) {
            throw new ClientException(SUBSCRIBE_POINT_NOT_FOUND);
        }
        SocketSubscribeFacade socketSubscribeFacade = socketSubscribeFacadeMap.get(subscribePoint.getFacadeName());
        if (socketSubscribeFacade == null) {
            throw new ClientException(SUBSCRIBE_POINT_NOT_AVAILABLE);
        }
        socketSubscribeFacade.validateSubscribe(subscribeInfo);

    }

    private void processAuthentication(StompHeaderAccessor accessor) {
        List<String> authorization = accessor.getNativeHeader(securityProperties.getBearerHeader());
        if (CollectionUtils.isNotEmpty(authorization)) {
            String header = authorization.get(0);
            if (StringUtils.isNotEmpty(header)) {
                String bearerToken = org.apache.commons.lang.StringUtils.removeStart(header, securityProperties.getBearerPrefix());
                if (StringUtils.isNotEmpty(bearerToken)) {
                    bearerToken = bearerToken.trim();
                    AuthDto auth = authService.checkAccessToken(bearerToken);
                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(auth.getUser(), auth.getTokenInfo()));
                    }
                }
            }
        }
    }

    private SubscribeInfo parseDestination(String destination) {
        destination = destination.trim();
        destination = StringUtils.removeStart(destination, "/");
        destination = StringUtils.removeEnd(destination, "/");

        String[] destinationPath = destination.split("/");
        if (destinationPath.length < 2) {
            throw new ClientException(INVALID_SUBSCRIBE_STRUCTURE);
        }
        String subscribeType = destinationPath[0];
        String[] split = destinationPath[1].split("\\.");
        if (split.length < 3) {
            throw new ClientException(INVALID_SUBSCRIBE_STRUCTURE);
        }
        String pointName = split[0];
        String subscribeDestination = split[1];
        String subscribeProperties = split[2];

        if (StringUtils.isAnyEmpty(subscribeType, pointName, subscribeDestination, subscribeProperties)) {
            throw new ClientException(INVALID_SUBSCRIBE_STRUCTURE);
        }

        return new SubscribeInfo(subscribeType, pointName, subscribeDestination, subscribeProperties);
    }
}
