package com.example.practice.dao;

import com.example.practice.domain.Hospital;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class HospitalDAO {

    private final JdbcTemplate jdbcTemplate;

    public HospitalDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Hospital> rowMapper = (rs, rowNum) -> {
      Hospital hospital = new Hospital();

      hospital.setId(rs.getInt("id"));
      hospital.setOpenServiceName(rs.getString("open_service_name"));
      hospital.setOpenLocalGovernmentCode(rs.getInt("open_local_government_code"));
      hospital.setManagementNumber(rs.getString("management_number"));
      hospital.setLicenseDate(rs.getTimestamp("license_date").toLocalDateTime());
      hospital.setBusinessStatus(rs.getInt("business_status"));
      hospital.setBusinessStatusCode(rs.getInt("business_status_code"));
      hospital.setPhone(rs.getString("phone"));
      hospital.setFullAddress(rs.getString("full_address"));
      hospital.setRoadNameAddress(rs.getString("road_name_address"));
      hospital.setHospitalName(rs.getString("hospital_name"));
      hospital.setBusinessTypeName(rs.getString("business_type_name"));
      hospital.setHealthcareProviderCount(rs.getInt("healthcare_provider_count"));
      hospital.setPatientRoomCount(rs.getInt("patient_room_count"));
      hospital.setTotalNumberOfBeds(rs.getInt("total_number_of_beds"));
      hospital.setTotalAreaSize(rs.getFloat("total_area_size"));

      return hospital;
    };
    public Hospital findById(int id){
        return this.jdbcTemplate.queryForObject("select * from nation_wide_hospital where id = ?", rowMapper, id);
    }

    public void deleteAll(){
        this.jdbcTemplate.update("delete from nation_wide_hospital");
    }

    public int getCount(){
        String sql="select count(id) from nation_wide_hospital";
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void add(List<Hospital> hospitalList){
        String sql ="INSERT INTO `hospital`.`nation_wide_hospital` \n" +
                "(`id`, `open_service_name`, `open_local_government_code`, `management_number`, `license_date`, " +
                "`business_status`, `business_status_code`, `phone`, `full_address`, `road_name_address`, `hospital_name`, " +
                "`business_type_name`, `healthcare_provider_count`, `patient_room_count`, `total_number_of_beds`, `total_area_size`) \n" +
                "VALUES (?,?,?," +
                "?,?,?," +
                "?,?,?," +
                "?,?,?," +
                "?,?,?," +
                "?);";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Hospital hospital = hospitalList.get(i);

                int index = 1;
                ps.setInt(index++, hospital.getId());
                ps.setString(index++, hospital.getOpenServiceName());
                ps.setInt(index++, hospital.getOpenLocalGovernmentCode());
                ps.setString(index++, hospital.getManagementNumber());
                ps.setString(index++, hospital.getLicenseDate().toString());
                ps.setInt(index++, hospital.getBusinessStatus());
                ps.setInt(index++, hospital.getBusinessStatusCode());
                ps.setString(index++, hospital.getPhone());
                ps.setString(index++, hospital.getFullAddress());
                ps.setString(index++, hospital.getRoadNameAddress());
                ps.setString(index++, hospital.getHospitalName());
                ps.setString(index++, hospital.getBusinessTypeName());
                ps.setInt(index++, hospital.getHealthcareProviderCount());
                ps.setInt(index++, hospital.getPatientRoomCount());
                ps.setInt(index++, hospital.getTotalNumberOfBeds());
                ps.setFloat(index++, hospital.getTotalAreaSize());

            }

            @Override
            public int getBatchSize() {
                return hospitalList.size();
            }
        });
    }
}
