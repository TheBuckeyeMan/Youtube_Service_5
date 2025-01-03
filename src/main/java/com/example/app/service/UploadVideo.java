package com.example.app.service;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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


    public void uploadVideo(YouTube youtube, Path youtubeVideo,String title, String tags) throws Exception{
        log.info("Begining to upload video to Youtube");

        //Set video Metadata
        Video video = new Video();

        VideoStatus status = new VideoStatus();
        status.setPrivacyStatus("public");
        video.setStatus(status);

        List<String> predefinedTags = Arrays.asList("fun facts", "fyp", "interesting", "shorts");
        List<String> processedTags = Arrays.stream(tags.split(" "))
                                   .map(tag -> tag.startsWith("#") ? tag.substring(1) : tag)
                                   .collect(Collectors.toList());
        List<String> allTags = new ArrayList<>(predefinedTags);
        allTags.addAll(processedTags);

        VideoSnippet snippet = new VideoSnippet();
        snippet.setTitle(title);
        snippet.setDescription("Hey! Thank you for watching my youtube short! In this YouTube Shorts video, we dive into some mind-blowing facts. Don't forget to like, comment, and subscribe for daily " + "#FunFacts #FYP " + tags);
        snippet.setTags(allTags);
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