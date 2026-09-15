package comp3011.assignment1.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final Instant startTime;

    public AdminController() {
        startTime = Instant.now();
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
}