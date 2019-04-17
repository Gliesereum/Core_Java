package com.gliesereum.notification.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
public class FirebaseAdminConfiguration {

	@Bean
	public FirebaseApp firebaseApp() throws Exception {
		File file = ResourceUtils.getFile("classpath:google-services.json");
		String fileContent = new String(Files.readAllBytes(file.toPath()));
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				fileContent.getBytes(Charset.forName("UTF-8")));
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setCredentials(GoogleCredentials.fromStream(byteArrayInputStream))
				.setProjectId("karma-c9d57").build();
		return FirebaseApp.initializeApp(options, "karmaFirebase");
	}

}
