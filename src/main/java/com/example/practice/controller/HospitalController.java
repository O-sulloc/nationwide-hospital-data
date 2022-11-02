package com.example.practice.controller;
import com.example.practice.dao.HospitalDAO;
import com.example.practice.domain.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hospital")
public class HospitalController {
    private final HospitalDAO hospitalDAO;

    @Autowired
    public HospitalController(HospitalDAO hospitalDAO) {
        this.hospitalDAO = hospitalDAO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hospital> get(@PathVariable Integer id) {
        try {
            Hospital hospital = this.hospitalDAO.findById(id);
            return ResponseEntity
                    .ok()
                    .body(hospital);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

