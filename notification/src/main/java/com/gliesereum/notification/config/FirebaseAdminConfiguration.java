package com.gliesereum.notification.config;

import com.gliesereum.notification.config.properties.FirebaseProperties;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
public class FirebaseAdminConfiguration {

    @Bean
    public FirebaseApp firebaseKarmaApp(FirebaseProperties firebaseProperties) throws IOException {
        File file = ResourceUtils.getFile(firebaseProperties.getCredentialPath());
        FirebaseOptions options;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(fileInputStream))
                    .setProjectId(firebaseProperties.getAppName()).build();
        }
        return FirebaseApp.initializeApp(options, "firebaseKarmaApp");
    }

    @Bean
    public FirebaseMessaging firebaseMessagingKarma(FirebaseApp firebaseKarmaApp) {
        return FirebaseMessaging.getInstance(firebaseKarmaApp);
    }
}
