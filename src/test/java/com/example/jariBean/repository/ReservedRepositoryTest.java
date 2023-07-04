package com.example.jariBean.repository;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.User;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ReservedRepositoryTest {

    @Autowired
    CafeRepository cafeRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TableRepository tableRepository;
    @Autowired
    ReservedRepository reservedRepository;

    @Test
    public void saveTest() {
        User user = userRepository.findByUserPhoneNumber("01031315656").orElseThrow();
        Cafe cafe = cafeRepository.findByCafePhoneNumber("01012341234").orElseThrow();
        LocalDateTime dateTime3 = LocalDateTime.of(2023, 7, 1, 18-9, 0);
        LocalDateTime dateTime4 = LocalDateTime.of(2023, 7, 1, 19-9, 0);
        Table table = tableRepository.findById("64a27eaf7244d72ba3c28d1b").orElseThrow();
        Reserved reserved = new Reserved(user.getId(), cafe.getId(), table.getId(),dateTime3, dateTime4);

        reservedRepository.save(reserved);
    }

}
