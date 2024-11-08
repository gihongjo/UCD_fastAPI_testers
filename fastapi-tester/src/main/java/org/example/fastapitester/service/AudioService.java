package org.example.fastapitester.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.fastapitester.model.AudioConfig;
import org.example.fastapitester.model.AudioResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

// AudioService.java
@Service
@Slf4j
public class AudioService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AudioService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public AudioResponse transcribeAudio(File audioFile, int numOfSpeakers, String bearerToken) {
        String url = "http://localhost:8000/v1/transcribe";  // FastAPI 서버 주소

        try {
            // Audio Config 생성
            AudioConfig config = AudioConfig.builder()
                    .use_diarization(true)
                    .diarization(Map.of("spk_count", numOfSpeakers))
                    .use_paragraph_splitter(true)
                    .paragraph_splitter(Map.of("max", 50))
                    .build();

            // MultiValueMap 설정
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(audioFile));
            body.add("config", objectMapper.writeValueAsString(config));

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(bearerToken);

            // HTTP 요청 엔티티 생성
            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            // POST 요청 전송
            ResponseEntity<AudioResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    AudioResponse.class
            );

            return response.getBody();

        } catch (RestClientException e) {
            log.error("REST client error: ", e);
            throw new RuntimeException("Failed to transcribe audio", e);
        } catch (JsonProcessingException e) {
            log.error("JSON processing error: ", e);
            throw new RuntimeException("Failed to create config JSON", e);
        }
    }
}

