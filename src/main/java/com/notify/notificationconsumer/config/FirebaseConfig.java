package com.notify.notificationconsumer.config;

import com.google.auth.oauth2.GoogleCredentials; 
import com.google.firebase.FirebaseApp;         
import com.google.firebase.FirebaseOptions;     
import com.google.firebase.messaging.FirebaseMessaging; 
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.context.annotation.Bean;       
import org.springframework.context.annotation.Configuration;  
import org.springframework.core.io.Resource;  
import java.io.IOException;  

@Configuration
public class FirebaseConfig {
    @Value("${firebase.config-path}")
    private Resource firebaseConfig;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
            .fromStream(firebaseConfig.getInputStream());
        
        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .build();
        
        FirebaseApp.initializeApp(options);
        return FirebaseMessaging.getInstance();
    }
}
