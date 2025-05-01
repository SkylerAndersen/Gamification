// src/DataStructures/TaskData.java
package DataStructures;

import java.time.LocalDate;
import java.util.*;
import DataStructures.FileHandler;

public class TaskData {
    private static final String KEY       = PlayerData.name + "-tasks";
    private static final String RESET_KEY = PlayerData.name + "-tasks-last-reset";
    private final List<Task> tasks       = new ArrayList<>();

    public List<Task> getTasks() { return tasks; }

    public static TaskData load(FileHandler fh) {
        TaskData td = new TaskData();

        // 1) See if we’ve already reset today
        String lastReset = fh.retrieveString(RESET_KEY);
        String today     = LocalDate.now().toString();
        if (!today.equals(lastReset)) {
            // new day → clear & seed defaults immediately
            td.tasks.clear();
            td.tasks.add(new Task("Dishes",   100, Task.Status.IN_PROGRESS));
            td.tasks.add(new Task("Laundry",  150, Task.Status.IN_PROGRESS));
            td.tasks.add(new Task("Cleaning", 200, Task.Status.IN_PROGRESS));
            // …any other daily defaults…

            // persist both the new list and today’s reset flag
            fh.save(RESET_KEY, today);
            td.save(fh);
            return td;
        }

        // 2) Otherwise, load whatever’s already on disk
        String[] raw = fh.retrieveStringArray(KEY);
        for (String entry : raw) {
            td.tasks.add(Task.deserialize(entry));
        }
        return td;
    }

    public void save(FileHandler fh) {
        String[] serialized = tasks.stream()
                .map(Task::serialize)
                .toArray(String[]::new);
        fh.save(KEY, serialized);
    }
}
