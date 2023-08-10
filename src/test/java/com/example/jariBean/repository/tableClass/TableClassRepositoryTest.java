package com.example.jariBean.repository.tableClass;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.table.TableRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
class TableClassRepositoryTest {

    @Autowired private CafeRepository cafeRepository;
    @Autowired private TableClassRepository tableClassRepository;
    @Autowired private TableRepository tableRepository;

    private Cafe globalCafe;


    @BeforeEach
    public void beforeEach() {
        Cafe cafe = Cafe.builder().build();
        globalCafe = cafeRepository.save(cafe);

        TableClass tableClass1 = TableClass.builder().cafeId(globalCafe.getId()).build();
        TableClass tableClass2 = TableClass.builder().cafeId(globalCafe.getId()).build();
        TableClass tableClass3 = TableClass.builder().cafeId(globalCafe.getId()).build();

        tableClassRepository.save(tableClass1);
        tableClassRepository.save(tableClass2);
        tableClassRepository.save(tableClass3);

    }


    @Test
    public void findByCafeIdTest() throws Exception {
        // given

        // when
        List<TableClass> tableClassList = tableClassRepository.findByCafeId(globalCafe.getId());

        // then
        Assertions.assertThat(tableClassList.size()).isEqualTo(3);
    }

}