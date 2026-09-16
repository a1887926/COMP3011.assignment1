package comp3011.assignment1.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import comp3011.assignment1.service.StatsService;

//The rest controller tells spring that the class conains a REST API endpoint.
//The value returned by the methods in this class are sent back as HTTP responses.
@RestController
public class StatsController {
	
	//A private final variable that stores the statService which keeps track of the total input and output token usage.
	
	private final StatsService statsService;
	
	public StatsController(StatsService statsService) {
		this.statsService = statsService;
		
	}
	
	//The get mapping makes this method run when a GET request is sent to the global API stats.
	@GetMapping("/api/v1/global/stats")
	public Map<String, Long> getGlobalStats() {
		//The return map treturns the current input and output token totals as a JSON response.
		//The values are pulled from StatsService.getInput().
		return Map.of("inputTokens", statsService.getInputTokens(), "outputTokens", statsService.getOutputTokens());
				
	}
}