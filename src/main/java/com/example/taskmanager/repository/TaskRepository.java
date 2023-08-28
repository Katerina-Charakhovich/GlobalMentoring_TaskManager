package com.example.taskmanager.repository;

import com.example.taskmanager.model.Category;
import com.example.taskmanager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByCategory(Category category);

    @Query(value= "{category: ?0}",fields = "{subtasks: 1}")
    List<Task> findSubTaskByCategory(Category category);

    @Query("{ 'description' : { $regex: ?0 } }")
    List<Task> findTasksByDescription(String regexp);

    @Query("{ 'subtasks.subTaskName': ?0}")
    List<Task> findTasksBySubtaskName(String subtaskName);

    @Query("{ deadline: {$lt : ?0}}")
    List<Task> findByDeadlineBefore(Date date);

}
