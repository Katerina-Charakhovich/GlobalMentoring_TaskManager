package com.example.taskmanager.service;

import com.example.taskmanager.model.Category;
import com.example.taskmanager.model.SubTask;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private TaskRepository repository;

    @Autowired
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> findAllTasks() {
        return repository.findAll();
    }

    public List<Task> getAllByCategory(String category) {
        return repository.findByCategory(Category.valueOf(category));
    }

    public Task save(Task task) {
        return repository.save(task);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Task> findSubTaskByCategory(String category) {
        return repository.findSubTaskByCategory(Category.valueOf(category));
    }

    public Optional<Task> updateTaskById(String id, Task task) {
        Optional<Task> realTask = repository.findById(id);
        return realTask.isPresent()
                ? Optional.of(repository.save(update(id, task)))
                : realTask;

    }

    public List<Task> findTasksByDescription(String regexp) {
        return repository.findTasksByDescription(regexp);
    }

    public List<Task> findTasksBySubtaskName(String subtaskName) {
        return repository.findTasksBySubtaskName(subtaskName);
    }

    public List<Task> findOverdueTasks(Date date) {
        return repository.findByDeadlineBefore(date);
    }

    public Optional<Task> addSubtasksToTaskById(String id, List<SubTask> subTasks) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()) {
            return optionalTask;
        }
        Task task = optionalTask.get();
        task.setSubtasks(subTasks);
        return Optional.of(repository.save(task));
    }

    public Optional<Task> deleteSubtaskOfTaskById(String id) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()) {
            return optionalTask;
        }
        Task task = optionalTask.get();
        task.setSubtasks(null);
        return Optional.of(repository.save(task));
    }

    public Optional<Task> updateSubtaskOfTaskById(String id, List<SubTask> subTasks) {
        Optional<Task> optionalTask = repository.findById(id);
        if (optionalTask.isEmpty()) {
            return optionalTask;
        }
        Task task = optionalTask.get();
        task.setSubtasks(null);
        return Optional.of(repository.save(task));
    }

    public Task update(final String id, final Task task) {
        final Task taskUpdated = new Task();
        taskUpdated.setId(id);
        taskUpdated.setTaskName(Objects.isNull(task.getTaskName()) ? null : task.getTaskName());
        taskUpdated.setCategory(Objects.isNull(task.getCategory()) ? null : task.getCategory());
        taskUpdated.setCreationDate(Objects.isNull(task.getCreationDate()) ? null : task.getCreationDate());
        taskUpdated.setDeadline(Objects.isNull(task.getDeadline()) ? null : task.getDeadline());
        taskUpdated.setSubtasks(Objects.isNull(task.getSubtasks()) ? null : task.getSubtasks());
        return taskUpdated;
    }

}
