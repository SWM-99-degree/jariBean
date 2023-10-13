package com.example.jariBean.service;

import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.handler.ex.CustomNoContentException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.example.jariBean.dto.reserved.ReserveReqDto.ReserveSaveReqDto;

@Service
@RequiredArgsConstructor
public class ReserveService {
    private final ReservedRepository reservedRepository;
    private final CafeRepository cafeRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    public Page<ReserveSummaryResDto> getMyReserved(String userId, Pageable pageable) {

        Page<Reserved> reservedList = reservedRepository.findByUserIdOrderByStartTimeAsc(userId, pageable);
        Page<ReserveSummaryResDto> reserveSummaryResDtoList = reservedList.map(reserved -> new ReserveSummaryResDto(reserved));
        return reserveSummaryResDtoList;
    }

    // 예약 삭제하기
    public void deleteMyReserved(String reservedId) {
        reservedRepository.deleteById(reservedId);
    }


    // 가장 가까운 예약
    public ReserveSummaryResDto getNearestReserved(String userId) {
        LocalDateTime userNow = LocalDateTime.now();

        Reserved reserved = reservedRepository.findNearestReserved(userId, userNow).orElseThrow(()-> new CustomDBException("데이터에 접근할 수 없습니다."));

        // 정보가 없다면 null 값으로 반환하며, 예외처리로 204를 보냄
        if (reserved == null) {
            throw new CustomNoContentException("예약이 존재하지 않습니다.");
        }

        // 예약 정보 넣기
        ReserveSummaryResDto reserveSummaryResDto = new ReserveSummaryResDto(reserved);
        return reserveSummaryResDto;
    }


    // 예약 신청
    @Transactional
    public void saveReserved(String userId, ReserveSaveReqDto saveReservedReqDto) {
        // 검증해야 할 테이블의 예약되어 있는 것들 중, 당일에 있는 것을 가져옴
        // 만약에 있으면 삭제
        boolean isExist = reservedRepository.isReservedByTableIdBetweenTime(saveReservedReqDto.getTableId(), saveReservedReqDto.getReservedStartTime(), saveReservedReqDto.getReservedEndTime());
        if (isExist){
            throw new CustomDBException("데이터가 중복됩니다.");
        }

        // 예약에 대한 처리
        Cafe cafe = cafeRepository.findById(saveReservedReqDto.getCafeId())
                .orElseThrow(() -> new CustomApiException("id에 해당하는 Cafe가 존재하지 않습니다."));

        Table table = tableRepository.findById(saveReservedReqDto.getTableId())
                .orElseThrow(() -> new CustomApiException("id에 해당하는 Table이 존재하지 않습니다."));

        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("id에 해당하는 User가 존재하지 않습니다."));

        Reserved reserved = saveReservedReqDto.toEntity(findUser, table, cafe);
        reservedRepository.save(reserved);
    }

}
