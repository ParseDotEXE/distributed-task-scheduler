# Distributed Task Scheduler with Priority Queuing 

A web-based Java made task scheduler where users can submit tasks with priorities and deadlines. The system processes tasks in a distributed manner, ensuring high-priority tasks are handled first.
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Java Version](https://img.shields.io/badge/Java-17-blue)

## Architecture

This project uses:
- Java with Spring Boot for RESTful APIs
- AWS SQS for priority-based task queuing
- AWS ECS/Lambda for distributed task processing
- PostgreSQL for task metadata storage

### Why SQS over SNS?
SQS was chosen because it provides a queue-based system that allows for tasks to be processed in order based on priority. SNS is more suited for broadcasting messages to multiple subscribers, which doesn't align with our goal of controlled task processing.

## Setup Instructions

1. Clone this repository
2. Configure database settings in `application.properties`
3. Set up AWS credentials for SQS and ECS/Lambda
4. Run with `./mvnw spring-boot:run`

## Challenges and Solutions

* **Challenge**: Handling priority collisions
  * **Solution**: Implemented secondary sorting by deadline to ensure tasks with equal priority are processed in order of urgency

## Development Timeline

This project is being developed according to a 3-month plan:
- Month 1: Foundation (System design, Spring Boot setup)
- Month 2: Core Implementation (SQS integration, priority logic)
- Month 3: Polish & Deployment
