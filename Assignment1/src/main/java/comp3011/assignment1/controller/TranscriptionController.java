package comp3011.assignment1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import comp3011.assignment1.service.TranscriptionService;

@RestController
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    public TranscriptionController(
            TranscriptionService transcriptionService) {

        this.transcriptionService = transcriptionService;
    }

    @PostMapping("/api/v1/transcriptions")
    public ResponseEntity<String> uploadAudio(
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("No audio received");
        }

        try {
            String text = transcriptionService.transcribe(file);

            return ResponseEntity.ok(text);

        } catch (Exception error) {
            error.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("Transcription failed");
        }
    }
}