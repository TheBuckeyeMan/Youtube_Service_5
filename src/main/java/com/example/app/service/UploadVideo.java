package com.example.app.service;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

@Service
public class UploadVideo {
    private static final Logger log = LoggerFactory.getLogger(UploadVideo.class);


    public void uploadVideo(YouTube youtube, Path youtubeVideo,String title) throws Exception{
        log.info("Begining to upload video to Youtube");

        //Set video Metadata
        Video video = new Video();

        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public");
        video.setStatus(status);

        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription("Hey! Thank you for watching my youtube short! Please feel free to leave a comment in the comment section below!");
        snippet.setTags(Collections.singletonList("example"));
        video.setSnippet(snippet);

        //Define Media Content
        File videoFile = youtubeVideo.toFile();
        com.google.api.client.http.FileContent mediaContent = new com.google.api.client.http.FileContent("video/*", videoFile);

        //Insert Video
        YouTube.Videos.Insert request = youtube.videos().insert("snippet,status", video, mediaContent);
        Video response = request.execute();
        log.info("Video uploaded successfully. Video ID: {}", response.getId());
    }

    
}
