package com.myproject.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private final Tasks tasks;

    // @Autowired
    // private Tasks tasks;

    // or

    // Constructor injection (preferred method)
    // private final Tasks tasks;
    // ScheduledTasks(Tasks tasks) {
    //   this.tasks = tasks;
    // }

    ScheduledTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        LOGGER.info("The time is now {}", DATE_FORMAT.format(new Date()));
    }
}