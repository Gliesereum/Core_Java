package com.gliesereum.notification.service.firebase;

import com.google.firebase.messaging.TopicManagementResponse;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface FirebaseService {

    TopicManagementResponse subscribeToTopic(String registrationToken, String topicDestination);

    TopicManagementResponse subscribeToTopic(String registrationToken, String subscribeDestination, UUID subscribeId);

    void sendNotificationToTopic(String topic, String title, String body, UUID recordId);
}
