package com.gibanator.dailystepbackendjava.asr;

import com.gibanator.dailystepbackendjava.asr.dto.AsrResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SpeechRecognitionClient {

    private final RestClient restClient;

    public SpeechRecognitionClient(@Value("${asr.url}") String asrUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(asrUrl)
                .build();
    }

    public AsrResponse transcribe(MultipartFile audio) throws IOException {
        InputStreamResource resource = new InputStreamResource(audio.getInputStream()) {
            @Override
            public String getFilename() {
                return audio.getOriginalFilename();
            }

            @Override
            public long contentLength() {
                return audio.getSize();
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);

        return restClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(body)
                .retrieve()
                .body(AsrResponse.class);

    }
}
