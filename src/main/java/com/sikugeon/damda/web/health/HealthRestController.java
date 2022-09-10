package com.sikugeon.damda.web.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

    @GetMapping("/")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }

    @GetMapping("/home")
    public ResponseEntity<String> path() {
        String res = System.getenv("spring.profiles.active") + "3";
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(res);
    }
}
