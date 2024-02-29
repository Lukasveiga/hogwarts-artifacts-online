package com.devlukas.hogwartsartifactsonline.system.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UsableMemoryHealthIndicator implements HealthIndicator {


    @Override
    public Health health() {
        File path = new File("."); // Path use to compute available disk space
        long diskUsableInBytes = path.getUsableSpace();
        var diskUsableThreshold = 10 * 1024 * 1024; // 10MB;
        boolean isHealth = diskUsableInBytes >= diskUsableThreshold;
        Status status = isHealth ? Status.UP : Status.DOWN; // UP means there is enough usable memory;
        return Health
                .status(status)
                .withDetail("usable memory", diskUsableInBytes)
                .withDetail("treshold", diskUsableThreshold)
                .build();
    }
}
