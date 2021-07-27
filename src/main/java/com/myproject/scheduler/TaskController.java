package com.myproject.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/*
 * This class autowires @Tasks and allows GET/Post/Delete operations
 *
 * String createTask(task, date) { return "success" / "failure" }
 * HashMap<String date, List<String> Task> showTask(date) { return date }
 * showAllTasks(date) { returns String tasks }
 * String deleteTask(date, index) { return "deleted" / "failure" }
 */


@RestController
public class TaskController {

    private final Tasks tasks;

    TaskController(@Autowired Tasks tasks) {
        this.tasks = tasks;
    }

    String createTasks(@RequestParam @DateTimeFormat(pattern="MM/dd/yyyy") LocalDate date, List<String> task) {
        try {
            tasks.createTask(date, task);
            return "ok!";
        }
        catch(Exception e) {
             return "Error creating task.";
        }
    }

    Map<LocalDate, List<String>> getTasks() {
        return tasks.getTasks();
    }

    List<String> getTasksFromDate(LocalDate date) {
        return tasks.getTasksFromDate(date);
    }

    String deleteTasks() {
        return "";
    }
}
