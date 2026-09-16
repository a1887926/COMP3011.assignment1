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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import comp3011.assignment1.model.OpenAiTranscriptionResponse;

//@Service tells spring that the class contains the transcription service logic.
@Service
public class TranscriptionService {

    private final RestClient restClient;
    private final StatsService statsService;
    private final String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(TranscriptionService.class);

    public TranscriptionService(
            RestClient.Builder builder, @Value("${OPENAI_API_KEY:}") String apiKey, StatsService statsService) {

        this.apiKey = apiKey;
        this.statsService = statsService;

       //this.restClient creates the restClient with openAI as the base URL.
        this.restClient = builder.baseUrl("https://api.openai.com").build();
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

            //Multi value map creates the multipart request body that will be sent to OpenAI.
            MultiValueMap<String, Object> body =
                    new LinkedMultiValueMap<>();

            //Adds the required and correct transcription model to the request.
            body.add("model", "gpt-4o-mini-transcribe");
            //Adds the uploaded audio file to the request.
            body.add("file", audioFile);

            //Logs that the transcription request is about to be sent.
            logger.info("Sendinfg audio to transcription service");
            //Response sends the audio to OpenAI and converts the JSON response into a java record.
            OpenAiTranscriptionResponse response =
                    restClient.post()
                            .uri("/v1/audio/transcriptions")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .body(body)
                            .retrieve()
                            .body(OpenAiTranscriptionResponse.class);

            if (response == null) {
            	logger.error("Transcription service returned no response");
                throw new RuntimeException("No transcription returned");
            }
            
            logger.info("Transcription completed successfully");

            if (response.usage() != null) {
                statsService.addUsage(response.usage().inputTokens(), response.usage().outputTokens());
            }

            return response.text();

        } catch (IOException error) {
        	logger.error("Could not read uploaded audio file");
            throw new RuntimeException("Could not read audio file");
        }
    }
}