// AudioController.java
package org.example.fastapitester.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.fastapitester.model.AudioResponse;
import org.example.fastapitester.service.AudioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
@Slf4j
public class AudioController {

    private final AudioService audioService;

    public AudioController(AudioService audioService) {
        this.audioService = audioService;
    }

    @PostMapping("/transcribe")
    public ResponseEntity<AudioResponse> transcribeAudioFromDirectory(
            @RequestHeader("Authorization") String bearerToken
            ) {

        File ftpDir = new File("ftp"); // FTP 디렉토리 객체 생성
        int numOfSpeakers = 3;
        if (!ftpDir.exists()) {
            log.info("There is no ftp directory. Ftp directory will be created automatically");
            ftpDir.mkdirs(); // 디렉토리가 없으면 생성
        }

        File[] files = ftpDir.listFiles(); // FTP 디렉토리 내 파일 목록 가져오기

        if (files == null || files.length == 0) {
            log.info("No files found in the ftp directory.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        for (File file : files) {
            if (file.isFile()) {
                // 서비스 호출
                AudioResponse response = audioService.transcribeAudio(file, numOfSpeakers, bearerToken);



                // 필요한 경우 응답을 반환하거나 누적
                // 여기서는 첫 번째 파일의 응답만 반환하도록 하겠습니다.
                return ResponseEntity.ok(response);

            }
        }

        // 모든 파일이 디렉토리가 아닌 경우
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
