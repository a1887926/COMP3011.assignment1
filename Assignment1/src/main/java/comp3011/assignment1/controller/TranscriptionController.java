package comp3011.assignment1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import comp3011.assignment1.service.TranscriptionService;

//A rest controller tells spring that this class contains a REST API endpoint.
//The responses returned from the methods in this class are sent directly back to the client as HTTP r
@RestController
public class TranscriptionController {

	//Private final variable that stores the transcription service.
    private final TranscriptionService transcriptionService;

    public TranscriptionController(
            TranscriptionService transcriptionService) {

        this.transcriptionService = transcriptionService;
    }

    //Post mapping makes this method run when a POST request is sent to the API.
    @PostMapping("/api/v1/transcriptions")
    //ResponseEntity receives the audio recording uploaded from the webpage.
    public ResponseEntity<String> uploadAudio(
    		//Request param retrieves the audio recording uploaded from the webpage.
            @RequestParam("file") MultipartFile file) {

    	//An if condition loop if the file is empty, a HTTP 400 bad request is returned instead of sending an empty file for transcription.
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("No audio received");
        }

        
        try {
        	//Sends the uploaded audio filoe to the transcription service.
        	
            String text = transcriptionService.transcribe(file);

            //Returns the transcription text with a HTTP 200 OK!
            return ResponseEntity.ok(text);

            //If an exception error is caught, it prints the errors details to the console to view issues.
        } catch (Exception error) {
            error.printStackTrace();

            //If the transcription failed, a HTTP 500 is returned.
            return ResponseEntity.internalServerError()
                    .body("Transcription failed");
        }
    }
}