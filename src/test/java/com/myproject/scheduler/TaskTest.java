package com.myproject.scheduler;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * There are two ways to unit test classes.
 * 1. The most obvious way is to instantiate the object you are trying to test and verify your operations are as
 * expected. This ends up being fairly difficult especially for more complex classes.
 * 2. The second way is to mock certain parts instead of having a fully fledged class instantiated. We can achieve
 * this by using Mockito. For example, we can "mock" a class to return 'true' or 'false' within our test instead of
 * having to run through all the logic to force the result.
 *
 * Both ways are used below.
 */
public class TaskTest {

    private static final String TASK = "I am a task!";
    private static final String TASK_2 = "I am another task!";

    /**
     * This is pretty much the same as having a separate test class.
     * This can be done for logical groupings of tests. For our purposes,
     * we are going to write a suite of tests with Mockito as well
     * within another nested test class. see {@link TaskHolderTestWithMockito}
     */
    @Nested
    class TaskHolderTestWithoutMockito {

        @Test
        void createTask() {
            Tasks tasks = new Tasks();
            // creates a task to the new TaskHolder
            assertTrue(tasks.createTask(LocalDate.now(), TASK));
        }

        @Test
        void askTaskFails() {
            Tasks tasks = new Tasks();
            // try to add a task to the new TaskHolder with a null LocalDate
            assertFalse(tasks.createTask(null, TASK));
        }

        @Test
        void getTasks() {
            Tasks tasks = new Tasks();
            // add a task for two different dates
            LocalDate now = LocalDate.now();
            LocalDate future = LocalDate.MAX;
            tasks.createTask(now, TASK);
            tasks.createTask(future, TASK_2);

            Map<LocalDate, List<String>> tasksMap = tasks.getTasks();
            assertEquals(TASK, tasksMap.get(now).get(0));
            assertEquals(TASK_2, tasksMap.get(future).get(0));
            assertEquals(2, tasksMap.size());
        }

        @Test
        void getTasksFromDate() {
            Tasks tasks = new Tasks();
            // add TASK and TASK_2 for the same date
            LocalDate now = LocalDate.now();

            tasks.createTask(now, TASK);
            tasks.createTask(now, TASK_2);

            List<String> tasksList = tasks.getTasksFromDate(now);
            assertEquals(TASK, tasksList.get(0));
            assertEquals(TASK_2, tasksList.get(1));
            assertEquals(2, tasksList.size());
        }

        @Test
        void deleteTask() {
            Tasks tasks = new Tasks();

            // add a task
            LocalDate now = LocalDate.now();
            tasks.createTask(now, TASK);

            // get tasks for the date and check that there is one task
            List<String> tasksList = tasks.getTasksFromDate(now);
            assertEquals(1, tasksList.size());

            // delete a task and check that there are now 0
            assertTrue(tasks.deleteTask(now, 0));
            assertEquals(0, tasksList.size());
        }

        @Test
        void deleteTaskFails() {
            Tasks tasks = new Tasks();

            // add a task
            LocalDate now = LocalDate.now();
            tasks.createTask(now, TASK);

            // get tasks for the date and check that there is one task
            List<String> tasksList = tasks.getTasksFromDate(now);
            assertEquals(1, tasksList.size());

            // try to delete a task with index 1 but there is only one element
            assertFalse(tasks.deleteTask(now, 1));
            assertEquals(1, tasksList.size());
        }

    }

    /**
     * This is pretty much the same as having a separate test class.
     * This can be done for logical groupings of tests. For our purposes,
     * we are going to write a suite of tests without Mockito as well
     * within another nested test class. see {@link TaskHolderTestWithoutMockito}
     */
    @Nested
    @ExtendWith(MockitoExtension.class)
    class TaskHolderTestWithMockito {

        // we use this in combination with 'when(taskHolder.getLogger()).thenReturn(logger);'
        // so that we stub in our mock version of logger rather than use the actual class one.
        @Mock
        private Logger logger;

        // This lets us test our desired class 'TaskHolder'
        @Spy
        private Tasks tasks;

        @Test
        void createTask() {
            LocalDate now = LocalDate.now();
            assertTrue(tasks.createTask(now, TASK));
            verify(logger, never()).warn(any());
        }

        @Test
        void createTaskThrowsException() {
            // this makes it so that getLogger() doesn't actually run. we just instantly return our 'logger'
            // instance created above
            when(tasks.getLogger()).thenReturn(logger);

            assertFalse(tasks.createTask(null, TASK));
            verify(logger).warn(eq("Failed to add task for date {}"), eq(null), any(Exception.class));
        }

        @Test
        void getTasks() {
            // Set TaskHolder.tasksMap using reflection; don't worry too much about reflection
            Map<LocalDate, List<String>> expected = getTasksMap(LocalDate.now());
            ReflectionTestUtils.setField(tasks, "tasksMap", expected);

            // get the actual tasksMap from taskHolder
            Map<LocalDate, List<String>> actual = tasks.getTasks();

            // verify that it is the same as the one we previously set
            assertEquals(expected, actual);
        }

        @Test
        void getTasksFromDate() {
            LocalDate now = LocalDate.now();

            // Set TaskHolder.tasksMap using reflection; don't worry too much about reflection
            Map<LocalDate, List<String>> expected = getTasksMap(now);
            ReflectionTestUtils.setField(tasks, "tasksMap", expected);

            // check that we get back the same task
            List<String> result = tasks.getTasksFromDate(now);
            assertEquals(expected.get(now), result);
        }

        @Test
        void deleteTask() {
            LocalDate now = LocalDate.now();

            // Set TaskHolder.tasksMap using reflection; don't worry too much about reflection
            Map<LocalDate, List<String>> expected = getTasksMap(now);
            ReflectionTestUtils.setField(tasks, "tasksMap", expected);

            // check that deletion was successful
            assertTrue(tasks.deleteTask(now, 0));
        }

        @Test
        void deleteTaskThrowsException() {
            // we need to stub getLogger() here so we can complete the verification below as well as control any responses
            when(tasks.getLogger()).thenReturn(logger);

            LocalDate now = LocalDate.now();

            // try to delete a task without any available & verify we log a warning
            assertFalse(tasks.deleteTask(now, 0));
            verify(logger).warn(eq("Got exception while trying to remove index {} of tasks on {}"), eq(0), eq(now), any(Exception.class));
        }

        private Map<LocalDate, List<String>> getTasksMap(LocalDate date) {
            Map<LocalDate, List<String>> tasksMap = new ConcurrentHashMap<>();
            List<String> tasks = new ArrayList<>(Arrays.asList(TASK, TASK_2));
            tasksMap.put(date, tasks);
            return tasksMap;
        }
    }
}
