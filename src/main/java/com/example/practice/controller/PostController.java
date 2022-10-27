package com.example.practice.controller;

import com.example.practice.domain.MemberDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/post-api")
public class PostController {

    @RequestMapping(value = "/domain", method = RequestMethod.POST)
    public String postExample(){
        return "hello post api";
    }

    @PostMapping(value = "/member")
    public String postMember(@RequestBody Map<String, Object> postData){
        StringBuilder sb = new StringBuilder();

        postData.entrySet().forEach(map -> {
            sb.append(map.getKey()+" : " + map.getValue() + "\n");
        });

        return  sb.toString();
    }

    @PostMapping("/member2")
    public String postMember(@RequestBody MemberDTO memberDto){
        return memberDto.toString();
    }

    @PostMapping("/member3")
    public ResponseEntity<MemberDTO> putMember(@RequestBody MemberDTO memberDto) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(memberDto);
    }
}
