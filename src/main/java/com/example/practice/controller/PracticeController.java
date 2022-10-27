package com.example.practice.controller;

import com.example.practice.domain.MemberDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/get-api")
public class PracticeController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello world";
    }

    @GetMapping(value = "/name")
    public String getName() {
        return "nnnnname";
    }

    // http://localhost:8080/api/v1/get-api/variable1/{String 값}
    // 위 요청 url 보면 이 메서드는 String값을 받아오는 요청을 하고 있음. 값을 간단히 전달할 때 주로 사용하며, 겟 요청에서 많이 사용된다.
    @GetMapping(value = "/variable1/{variable}") //여기 중괄호 {} 안의 값이랑
    public String getVariable1(@PathVariable String variable) { //여기 @PathVariable 에 선언한 변수랑 이름이 같아야 함.
        return variable;
    }

    // http://localhost:8080/api/v1/get-api/variable2/{String 값}
    @GetMapping(value = "/variable2/{variable}")
    public String getVariable2(@PathVariable("variable") String var) {
        // getMapping에 지정한(중괄호 안 값) 변수명이랑 메서드 매개변수 이름(String var)을 동일하게 맞추기 어려우면
        // @PathVariable 뒤에 괄호 열어서 변수명 지정해주면 됨.
        return var;
    }

    // @RequestParam을 활용한 get 메서드 구현
    @GetMapping(value = "/request1")
    public String getRequestParam1(@RequestParam String name, @RequestParam String email, @RequestParam String organization) {

        return String.format("%s %s %s", name, email, organization);
    }

    @GetMapping(value = "/request2")
    public String getRequestParam2(@RequestParam Map<String, String> param) {

        StringBuilder sb = new StringBuilder();
        param.entrySet().forEach(map -> {
            sb.append(map.getKey() + " :" + map.getValue() + "\n");
        });

        return sb.toString();
    }

    @GetMapping(value = "/request3")
    public String getRequestParam3(MemberDTO memberDTO) {
        memberDTO.setName("jhjh");
        memberDTO.setEmail("jh@jhjh");
        memberDTO.setOrganization("lion");
        return memberDTO.toString();
    }
}
