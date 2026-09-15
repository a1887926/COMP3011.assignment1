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

    private final Instant startTime;
    private final ConfigurableApplicationContext context;
    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);
    

    public AdminController(ConfigurableApplicationContext context) {
    	this.context = context;
    	this.startTime = Instant.now();
    }

    @GetMapping("/api/v1/admin/uptime")
    public Map<String, Object> getUptime() {

        Instant now = Instant.now();

        double uptimeSeconds =
                Duration.between(startTime, now).toMillis() / 1000.0;

        return Map.of(
                "utcServerStart", startTime.toString(),
                "utcNow", now.toString(),
                "serverUptimeSeconds", uptimeSeconds);
    }
    
    @PostMapping("/api/v1/admin/shutdown")
    public ResponseEntity<?> shutdown() {
    	// This segment was incorporated with the assistance of AI to review this section and suggest an appropriate way to handle multiple shutdown requests at the same time.
    	// Wasn't exactly too sure how to incorporate this, required assistance of AI for help.
    	if (!shuttingDown.compareAndSet(false, true)) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("timestamp", Instant.now().toString(),
    				"status", 409, "error", "Conflict", "message", "Graceful shutdown is already in progress.", 
    				"path", "/api/v1/admin/shutdown"));
    	}
    	
    	new Thread (() -> {
    		try {
    			Thread.sleep(500);
    			context.close();
    		} catch (InterruptedException error) {
    			Thread.currentThread().interrupt();
    		}
    	}).start();
    	
    	return ResponseEntity.accepted().body(Map.of("message", "Graceful shutdown requested."));
    }
    
    
    
    
}