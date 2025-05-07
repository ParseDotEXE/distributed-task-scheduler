package prahimProject.demo.util;

import prahimProject.demo.model.Task;
import java.util.Comparator;

public class TaskPriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task task1, Task task2){
        //TODO: implement the compare method to compare tasks based on their priority and due date
        //higher priority first, then earlier due date first
        return 1;
    }
}
