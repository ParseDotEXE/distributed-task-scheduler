# Distributed Task Scheduler with Priority Queuing

A web-based task scheduling system built with Java that allows users to submit tasks with priorities and deadlines. The system processes tasks in a distributed manner, ensuring high-priority tasks are handled first.

## 🏗️ Architecture

This project implements a distributed architecture with the following components:

- **Java with Spring Boot** for RESTful APIs and core business logic
- **AWS SQS** for priority-based task queuing
- **AWS ECS/Lambda** for distributed task processing
- **PostgreSQL** for task metadata storage

### System Design

![System Architecture](https://placeholder-for-architecture-diagram.com/diagram.png)

### Why SQS over SNS?

SQS was chosen because it provides a queue-based system that allows for tasks to be processed in order based on priority. SNS is more suited for broadcasting messages to multiple subscribers, which doesn't align with our goal of controlled task processing.

## 🚀 Features

- **Priority-based Task Scheduling**: Tasks with higher priority are processed first
- **Deadline Management**: Secondary sorting by deadline for equal-priority tasks
- **Task Status Tracking**: Monitor tasks through their lifecycle
- **Distributed Processing**: Scale horizontally to handle increased load
- **RESTful API**: Easy integration with various clients

## 🛠️ Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat-square&logo=amazon-aws&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)

## 🧩 Core Components

### Domain Model

```java
public class Task implements Comparable<Task> {
    private Long id;
    private String description;
    private TaskPriority priority;
    private LocalDateTime deadline;
    private TaskStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifiedDate;
    
    @Override
    public int compareTo(Task other) {
        // Primary sort by priority
        int priorityComparison = this.priority.compareTo(other.priority);
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        
        // Secondary sort by deadline
        return this.deadline.compareTo(other.deadline);
    }
}
```

### Service Layer

The service layer handles business logic:

```java
@Service
public class TaskSubmissionService {
    private final TaskRepository taskRepository;
    
    @Autowired
    public TaskSubmissionService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    
    public Task submitTask(Task task) {
        // Set initial status and creation timestamp
        task.setStatus(TaskStatus.PENDING);
        task.setCreationDate(LocalDateTime.now());
        
        // Save to database
        return taskRepository.save(task);
    }
    
    // Other task management methods
}
```

```java
@Service
public class PriorityScheduler {
    private final TaskPriorityComparator priorityComparator;
    private final TaskRepository taskRepository;
    
    @Autowired
    public PriorityScheduler(TaskPriorityComparator priorityComparator,
                            TaskRepository taskRepository) {
        this.priorityComparator = priorityComparator;
        this.taskRepository = taskRepository;
    }
    
    public List<Task> getNextBatchOfTasks(int batchSize) {
        // Get pending tasks and sort by priority
        List<Task> pendingTasks = taskRepository.findByStatus(TaskStatus.PENDING);
        Collections.sort(pendingTasks, priorityComparator);
        
        // Return batch of highest priority tasks
        return pendingTasks.stream()
                .limit(batchSize)
                .collect(Collectors.toList());
    }
    
    // Other scheduling methods
}
```

### Queue Management

```java
@RestController
@RequestMapping("/api/queue")
public class QueueController {
    private final AmazonSQS sqsClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public QueueController(AmazonSQS sqsClient, 
                          @Value("${aws.sqs.queueUrl}") String queueUrl,
                          ObjectMapper objectMapper) {
        this.sqsClient = sqsClient;
        this.queueUrl = queueUrl;
        this.objectMapper = objectMapper;
    }
    
    @PostMapping("/enqueue")
    public ResponseEntity<String> enqueueTask(@RequestBody Task task) throws JsonProcessingException {
        // Convert task to JSON
        String taskJson = objectMapper.writeValueAsString(task);
        
        // Send to SQS
        SendMessageRequest request = new SendMessageRequest()
            .withQueueUrl(queueUrl)
            .withMessageBody(taskJson)
            .withMessageGroupId(task.getPriority().toString());
            
        SendMessageResult result = sqsClient.sendMessage(request);
        return ResponseEntity.ok("Task enqueued with ID: " + result.getMessageId());
    }
    
    // Methods for polling, queue status, etc.
}
```

## 📋 Project Structure

```
src/
├── apis/
│   ├── GreetController.java
│   ├── QueueController.java
│   ├── SchedulerController.java
│   ├── TaskController.java
│   └── TaskStatusController.java
├── model/
│   ├── Task.java
│   └── TaskStatus.java
├── repository/
│   └── TaskRepository.java
├── service/
│   ├── PriorityScheduler.java
│   └── TaskSubmissionService.java
└── util/
    └── TaskPriorityComparator.java
│   └── resources/
│       ├── application.properties
│       └── templates/
├── test/
│   └── java/
└── pom.xml
```

## ⚙️ Setup Instructions

1. Clone this repository
   ```bash
   git clone https://github.com/yourusername/distributed-task-scheduler.git
   cd distributed-task-scheduler
   ```

2. Configure database settings in `application.properties`
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/taskdb
   spring.datasource.username=postgres
   spring.datasource.password=password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Set up AWS credentials
   ```properties
   aws.accessKey=your_access_key
   aws.secretKey=your_secret_key
   aws.region=us-east-1
   aws.sqs.highPriorityQueue=high-priority-task-queue
   aws.sqs.mediumPriorityQueue=medium-priority-task-queue
   aws.sqs.lowPriorityQueue=low-priority-task-queue
   ```

4. Run with Maven
   ```bash
   ./mvnw spring-boot:run
   ```

## 📡 API Endpoints

| Endpoint | Controller | Method | Description |
|----------|------------|--------|-------------|
| `/api/greet` | GreetController | GET | Health check endpoint |
| `/api/tasks` | TaskController | POST | Submit a new task |
| `/api/tasks` | TaskController | GET | Retrieve all tasks |
| `/api/tasks/{id}` | TaskController | GET | Get a specific task |
| `/api/tasks/{id}` | TaskController | PUT | Update a task |
| `/api/tasks/status/{id}` | TaskStatusController | GET | Get task status |
| `/api/tasks/status/{id}` | TaskStatusController | PATCH | Update task status |
| `/api/scheduler/status` | SchedulerController | GET | Get scheduler status |
| `/api/scheduler/pause` | SchedulerController | POST | Pause the scheduler |
| `/api/scheduler/resume` | SchedulerController | POST | Resume the scheduler |
| `/api/queue/enqueue` | QueueController | POST | Manually enqueue a task |
| `/api/queue/status` | QueueController | GET | Get queue statistics |

## 🧪 Testing

Run the test suite with:

```bash
./mvnw test
```

## 🚧 Challenges and Solutions

* **Challenge**: Handling priority collisions
  
  **Solution**: Implemented secondary sorting by deadline to ensure tasks with equal priority are processed in order of urgency

* **Challenge**: Maintaining task order while scaling horizontally
  
  **Solution**: Used SQS FIFO queues with priority-based message groups

* **Challenge**: Task failure handling
  
  **Solution**: Implemented dead-letter queues and automated retry mechanisms with exponential backoff

## ⏱️ Development Timeline

This project is being developed according to a 3-month plan:

- **Month 1**: Foundation (System design, Spring Boot setup)
- **Month 2**: Core Implementation (SQS integration, priority logic)
- **Month 3**: Polish & Deployment

## 📊 Performance Considerations

- SQS long polling to reduce API calls
- Connection pooling for database access
- Caching frequently accessed task metadata
- Task batching for improved throughput

## 🔒 Security Features

- JWT-based API authentication
- Encrypted task data in transit and at rest
- IAM role-based access for AWS services
- Parameterized queries to prevent SQL injection

## 🔄 Data Flow

1. **Task Submission Flow**:
   ```
   Client → TaskController → TaskSubmissionService → 
   TaskRepository (saves to database) → QueueController (enqueues to SQS)
   ```

2. **Task Scheduling Flow**:
   ```
   QueueController (polls SQS) → PriorityScheduler (uses TaskPriorityComparator) → 
   SchedulerController (manages task execution)
   ```

3. **Task Status Management Flow**:
   ```
   SchedulerController → TaskStatusController → 
   TaskRepository (updates status in database)
   ```

4. **Status Retrieval Flow**:
   ```
   Client → TaskStatusController → TaskRepository → Response to Client
   ```

5. **Greeting/Health Check Flow**:
   ```
   Client → GreetController → Simple response (for health checks/API testing)
   ```

## ⚠️ Usage Notice

This project is a personal portfolio project and is not accepting contributions at this time. It is provided for educational and demonstration purposes only.

---

Project developed by Parsa Rahimnia
