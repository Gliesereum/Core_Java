package com.gliesereum.notification.service.firebase;

import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.google.firebase.messaging.TopicManagementResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface FirebaseService {

    TopicManagementResponse subscribeToTopic(String registrationToken, String topicDestination);

    TopicManagementResponse subscribeToTopic(String registrationToken, String subscribeDestination, UUID subscribeId);

    TopicManagementResponse unsubscribeFromTopic(String registrationToken, String subscribeDestination, UUID subscribeId);

    void sendNotificationToTopic(String topic, String title, String body, UUID objectId, SubscribeDestination subscribeDestination);

    void sendNotificationToDevice(String registrationToken, String title, String body, Map<String, String> data);

    void sendNotificationToDevices(List<String> registrationTokens, String title, String body, Map<String, String> data);
}
