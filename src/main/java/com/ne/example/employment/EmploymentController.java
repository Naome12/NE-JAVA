package com.ne.example.employment;

import com.ne.example.employment.dto.EmploymentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employments")
@RequiredArgsConstructor
public class EmploymentController {
    private final EmploymentService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody EmploymentRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<?>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getById(@PathVariable Long code) {
        return ResponseEntity.ok(service.getById(code));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable Long code, @RequestBody EmploymentRequestDto dto) {
        return ResponseEntity.ok(service.update(code, dto));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> delete(@PathVariable Long code) {
        service.delete(code);
        return ResponseEntity.ok("Deleted successfully");
    }
}
