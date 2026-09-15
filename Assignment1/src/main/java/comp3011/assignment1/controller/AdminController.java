package comp3011.assignment1.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    private final long startTime;

    public AdminController() {
        startTime = System.currentTimeMillis();
    }

    @GetMapping("/api/v1/admin/uptime")
    public Map<String, Long> getUptime() {
        long uptime = System.currentTimeMillis() - startTime;
        return Map.of("uptime", uptime);
    }
}