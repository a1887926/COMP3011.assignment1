package comp3011.assignment1.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import comp3011.assignment1.service.StatsService;

@RestController
public class StatsController {
	private final StatsService statsService;
	
	public StatsController(StatsService statsService) {
		this.statsService = statsService;
		
	}
	
	@GetMapping("/api/v1/global/stats")
	public Map<String, Long> getGlobalStats() {
		return Map.of("inputTokens", statsService.getInputTokens(), "outputTokens", statsService.getOutputTokens());
				
	}
}