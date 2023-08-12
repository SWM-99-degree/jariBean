package com.example.jariBean.repository.cafe;


import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.repository.cafe.elasticcafe.CafeSearchRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.tableClass.TableClassRepository;
import com.example.jariBean.repository.user.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
//@ActiveProfiles("test")
class CafeRepositoryTest {

    @Autowired CafeRepository cafeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TableRepository tableRepository;
    @Autowired
    CafeOperatingTimeRepository cafeOperatingTimeRepository;
    @Autowired
    ReservedRepository reservedRepository;
    @Autowired
    TableClassRepository tableClassRepository;

    @Autowired
    CafeSearchRepository cafeSearchRepository;

    private final String cafeName = "(주)커피지아";
    private final String cafeAddress = "서울특별시 서초구 강남대로 27, AT센터 제1전시장 (양재동)";

    @Test
    public void searchingByELKTest() {
        List<String> searchingWords = new ArrayList<>();
        searchingWords.add("미추홀");


        List<String> lst = cafeSearchRepository.findBySearchingWord(searchingWords, 37.4467039276238, 37.4467039276238);
        System.out.println(lst);
    }

    @Test
    public void saveCafesTable() {
        List<String> cafes = new ArrayList<>();
        cafes.add("64c45ac3935eb61c140793e8");
        cafes.add("64c45ac3935eb61c140793eb");
        cafes.add("64c45ac3935eb61c140793e9");
        cafes.add("64c45ac3935eb61c140793ed");
        for (String cafe : cafes) {
            List<TableClass.TableOption> options = new ArrayList<>();
            options.add(TableClass.TableOption.PLUG);
            options.add(TableClass.TableOption.RECTANGLE);
            Table table = new Table();
            table.setCafeId(cafe);
            table.setTableOptionList(options);
            tableRepository.save(table);
        }

    }


    @Test
    public void saveCafesReserved() {
        List<String> cafes = new ArrayList<>();
        cafes.add("64c45ac3935eb61c140793e8");
//        cafes.add("64c45ac3935eb61c140793eb");
//        cafes.add("64c45ac3935eb61c140793e9");
//        cafes.add("64c45ac3935eb61c140793ed");
        for (String cafe : cafes) {
            Table table = tableRepository.findByCafeId(cafe).get(0);
            LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 15, 0);
            LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 17, 0);

            Reserved reserved = new Reserved("123", new Cafe(), table, dateTime1, dateTime2);
            reservedRepository.save(reserved);

        }

    }



    @Test
    public void saveCafesTest() throws Exception {
        try {
            String filePath = "/Users/kisung/Desktop/cafes.xlsx";
            FileInputStream fileInputStream = new FileInputStream(filePath);

            // 엑셀 워크북 객체 생성
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            // 첫 번째 시트 선택
            Sheet sheet = workbook.getSheetAt(0);

            // 읽어올 행 번호 (0부터 시작)
            int startRowNumber = 2;

            // 선택한 행 객체 가져오기
            for (int rowNum = startRowNumber; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                String name = row.getCell(1).getStringCellValue();
                String address = row.getCell(2).getStringCellValue();
                double latitude = row.getCell(4).getNumericCellValue();
                double longitude = row.getCell(5).getNumericCellValue();
                GeoJsonPoint jsonPoint = new GeoJsonPoint(latitude,longitude);
                Cafe cafe = Cafe.builder()
                        .name(name)
                        .address(address)
                        .coordinate(jsonPoint)
                        .build();
                cafeRepository.save(cafe);
                }

            // 파일 읽기 완료 후 리소스 해제
            fileInputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void saveAndDeleteTest() throws Exception {
        // given
        String cafeName = "testCafeName";
        String cafePhoneNumber = "12345678910";
        String cafePassword = "1234567890";
        String cafeAddress = "testCafeAddress";
        GeoJsonPoint jsonPoint = new GeoJsonPoint(12.123123,13.131313);
        Cafe cafe = Cafe.builder()
                .name(cafeName)
                .phoneNumber(cafePhoneNumber)
                .description(" this is so beautiful caffee")
                .address("newdjf")
                .coordinate(jsonPoint)
                .build();
        // when
        Cafe savedCafe = cafeRepository.save(cafe);

        //then
//        assertThat(savedCafe.getCafeName()).isEqualTo(cafeName);
//        assertThat(savedCafe.getCafePhoneNumber()).isEqualTo(cafePhoneNumber);
//        assertThat(savedCafe.getCafePassword()).isEqualTo(cafePassword);
//        assertThat(savedCafe.getCafeAddress()).isEqualTo(cafeAddress);

        // delete
        //cafeRepository.deleteById(savedCafe.getId());
    }

    @Test
    public void saveAllAndDeleteAllTest() throws Exception {
        // given
        String cafeName = "testCafeName";
        String cafePhoneNumber = "12345678910";
        String cafePassword = "1234567890";
        String cafeAddress = "testCafeAddress";

        List<Cafe> cafeList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Cafe newCafe = Cafe.builder()
                    .name(cafeName)
                    .phoneNumber(cafePhoneNumber)
                    .address(cafeAddress)
                    .build();
            cafeList.add(newCafe);
        }

        // when
        List<Cafe> savedCafeList = cafeRepository.saveAll(cafeList);

        // delete
        List<String> savedCafeIdList = savedCafeList.stream().map(Cafe::getId).collect(Collectors.toList());
        cafeRepository.deleteAllById(savedCafeIdList);
    }



    @Test
    public void findByCoordinateNearTest() throws Exception {
        String word = "무인";
        List<String> words = new ArrayList<>();
        words.add(word);

        GeoJsonPoint coordinate = new GeoJsonPoint(126.9841888305794, 37.53697168039137);
        Double distance = 50D;

        List<String> cafeList = cafeRepository.findByWordAndCoordinateNear(words,coordinate);
        System.out.println("byCoordinateDistance.getContent().size() = " + cafeList.size());
        cafeList.forEach(cafe -> {
            System.out.println(cafe);
        });
    }


    @Test
    public void findAllTest() throws Exception {
        List<Cafe> cafeList = cafeRepository.findAll();
        System.out.println("cafeList.size() = " + cafeList.size());
    }


}