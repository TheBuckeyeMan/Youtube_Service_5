// package com.example.app.service;

// import java.io.StringReader;
// import java.util.Collections;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.stereotype.Service;
// import com.google.api.client.auth.oauth2.Credential;
// import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
// import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
// import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
// import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
// import com.google.api.client.json.gson.GsonFactory;


// @Service
// public class RefreshToken {
//     private static final Logger log = LoggerFactory.getLogger(RefreshToken.class);
//     private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


//     public String getRefreshToken(String clientId, String clientSecret, String authUri, String tokenUri, String redirectUri) throws Exception{
//         log.info("Initialization Process to gather Refresh Token");

//         String clientSecretsJson = String.format(
//                 "{ \"installed\": { \"client_id\": \"%s\", \"client_secret\": \"%s\", \"auth_uri\": \"%s\", \"token_uri\": \"%s\", \"redirect_uris\": [\"%s\"] } }",
//                 clientId, clientSecret, authUri, tokenUri, redirectUri
//         );

//         log.info("The Client Secret is: " + clientSecretsJson);
//         GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new StringReader(clientSecretsJson));

//         GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                 new com.google.api.client.http.javanet.NetHttpTransport(),
//                 JSON_FACTORY,
//                 clientSecrets,
//                 Collections.singletonList("https://www.googleapis.com/auth/youtube.upload")
//         ).setAccessType("offline").build();

//         LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8087).build();
//         Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

//         String refreshToken = credential.getRefreshToken();
//         log.info("Refresh Token retrieved: {}", refreshToken);

//         return refreshToken;
//     }


// }
