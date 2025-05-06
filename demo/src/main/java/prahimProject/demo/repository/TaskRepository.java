package prahimProject.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import prahimProject.demo.model.Task;
import prahimProject.demo.model.TaskStatus;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    //all these methods are automatically implemented by Spring Data JPA!!! (YOUPPIEEE)

    //find all tasks by a certain status
    List<Task> findByStatus(TaskStatus status);

    //find tasks with priority greated than a specified value
    List<Task> findByPriorityGreaterThanEqual(int priority);

    //find pending tasks ordered by priority (highest first) and due date (earliest first)
    List<Task> findByStatusOrderByPriorityDescDueDateAsc(TaskStatus status);

    //find tasks due before a certain date
    List<Task> findByDueDateBefore(LocalDateTime dueDate);

    //find tasks by status and priority
    List<Task> findByStatusAndPriority(TaskStatus status, int priority);

    //find task by id
    Task findById(long id);

    //remove task by id
    void deleteById(long id);

    //update tasks status by id
    void updateStatusById(TaskStatus status, long id);

    /*TODO: add queries that do:
    1. find the next batch of tasks to process based on priority and deadline
    2. get statistics about tasks (count by status, average completion time, etc.)
    3. find tasks that might be stuck (in PROCESSING state for too long)
    */
    //find the next batch of tasks to process based on priority and deadline
    @Query("SELECT t FROM Task t WHERE t.status = ?1 AND t.priority >= ?2 ORDER BY t.priority DESC, t.dueDate ASC")
    List<Task> findHighPriorityTasks(TaskStatus status, int minPriority);
    //get stats about tasks (count by status, average completion time, etc.)
    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> getTaskCountsByStatus();
    //find tasks that might be stuck (in PROCESSING state for too long)
    @Query("SELECT t FROM Task t WHERE t.status = 'PROCESSING' AND t.lastUpdated < :cutoffTime")
    List<Task> findStuckTasks(@Param("cutoffTime") LocalDateTime cutoffTime);
}
