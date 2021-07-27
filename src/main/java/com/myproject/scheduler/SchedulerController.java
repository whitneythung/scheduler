package com.myproject.scheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class SchedulerController {

    @GetMapping("/")
    public String welcomeMessage() {
        return "Today's Date: " + LocalDateTime.now();
    }

  /*  private fun LocalDateTime.format(): String? {
        return "Today's Date: " + LocalDateTime.now();
    }
    */

}
