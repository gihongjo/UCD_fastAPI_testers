package org.example.fastapitester.model;

import lombok.*;

import java.util.Map;

// AudioConfig.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AudioConfig {
    private boolean use_diarization = true;
    private Map<String, Integer> diarization;
    private boolean use_itn = false;
    private boolean use_disfluency_filter = false;
    private boolean use_profanity_filter = false;
    private boolean use_paragraph_splitter = true;
    private Map<String, Integer> paragraph_splitter;
}

