package comp3011.assignment1.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public record OpenAiTranscriptionResponse(String text, Usage usage) {
	
	// This part was assisted with AI given I was unsure how to efficiently map token usage fields.
	// I prompted ChatGPT to help me in properly learning and applying me to map JSON fields to the appropriate java record fields.
	public record Usage(@JsonProperty("input_tokens") long inputTokens, @JsonProperty("output_tokens") long outputTokens) {
		
	}
}
