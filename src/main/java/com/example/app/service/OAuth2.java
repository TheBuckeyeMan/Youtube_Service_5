package com.example.app.service;

import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;
import com.google.api.services.youtube.YouTube;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OAuth2 {
    private static final Logger log = LoggerFactory.getLogger(OAuth2.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    /**
     * Authenticate using the pre-generated refresh token.
     *
     * @param refreshToken The refresh token passed from environment variables.
     * @return An authenticated YouTube client.
     * @throws Exception if authentication fails.
     */
    public YouTube authenticate(String Token, String clientId, String clientSecret, String tokenUri) throws Exception{
        log.info("Initialization of Authentication with YouTube via OAuth2");

        UserCredentials credentials = UserCredentials.newBuilder()
                        .setClientId(clientId)
                        .setClientSecret(clientSecret)
                        .setRefreshToken(Token)
                        .setTokenServerUri(new java.net.URI(tokenUri))
                        .build();
            
        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, new HttpCredentialsAdapter(credentials)).setApplicationName("Youtube Uploader").build();
    }
}
