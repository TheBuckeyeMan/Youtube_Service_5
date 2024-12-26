package com.example.app.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.services.youtube.YouTube;

@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);
    private S3LoggingService s3LoggingService;
    private GetVideoFile getVideoFile;
    private ReadFile readFile;
    private OAuth2 oAuth2;
    // private RefreshToken refreshToken;
    private UploadVideo uploadVideo;

    @Value("${spring.profiles.active}")
    private String environment;

    @Value("${aws.s3.bucket.landing}")
    private String landingBucket;

    @Value("${aws.s3.bucket.logging}")
    private String loggingBucket;
 
    @Value("${aws.s3.key.youtube}")
    private String youtubeKey;

    @Value("${aws.s3.key.logging}")
    private String loggingKey;

    @Value("${aws.s3.key.title}")
    private String titleKey;

    @Value("${youtube.api.clientid}")
    private String clientId;

    @Value("${youtube.api.clientsecret}")
    private String clientSecret;

    @Value("${youtube.api.authuri}")
    private String authUri;

    @Value("${youtube.api.tokenuri}")
    private String tokenUri;

    @Value("${youtube.api.certurl}")
    private String certUrl;

    @Value("${youtube.api.redirecturi}")
    private String redirectUri;

    @Value("${youtube.api.refresh.token}")
    private String Token;

    public ServiceTrigger(S3LoggingService s3LoggingService, GetVideoFile getVideoFile, ReadFile readFile, OAuth2 oAuth2, UploadVideo uploadVideo){
        this.s3LoggingService = s3LoggingService;
        this.getVideoFile = getVideoFile;
        this.readFile = readFile;
        this.oAuth2 = oAuth2;
      //  this.refreshToken = refreshToken;
        this.uploadVideo = uploadVideo;
    }

    public void TriggerService(){
        try{
        //Initialization Logs
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");

        //Trigger Services
        //Service 1: Download the Youtube Video Title
        String videoTitle = readFile.getBasicFileContents(landingBucket, titleKey);
        
        //Service 2: Download the Youtube Video
        Path youtubeVideoToPost = getVideoFile.getVideo(landingBucket, youtubeKey);
        
        //Service 3: get Refresh Token - ONLY UNCOMMENT IF YOU NEED A NEW REFRESH TOKEN - DOES NOT WORK ON LAMBDA
        // String oAuthRefreshToken = refreshToken.getRefreshToken(clientId,clientSecret,authUri,tokenUri,redirectUri);

        //Service 4 Authenticate with Oauth
        YouTube youtube = oAuth2.authenticate(Token,clientId,clientSecret,tokenUri);

        //service 5
        uploadVideo.uploadVideo(youtube, youtubeVideoToPost, videoTitle);

        //Service 4: Save the audio file to the aws s3 bucket
        log.info("The Youtube Video Has Been Posted");
        s3LoggingService.logMessageToS3("Succcess: Success occured at: " + LocalDateTime.now() + " On: youtube-service-5" + ",");
        log.info("Final: The Lambda has triggered successfully and the Video is now posted to youtube!: " + landingBucket);

        //TODO Task 6: implement unit testing
        } catch (Exception e){
            log.error("Error during video upload: {}", e.getMessage(), e);
            s3LoggingService.logMessageToS3("Error: Uploading Youtube Video ServiceTrigger.java line 88, Check if refresh token has expired: " + LocalDate.now() + " On: youtube-service-5" + ",");
        }
    }
}
