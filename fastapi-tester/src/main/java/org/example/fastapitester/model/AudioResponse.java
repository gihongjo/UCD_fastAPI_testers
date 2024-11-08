package org.example.fastapitester.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

// AudioResponse.java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AudioResponse {
    private String status;
    private String message;
    private String filename;
    private String content_type;
    private Map<String, Object> config;
}
