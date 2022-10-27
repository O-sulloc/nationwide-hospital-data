package com.example.practice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/get-api")
public class PracticeController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(){
        return "hello world";
    }

    @GetMapping(value = "/name")
    public String getName(){
        return "nnnnname";
    }

    @GetMapping(value = "/variable1/{varaible}")
    public String getVariable1(@PathVariable String varaible){
        return varaible;
    }

    @GetMapping(value = "/variable2/{varaible}")
    public String getVariable2(@PathVariable String var) {
        return var;
    }

    @GetMapping(value = "/request1")
    public String getVariable2(@RequestParam String name, @RequestParam String email, @RequestParam String organization){

        return String.format("%s %s %s", name, email, organization);
    }
}
