# 전국 병·의원 데이터 파싱
> 약 11만 건에 달하는 대용량 데이터 처리 및 가공 경험.
---

[공공데이터](https://www.data.go.kr/data/15045024/fileData.do)가 제공하는 전국 의료기관 CSV 데이터를 파싱하여 MySql DB에 저장하였다.

<details>
<summary>DB 테이블 설계</summary>
<div>

| no | 컬럼명 | 타입 | 비고                               |
| --- | --- | --- |----------------------------------|
| 1 | id(pk) | Int | 번호                               |
| 2 | open_service_name | VARCHAR(10) | 개방서비스명                           |
| 3 | open_local_government_code | int | 개방자치단체코드                         |
| 4 | management_number(unique) | varchar(40) | 관리번호                             |
| 5 | license_date | datetime | 인허가일자                            |
| 6 | business_status | tinyint(2) | 1: 영업/정상2: 휴업3: 폐업4: 취소/말소영업상태구분 |
| 7 | business_status_code | tinyint(2) | 영업상태코드2: 휴업3: 폐업13: 영업중          |
| 8 | phone | varchar(20) | 소재지전화                            |
| 9 | full_address | VARCHAR(200) | 소재지전체주소                          |
| 10 | road_name_address | VARCHAR(200) | 도로명전체주소                          |
| 11 | hospital_name | VARCHAR(20) | 사업장명(병원이름)                       |
| 12 | business_type_name | VARCHAR(10) | 업태구분명                            |
| 13 | healthcare_provider_count | tinyint(2) | 의료인수                             |
| 14 | patient_room_count | tinyint(2) | 입원실수                             |
| 15 | total_number_of_beds | tinyint(2) | 병상수                              |
| 16 | total_area_size | float | 총면적                              |

</div>
</details>

## 파일 설명

| 내용                         |파일|
|----------------------------|----|
| csv 데이터 파일 파싱             |ReadLineContext, Parser, ParserFactory, HospitalParser|
| DB에 INSERT, DELETE, SELECT |Hospital, HospitalDao|
| REST API 적용                |SwaggerConfiguration, HospitalService, HospitalController|

## 진행 과정
### 1.BufferedReader

[ReadLineContext 소스 코드](https://github.com/O-sulloc/nationwide-hospital-data/blob/master/src/main/java/com/example/practice/parser/ReadLineContext.java)

입출력 속도가 빨라 대용량 파일을 읽을 때 유용한 BufferedReader 클래스를 사용했다. 해당 클래스를 사용한 readByLine 메서드로 텍스트 데이터를 한 줄씩 빠르게 읽어올 수 있었다.

### 2. HospitalParser
[HospitalParser 소스 코드](https://github.com/O-sulloc/nationwide-hospital-data/blob/master/src/main/java/com/example/practice/parser/HospitalParser.java)

BufferedReader로 읽어온 데이터 한 줄을 콤마 단위로 split 하여 필요한 정보만 hospital 객체에 담았다.

### 3. JdbcTemplate

[JdbcTemplate 소스 코드](https://github.com/O-sulloc/nationwide-hospital-data/blob/master/src/main/java/com/example/practice/dao/HospitalDAO.java)

Connection, Statement 객체의 생성/관리 작업을 개발자 대신 처리하여 코드 가독성과 효율성이 좋은 JDBC Template을 사용했다. JDBC Template의 update 메서드를 호출하여 DB에 병의원 정보를 입력했다.

## 보완 사항
![스크린샷 2023-03-27 오전 11 01 41](https://user-images.githubusercontent.com/96342941/229012778-a80965a9-af7d-4040-8714-31a5ba276317.png)

직접 실행해 본 결과, DB 삽입까지 대략 956초(15분 56초)가 소요되었다. update 메서드는 한 번에 한 개의 SQL 문만을 실행하므로, 대량의 데이터 처리 작업을 수행하기에는 부적합하다고 판단했다. 따라서 성능을 향상시킬 수 있는 다른 방안이 있는지 살펴보았다.

### 1. Bulk Insert
첫 번째로 고려한 방식은 Bulk Insert이다. 대용량 데이터 처리에 적합하며 CSV 파일 등의 데이터 파일을 읽어와 DB에 적재하는 방식으로 JDBC Template을 사용하거나 DBMS 내부에서 제공하는 Bulk Insert 기능을 사용하여 쉽게 구현할 수 있다.하지만 DB에 저장할 데이터 파일의 구조가 DB의 테이블 구조와 일치해야 한다는 점이 문제였다. 현재의 상황에서는 불필요한 데이터를 1차적으로 거르고 가공한 후에 DB에 저장해야 하기 때문에 Bulk Insert는 적절하지 않은 기술이라고 판단했다.    

### 2. BatchUpdate
데이터 처리 성능을 개선하기 위해 고민한 두 번째 방법은 Batch Update이다. JDBC Template에서 제공하는 batchUpdate() 메서드를 사용하면 여러 개의 SQL 문을 일괄 처리할 수 있다. 여러 개의 쿼리를 한 번의 요청으로 처리하여 네트워크 비용과 DBMS에 대한 부하를 줄일 수 있어, 데이터 처리 속도를 향상할 수 있을 것 같다.

조사한 사항을 바탕으로 BatchUpate를 적용하였고, 그 결과 DB 입력 속도를 15초로 향상시킬 수 있었다.

![스크린샷 2023-03-31 오전 11 40 53](https://user-images.githubusercontent.com/96342941/229013048-a7970f4a-af56-42c4-afdf-b802d903400b.png)
![스크린샷 2023-03-31 오전 11 39 30](https://user-images.githubusercontent.com/96342941/229013047-f37e0bb3-30ce-459d-86a5-b81d1a72572b.png)
