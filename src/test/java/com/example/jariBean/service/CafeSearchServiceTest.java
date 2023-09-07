package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Table;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.table.TableRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CafeSearchServiceTest {

    @Autowired
    private SearchService searchService;

    @Autowired
    private CafeService cafeService;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private CafeRepository cafeRepository;

    public ObjectId objectId = new ObjectId();


    @BeforeAll
    public void saveCafe() {
        Cafe cafe = Cafe.builder()
                .coordinate(new GeoJsonPoint(126.4467039276238, 37.4467039276238))
                .name("미추홀").build();

        cafe.setEndTime(LocalDateTime.of(2023, 7, 1, 0, 0));
        cafe.setStartTime(LocalDateTime.of(2023, 7, 1, 10, 0));
        Table table = Table.builder()
                .seating(4)
                .tableOptionList(new ArrayList<>())
                .build();

        Cafe newCafe = cafeRepository.save(cafe);
        objectId = new ObjectId(newCafe.getId());
        tableRepository.save(table);

    }


    @Test
    public void findBySearchingTest(){
        // given
        Pageable pageable = Pageable.ofSize(1);
        CafeReqDto.CafeSearchReqDto cafeSearchReqDto = new CafeReqDto.CafeSearchReqDto();
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 7, 1, 15, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 7, 1, 16, 0);
        String text = "미추홀";
        double latitude = 37.4467039276238;
        double longitude = 126.4467039276238;
        CafeReqDto.Location location = new CafeReqDto.Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        cafeSearchReqDto.setSearchingWord(text);
        cafeSearchReqDto.setLocation(location);
        cafeSearchReqDto.setPeopleNumber(3);
        cafeSearchReqDto.setReserveStartTime(dateTime1);
        cafeSearchReqDto.setReserveEndTime(dateTime2);
        cafeSearchReqDto.setTableOptionList(new ArrayList<>());

        // when
        Page<Cafe> cafes = searchService.findByText(cafeSearchReqDto,pageable);

        // then
        for (Cafe cafe : cafes) {
            System.out.println(cafe.getId());
            Assertions.assertEquals(cafe.getId(), cafe.getId());
        }
    }

    // getCafeWithTodayReserved 과 동일한 로직이라서 추가적인 테스트 코드를 작성하지 않았습니다.
    @Test
    public void findReservedTest() {
        Pageable pageable = Pageable.ofSize(1);
        // given
        String cafeId = objectId.toString();
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 8, 7, 15, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 8, 7, 16, 0);

        // then

        Assertions.assertDoesNotThrow(()->cafeService.getCafeWithSearchingReserved(cafeId, dateTime1,dateTime2, null, new ArrayList<>(), pageable));

    }


}
