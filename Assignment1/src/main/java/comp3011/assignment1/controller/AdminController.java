package comp3011.assignment1.controller;


import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

	//Private final variable for storing the time when the server starts.
    private final Instant startTime;
    //Private final variable used to access and close off the Spring application.
    private final ConfigurableApplicationContext context;
    //Private final variable used to track if shutdown has started.
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    

    public AdminController(ConfigurableApplicationContext context) {
    	this.context = context;
    	this.startTime = Instant.now();
    }

    //Get mapping makes the method run appropriately when a GET request is sent to the API.
    @GetMapping("/api/v1/admin/uptime")
    public Map<String, Object> getUptime() {

    	//Instant now stores the correct time when the endpoint is requested.
        Instant now = Instant.now();

        //uptimeSeconds finds the difference between the server sart time and the current time.
        double uptimeSeconds =
                Duration.between(startTime, now).toMillis() / 1000.0;

        //This segment returns the required uptime information sent as a JSON response.
        return Map.of(
                "utcServerStart", startTime.toString(),
                "utcNow", now.toString(),
                "serverUptimeSeconds", uptimeSeconds);
    }
    
    //Post mapping makes the method run when a POST request is sent to the admin API.
    @PostMapping("/api/v1/admin/shutdown")
    public ResponseEntity<?> shutdown() {
    	// This segment was incorporated with the assistance of AI to review this section and suggest an appropriate way to handle multiple shutdown requests at the same time.
    	// Wasn't exactly too sure how to incorporate this, required assistance of AI for help.
    	if (!shuttingDown.compareAndSet(false, true)) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("timestamp", Instant.now().toString(),
    				"status", 409, "error", "Conflict", "message", "Graceful shutdown is already in progress.", 
    				"path", "/api/v1/admin/shutdown"));
    	}
    	
    	// This segment of code starts a seperate thread so the request can finish and send the appropriate response before the Spring application is closed.
    	new Thread (() -> {
    		try {
    			//A smell delay gives the HTTP response time to be returned.
    			Thread.sleep(500);
    			//Closes the spring application context and shuts down the server.
    			
    			context.close();
    		} catch (InterruptedException error) {
    			//Interrupt restores the interrupted state if the thread gets interrupted.
    			Thread.currentThread().interrupt();
    		}
    	}).start();
    	
    	//Returns HTTP 202 Accepted to appropriately show the shutdown request was accepted and the shutdown process has started.
    	return ResponseEntity.accepted().body(Map.of("message", "Graceful shutdown requested."));
    }
    
    
    
    
}