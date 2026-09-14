package comp3011.assignment1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TranscriptionController {
	
	@PostMapping("/api/v1/transcriptions")
	public ResponseEntity<String> transcribe (
			@RequestParam("file") MultipartFile file) {
		
		if (file.isEmpty()) {
			return ResponseEntity.badRequest()
					.body("No audio file received");
		}
		
		return ResponseEntity.ok("Audio received successfully");
	}
}