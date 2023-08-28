package com.example.taskmanager.web;

import com.example.taskmanager.model.SubTask;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


//
// +Display on console all tasks.
//        Display overdue tasks.
//       + Display all tasks with a specific category (query parameter).
//      +  Display all subtasks related to tasks with a specific category (query parameter).
//       + Perform insert/update/delete of the task.
//        Perform insert/update/delete all subtasks of a given task (query parameter).
//     +   Support full-text search by word in the task description.
//      +  Support full-text search by a sub-task name.


@RestController
@RequestMapping("/tasks")
public class TaskController {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private TaskService service;

    @Autowired
    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @GetMapping
    public ResponseEntity<?> findAllTasks() {
        List<Task> tasks = service.findAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/category/{category}")
    @ResponseBody
    public List<Task> findTaskByCategory(@PathVariable @NonNull String category) {
        return service.getAllByCategory(category);
    }

    @GetMapping("/category/{category}/subtasks")
    @ResponseBody
    public List<Task> findSubTaskByCategory(@PathVariable @NonNull String category) {
        return service.findSubTaskByCategory(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final String id,
                                    @RequestBody final Task task) {
        final var response = service.updateTaskById(id, task);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody final Task task) {
        task.setCreationDate(new Date());
        return ResponseEntity.ok(service.save(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("description")
    public List<Task> findByDescription(@RequestParam @NonNull String regexp) {
        return service.findTasksByDescription(regexp);
    }

    @GetMapping("/subtasks")
    public List<Task> findBySubtaskName(@RequestParam @NonNull String subtaskName) {
        return service.findTasksBySubtaskName(subtaskName);
    }

    @GetMapping("/deadline")
    public ResponseEntity<?> findOverdueTasks(@RequestParam @NonNull String deadline) {
        try {
            Date date = DATE_FORMATTER.parse(deadline);
            return ResponseEntity.ok(service.findOverdueTasks(date));
        } catch (ParseException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/subtasks")
    public ResponseEntity<?> addSubtasksToTaskById(@PathVariable final String id,
                                                   @RequestBody final List<SubTask> subtasks) {
        final var response = service.addSubtasksToTaskById(id, subtasks);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @PutMapping("/{id}/subtasks")
    public ResponseEntity<?> updateSubtasksToTaskById(@PathVariable final String id,
                                                      @RequestBody final List<SubTask> subtasks) {
        final var response = service.addSubtasksToTaskById(id, subtasks);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }

    @DeleteMapping("/{id}/subtasks")
    public ResponseEntity<?> deleteSubtaskOfTaskById(@PathVariable final String id) {
        final var response = service.deleteSubtaskOfTaskById(id);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response.get());
    }
}
