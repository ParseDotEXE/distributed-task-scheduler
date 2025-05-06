package prahimProject.demo.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {
    @GetMapping("/greet")// This endpoint will be accessible at http://localhost:8080/greet
    public String greetTheUser(){
        return "Hello, user!";
    }
    @GetMapping("/")
    public String root(){
        return "This is the root endpoint. You can access the greet endpoint at /greet";
    }
}
