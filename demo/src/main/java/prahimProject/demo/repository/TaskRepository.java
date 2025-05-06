package prahimProject.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prahimProject.demo.model.Task;
import prahimProject.demo.model.TaskStatus;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    //all these methods are automatically implemented by Spring Data JPA!!! (YOUPPIEEE)

    //find all tasks by a certain status
    List<Task> findByStatus(TaskStatus status);

    //find tasks with priority greated than a specified value
    
}
