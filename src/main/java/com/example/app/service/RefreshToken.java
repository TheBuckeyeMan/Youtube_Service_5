package com.example.app.service;

import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import javax.management.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.oauth2.UserCredentials;

//The following Refreshes the access token using the long lived token, ensuring that it works on a serverless archetype
@Service
public class RefreshToken {
    private static final Logger log = LoggerFactory.getLogger(RefreshToken.class);
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private S3LoggingService s3LoggingService;

    public RefreshToken(S3LoggingService s3LoggingService){
        this.s3LoggingService = s3LoggingService;
    }


    public String getRefreshToken(String clientId, String clientSecret, String authUri, String tokenUri, String LongLivedToken) throws Exception{
        log.info("Attempting to get Refresh Token...");

        if(LongLivedToken == null){
            log.error("LongLivedAccessToken not present, please generate a long lived access token and store in an environment variable" );
            s3LoggingService.logMessageToS3("Error: LongLivedToken does not exist. Line 30 on RefreshToken.java: " + LocalDate.now() + " On: youtube-service-5" + ",");
        }   
        try{
            UserCredentials credentials = UserCredentials.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(LongLivedToken)
                .setTokenServerUri(new java.net.URI(tokenUri))
                .build();

                String newToken = credentials.refreshAccessToken().getTokenValue();
                if(newToken == null){
                    log.error("Error: The Refresh Token returned null: Error Line 48 of RefreshToken.java");
                    s3LoggingService.logMessageToS3("Error: The Refresh Token returned null: Error Line 48 of RefreshToken.java: " + LocalDate.now() + " On: youtube-service-5" + ",");
                    throw new RuntimeException("Failed to refresh access token. Token is null or empty.");
                }
                log.info("New RefreshToken has successfully been retrieved: " + newToken);
                return newToken;

        } catch (Exception e) {
            log.error("Error on line 48", e);
            throw e;
        }
    }
}