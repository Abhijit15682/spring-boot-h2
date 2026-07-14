package org.rest.secure_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
@PreAuthorize("hasRole('USER')")
public class DataController {

    @GetMapping("/summary")
    public ResponseEntity<?> getSummaryData() {
        // This endpoint will return data if the Bearer JWT token passes validation checks
        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Authenticated data payload read successfully from H2 context store!"
        ));
    }
}
