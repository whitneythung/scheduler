package com.myproject.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
 * This class auto wires the Tasks class and allows GET/Post/Delete operations. Auto wiring uses dependency injection to connect
 * Tasks to TaskController. It is NOT the same as inheritance in Object Oriented Programming.
 *
 * Methods in this controller:
 * - createTask(task, date) { return "success" / "failure" }
 * - getTask() { returns all Tasks }
 * - getTasksFromDate(date) { returns tasks.getTasksFromDate(date) / Empty list with message, "unable to get tasks from date" }
 * - deleteTask(date, index) { return "Success" / "Unable to delete task" }
 *
 * Some notes about GET/POST/DELETE operations:
 * GET is used to view something
 * POST is used for changing something
 * DELETE is used to delete something
 *
 * Some other notes:
 * @RequestParam is used to retrieve query parameters
 */


@RestController
public class TaskController {

    private final Tasks tasks;

    TaskController(@Autowired Tasks tasks) {
        this.tasks = tasks;
        // tasks = new TaskFactory().getTask();
        // inside TaskFactory you still need to have new Task();
    }

    @PostMapping("/task")
    String createTasks(@RequestParam("date") @DateTimeFormat(pattern="yyyy-mm-dd") LocalDate date, @RequestParam("task") String task) {
            tasks.createTask(date, task);
            return "Task successfully created.";
    }

    @GetMapping("/tasks")
    Map<LocalDate, List<String>> getTasks() {
        return tasks.getTasks();
    }

    //gets task from a specific date
    @GetMapping("/task")
    List<String> getTasksFromDate(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate date) {
       try {
           return tasks.getTasksFromDate(date);
       }
       catch(Exception e) {
           System.out.println("Unable to get tasks for date : " + date);
           return Collections.emptyList();
       }
    }

    @DeleteMapping("/task")
    String deleteTasks(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-mm-dd") LocalDate date, @RequestParam("index") int index) {
        try {
            tasks.deleteTask(date, index);
            return "Successfully deleted task.";
        }
        catch(Exception e) {
            return "Unable to delete task.";
        }
    }
}
