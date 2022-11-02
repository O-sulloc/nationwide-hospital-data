package com.example.practice.controller;

import com.example.practice.dao.HospitalDAO;
import com.example.practice.domain.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
        Hospital hospital = hospitalDAO.findById(id);
        Optional<Hospital> opt = Optional.of(hospital);

        if (!opt.isEmpty()) {
            return ResponseEntity.ok().body(hospital);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Hospital());

        }

    }
}
