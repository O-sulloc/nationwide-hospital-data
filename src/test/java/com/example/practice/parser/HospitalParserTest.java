package com.example.practice.parser;

import com.example.practice.dao.HospitalDAO;
import com.example.practice.domain.Hospital;
import com.example.practice.service.HospitalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HospitalParserTest {

    String line1 = "\"1\",\"의원\",\"01_01_02_P\",\"3620000\",\"PHMA119993620020041100004\",\"19990612\",\"\",\"01\",\"영업/정상\",\"13\",\"영업중\",\"\",\"\",\"\",\"\",\"062-515-2875\",\"\",\"500881\",\"광주광역시 북구 풍향동 565번지 4호 3층\",\"광주광역시 북구 동문대로 24, 3층 (풍향동)\",\"61205\",\"효치과의원\",\"20211115113642\",\"U\",\"2021-11-17 02:40:00.0\",\"치과의원\",\"192630.735112\",\"185314.617632\",\"치과의원\",\"1\",\"0\",\"0\",\"52.29\",\"401\",\"치과\",\"\",\"\",\"\",\"0\",\"0\",\"\",\"\",\"0\",\"\",";

    @Autowired
    ReadLineContext<Hospital> hospitalReadLineContext;

    @Autowired
    HospitalDAO hospitalDAO;

    @Autowired
    HospitalService hospitalService;

    //@Test
    void deleteAndCount() {
        HospitalParser hp = new HospitalParser();
        hospitalDAO.deleteAll();
        int result = hospitalDAO.getCount();
        System.out.println(result);
    }

    //@Test
    void addAndGet() {
        HospitalParser hp = new HospitalParser();

        hospitalDAO.deleteAll();
        int result = hospitalDAO.getCount();

        Hospital hospital = hp.parse(line1);

        //hospitalDAO.add(hospital);

        Hospital selected = hospitalDAO.findById(hospital.getId());
        assertEquals(selected.getId(), hospital.getId());
        assertEquals(selected.getOpenServiceName(), hospital.getOpenServiceName());
        assertEquals(selected.getHospitalName(), hospital.getHospitalName());

        //날짜 Float test
        assertEquals(selected.getLicenseDate(), hospital.getLicenseDate());

        assertEquals(selected.getTotalAreaSize(), hospital.getTotalAreaSize());
    }

    @Test
    @DisplayName("전국 병의원 데이터 DB 입력")
    void addDB() throws IOException {

        long startTime = System.currentTimeMillis();

        hospitalDAO.deleteAll();

        String fileName = "/Users/jeonghyeonkim/Downloads/fulldata_01_01_02_P_utf8.csv";
        int cnt = this.hospitalService.insertLargeVolumeHospitalData(fileName);
        List<Hospital> hospitalList = hospitalReadLineContext.readByLine(fileName);

        long endTime = System.currentTimeMillis();

        long time = (endTime - startTime) / 1000;
        System.out.println("소요 시간: " + time + "초");

        assertTrue(hospitalList.size() > 1000);
        assertTrue(hospitalList.size() > 10000);
        
    }

    //@Test
    @DisplayName("csv 1줄 hospital로 잘 만드는지 테스트")
    void convertToHospital() {
        HospitalParser hp = new HospitalParser();
        Hospital hospital = hp.parse(line1);

        assertEquals(1, hospital.getId());
        assertEquals("의원", hospital.getOpenServiceName());
        assertEquals(3620000, hospital.getOpenLocalGovernmentCode());
        assertEquals("PHMA119993620020041100004", hospital.getManagementNumber());
        assertEquals(LocalDateTime.of(1999, 6, 12, 0, 0, 0), hospital.getLicenseDate()); //19990612
        assertEquals(1, hospital.getBusinessStatus());
        assertEquals(13, hospital.getBusinessStatusCode());
        assertEquals("062-515-2875", hospital.getPhone());
        assertEquals("광주광역시 북구 풍향동 565번지 4호 3층", hospital.getFullAddress());
        assertEquals("광주광역시 북구 동문대로 24, 3층 (풍향동)", hospital.getRoadNameAddress());
        assertEquals("효치과의원", hospital.getHospitalName());
        assertEquals("치과의원", hospital.getBusinessTypeName());
        assertEquals(1, hospital.getHealthcareProviderCount());
        assertEquals(0, hospital.getPatientRoomCount());
        assertEquals(0, hospital.getTotalNumberOfBeds());
        assertEquals(52.29f, hospital.getTotalAreaSize());

    }
}