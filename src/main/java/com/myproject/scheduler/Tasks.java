package com.myproject.scheduler;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/*
 *  This class will have all of the tasks I can do on the scheduler.
 *  boolean createTask(date, task) { returns OK / FAIL }
 *  map<date, task> getTasks() { return task }
 *  List<String> getTasksFromDate(date) { return tasks}
 *  boolean deleteTask(task) { returns OK / FAIL }
 */

@Component
public class Tasks {
    private Map<LocalDate, List<String>> tasks = new HashMap<>();

    boolean createTask(LocalDate date, String task) {
            if (tasks.get(date) == null) {
                List<String> currentTask = new ArrayList<>();
                tasks.put(date,currentTask);
                return true;
            }
            else {
            System.out.println("Failed to create task.");
            return false;
        }
    }

    // This function returns all tasks
    Map<LocalDate, List<String>> getTasks() {
        return tasks;
    }

    // This function returns your tasks from a specified date
    List<String> getTasksFromDate(LocalDate date) {
        return tasks.get(date);
    }

    boolean deleteTask(LocalDate date, int index) {
        try {
            tasks.remove(date, index);
            return true;
        }
        catch(Exception e) {
            System.out.println("Unable to remove task.");
            return false;
        }
    }
}
