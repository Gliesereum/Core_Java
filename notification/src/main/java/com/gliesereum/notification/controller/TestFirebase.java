package com.gliesereum.notification.controller;

import com.gliesereum.share.common.model.response.MapResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
public class TestFirebase {

	private String token = "e1aDWMlEFXo:APA91bGQLBhvwCLSRTeWQahKoiQ4DbsScLXyQgc_X97ubrpeLf1YEnSqrbop3MW26LioHeOP0YL_fZ6j1qGLxBx75zY4EvWyAc9ZIrhPR7c3KZoH-lpK-ax_oHGsKSA2FgMq8tLjFDT8";

	@Autowired
	private FirebaseApp firebaseApp;

	private SseEmitter emitter;

	@GetMapping("/test")
	public MapResponse test() throws FirebaseMessagingException {
		FirebaseMessaging instance = FirebaseMessaging.getInstance(firebaseApp);
		Message message = Message.builder().putData("testField1", "value1").putData("testField2", "value2").setNotification(new Notification("def not", "def body"))
				.setAndroidConfig(AndroidConfig.builder().setCollapseKey("key")
						.setPriority(AndroidConfig.Priority.HIGH).setTtl(10000l).setNotification(AndroidNotification.builder().setTag("#tag")
								.setTitle("$GOOG title")
								.setIcon("@drawable/ic_assignment_black_24dp.xml")
								.setClickAction("SHOW_DETAILS").setBody("BODY dsff DASF")
								.setColor("#203080").build())
						.build())
				.setToken(token).build();
		String send = instance.send(message);
		return new MapResponse(send);
	}

}
