package comp3011.assignment1.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import comp3011.assignment1.model.OpenAiTranscriptionResponse;

@Service
public class TranscriptionService {

    private final RestClient restClient;

    public TranscriptionService(
            RestClient.Builder builder,
            @Value("${OPENAI_API_KEY:}") String apiKey) {

        restClient = builder
                .baseUrl("https://api.openai.com")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey)
                .build();
    }

    public String transcribe(MultipartFile file) {

        try {
            ByteArrayResource audioFile =
                    new ByteArrayResource(file.getBytes()) {

                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();

            body.add("model", "gpt-4o-mini-transcribe");
            body.add("file", audioFile);

            OpenAiTranscriptionResponse response =
                    restClient.post()
                            .uri("/v1/audio/transcriptions")
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(body)
                            .retrieve()
                            .body(OpenAiTranscriptionResponse.class);

            if (response == null) {
                throw new RuntimeException("No transcription returned");
            }

            return response.text();

        } catch (IOException error) {
            throw new RuntimeException("Could not read audio file");
        }
    }
}