package com.example.app.service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceTrigger {
    private static final Logger log = LoggerFactory.getLogger(ServiceTrigger.class);
    private S3LoggingService s3LoggingService;
    private GetVideoFile getVideoFile;
    private ReadFile readFile;

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

    @Value("${youtube.api.endpoint}")
    private String youtubeEndpoint;

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    public ServiceTrigger(S3LoggingService s3LoggingService, GetVideoFile getVideoFile, ReadFile readFile){
        this.s3LoggingService = s3LoggingService;
        this.getVideoFile = getVideoFile;
        this.readFile = readFile;
    }

    public void TriggerService(){
        //Initialization Logs
        log.info("The Active Environment is set to: " + environment);
        log.info("Begining to Collect Contents of Fun Fact form S3 Bucket");

        //Trigger Services
        //Service 1: Download the Youtube Video Title
        String videoTitle = readFile.getBasicFileContents(landingBucket, titleKey);
        //Service 2: Download the Youtube Video
        Path youtubeVideoToPost = getVideoFile.getVideo(landingBucket, youtubeKey);
        //Service 3: 


        //Service 4: Save the audio file to the aws s3 bucket
        log.info("The Youtube Video Has Been Posted");
        s3LoggingService.logMessageToS3("Succcess: Success occured at: " + LocalDateTime.now() + " On: youtube-service-5" + ",");
        log.info("Final: The Lambda has triggered successfully and the Video is now posted to youtube!: " + landingBucket);

        //TODO Task 6: implement unit testing
    }
}