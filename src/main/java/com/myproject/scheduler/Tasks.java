package com.myproject.scheduler;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);
    private Map<LocalDate, List<String>> tasks = new HashMap<>();

    boolean createTask(LocalDate date, String task) {
        try {
            /*
            if (tasks.get(date) == null) {
                List<String> currentTask = new ArrayList<>();
                currentTask.add(task);
                tasks.put(date, currentTask);
                return true;
            } else {
                //redundant? fix later
                List<String> newTask = new ArrayList<>();
                newTask.add(task);
                tasks.put(date, newTask);
                return true;
            } */
            List<String> tasksOnDate = tasks.computeIfAbsent(date, k -> new ArrayList<>());
            tasksOnDate.add(task);
            return true;
        }
        catch(Exception e) {
            getLogger().warn("Failed to add task for date {}", date, e);
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
        List<String> tasksOnDate = tasks.computeIfAbsent(date, k -> new ArrayList<>());
        try {
            tasksOnDate.remove(index);
            return true;
        }
        catch(Exception e) {
            getLogger().warn("Got exception while trying to remove index {} of tasks on {}", index, date, e);
            return false;
        }
    }

    /**
     * This annotation doesn't really do anything. It's mainly for the developer to know it's here for testing
     * purposes. Usually with a visibility modifier less strict than private so it can be used in a test class.
     *
     * This is used in order to allow us to stub a logger in our test class e.g., 'when(taskHolder.getLogger).thenReturn(logger);'
     *
     * @return the static instance of LOGGER in this class
     */
    @VisibleForTesting
    Logger getLogger() {
        return LOGGER;
    }
}
