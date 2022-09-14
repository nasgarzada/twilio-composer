package com.example.twiliocomposer;

import com.twilio.Twilio;
import com.twilio.rest.video.v1.Composition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompositionService implements CommandLineRunner {
    private static final String USERNAME = "";
    private static final String PASSWORD = "";


    public Composition createComposition(String roomSid) {
        Twilio.init(USERNAME, PASSWORD);
        var composition = Composition.creator(roomSid)
                .setAudioSources(List.of("*"))
                .setVideoLayout(Map.of("grid", Map.of("video_sources", List.of("*"))))
                .setFormat(Composition.Format.MP4)
                .create();

        saveComposition(composition);
        return composition;
    }

    public Composition getComposition(String compositionId) {
        Twilio.init(USERNAME, PASSWORD);
        return Composition.fetcher(compositionId)
                .fetch();
    }

    private void saveComposition(Composition composition) {
        log.info("Composition: {}", composition);
    }


    @Override
    public void run(String... args) throws Exception {
        var composition = getComposition("");
        log.info("{}",composition);
        while (!composition.getStatus().equals(Composition.Status.COMPLETED)) {
            log.info("Composition: {}", getComposition(composition.getSid()));
            Thread.sleep(5000);
        }
    }
}
