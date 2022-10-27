package com.example.practice.domain;

public class MemberDTO {private String name;
    private String email;
    private String organization;

    public MemberDTO(String name, String email, String organization) {
        this.name = name;
        this.email = email;
        this.organization = organization;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", this.name, this.email, this.organization);
    }

}
