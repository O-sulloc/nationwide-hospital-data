package com.example.practice.parser;

import com.example.practice.domain.Hospital;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadLineContext<T> {

    private Parser<Hospital> parser;

    public ReadLineContext(Parser<Hospital> parser) {
        this.parser = parser;
    }

    public List<Hospital> readByLine(String filename) throws IOException {

        // hospital 객체 받을 list 초기화
        List<Hospital> hospitalList = new ArrayList<>();

        BufferedReader reader = new BufferedReader(
                new FileReader(filename)
        );

        String str;

        while ((str = reader.readLine()) != null) {
            try {
                Hospital hospital = parser.parse(str);
                hospitalList.add(hospital);
            } catch (Exception e) {
                System.out.printf("파싱중 문제가 생겨 이 라인은 넘어갑니다. 파일내용:%s\n", str.substring(0, 20));
            }
        }
        reader.close();
        return hospitalList;
    }
}
