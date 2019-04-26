package com.gliesereum.notification.service.firebase.impl;

import com.gliesereum.notification.service.firebase.FirebaseService;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.NotificationUtil;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class FirebaseServiceImpl implements FirebaseService {

    @Autowired
    private FirebaseMessaging firebaseMessagingKarma;

    @Override
    public TopicManagementResponse subscribeToTopic(String registrationToken, String topicDestination) {
        TopicManagementResponse response = null;
        try {
            response = firebaseMessagingKarma.subscribeToTopic(Arrays.asList(registrationToken), topicDestination);
        } catch (FirebaseMessagingException e) {
            log.warn("Error while subscribe", e);
        }
        return response;
    }

    @Override
    public TopicManagementResponse subscribeToTopic(String registrationToken, String subscribeDestination, UUID subscribeId) {
        TopicManagementResponse response = null;
        if (StringUtils.isNotBlank(registrationToken) && StringUtils.isNotBlank(subscribeDestination) && (subscribeId != null)) {
            String topicDestination = NotificationUtil.routingKey(subscribeDestination, subscribeId);
            response = subscribeToTopic(registrationToken, topicDestination);
        }
        return response;
    }

    @Override
    public void sendNotificationToTopic(String topic, String title, String body, UUID recordId) {
        Message message = Message.builder()
                .putData("recordId", recordId.toString())
                .setNotification(new Notification(title, body))
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH).setNotification(AndroidNotification
                                .builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build())
                .setTopic(topic)
                .build();
        try {
            firebaseMessagingKarma.send(message);
        } catch (FirebaseMessagingException e) {
            log.warn("Error while send message", e);
        }
    }
}
