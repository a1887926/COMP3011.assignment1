package comp3011.assignment1.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public record OpenAiTranscriptionResponse(String text, Usage usage) {
	public record Usage(@JsonProperty("input_tokens") long inputTokens, @JsonProperty("output_tokens") long outputTokens) {
		
	}
}
