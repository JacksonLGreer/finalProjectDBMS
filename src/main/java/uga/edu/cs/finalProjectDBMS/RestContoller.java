package uga.edu.cs.finalProjectDBMS;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestContoller {
    
    @GetMapping("/")
    public String testName() {
        return "Jackson was here";
    }
}
