package prahimProject.demo.service;

import java.util.PriorityQueue;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

// Define or import the Task class
import prahimProject.demo.model.Task;
import prahimProject.demo.util.TaskPriorityComparator;

    /*
     * has a private field taskQueue of type priority queue that stores tasks
     * has a public method addTask that adds a task to the queue based on its priority(void)
     * has a public method getNextTask that retrieves and removes the task with the highest priority (Task)
     * has a public method size that returns the number of tasks in the queue(int)
     */

@Service
public class PriorityScheduler {

    // private final field taskQueue of type priority queue that stores tasks
    private final PriorityQueue<Task> taskQueue;
    private final TaskPriorityComparator comparator;
    
    //constructor that initializes the taskQueue with a custom comparator
    public PriorityScheduler() {
        this.comparator = new TaskPriorityComparator();
        this.taskQueue = new PriorityQueue<Task>(comparator);
    }
    //methods
    public void addTask(Task task){
        //adds a task to the queue based on its priority
        taskQueue.add(task);
    }
    public Task peekNextTask(){
        //retrieves the task with the highest priority without removing it
        return taskQueue.peek();
    }
    public Task getNextTask(){
        //retrieves and removes the task with the highest priority
        return taskQueue.poll();
    }
    public List<Task> getAllTasks(){
        //return all current tasks in the queue as a list
        List<Task> taskList = new ArrayList<>(taskQueue);
        //sort them
        taskList.sort(comparator);
        return taskList;
    }
    public int size(){
        //returns the number of tasks in the queue
        return taskQueue.size();
    }
    public boolean isEmpty(){
        //returns true if the queue is empty, false otherwise
        return taskQueue.isEmpty();
    }
    public void clear(){
        //clears the queue
        taskQueue.clear();
    }
    public boolean removeTask(Task task){
        //removes a specific task from the queue
        //priority queue does not support remove directly, so we need to create a new queue without the task
        PriorityQueue<Task> newQueue = new PriorityQueue<>(comparator);
        //boolean switch
        boolean found = false;
        for(Task t : taskQueue){ //worst case O(n)
            if(t.getId() == task.getId()){
                //this is the task we want to remove, skip it
                found = true;
                continue;
            }
            else{
                //add the task to the new queue
                newQueue.add(t);
            }
        }
        if(found){
            //clear the old queue and set it to the new queue
            taskQueue.clear();
            taskQueue.addAll(newQueue);
        }
        return found;
    }
}
