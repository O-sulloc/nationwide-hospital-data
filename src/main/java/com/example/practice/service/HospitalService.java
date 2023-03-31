package com.example.practice.service;

import com.example.practice.dao.HospitalDAO;
import com.example.practice.domain.Hospital;
import com.example.practice.parser.ReadLineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
public class HospitalService {

    private final ReadLineContext<Hospital> readLineContext;
    private final HospitalDAO hospitalDAO;


    public HospitalService(ReadLineContext<Hospital> readLineContext, HospitalDAO hospitalDAO) {
        this.readLineContext = readLineContext;
        this.hospitalDAO = hospitalDAO;
    }

    @Transactional
    public int insertLargeVolumeHospitalData(String filename) {
        int cnt = 0;
        try {
            List<Hospital> hospitalList = readLineContext.readByLine(filename);

            try {
                this.hospitalDAO.add(hospitalList);
                cnt++;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return cnt;
    }

}
