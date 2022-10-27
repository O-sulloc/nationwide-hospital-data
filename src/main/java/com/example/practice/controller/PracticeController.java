package com.example.practice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PracticeController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }

    @GetMapping(value = "/request1")
    public String getVariable2(@RequestParam String name, @RequestParam String email){

        return String.format("%s %s", name, email);
    }
}
