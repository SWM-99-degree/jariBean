package com.example.jariBean.handler.constraintValidator;

import com.example.jariBean.dto.reserved.ReserveReqDto.TimeDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CustomValidator implements ConstraintValidator<CustomConstraint, TimeDto> {

    @Override
    public void initialize(CustomConstraint constraintAnnotation) {
        // 초기화 로직 (필요한 경우)
    }

    @Override
    public boolean isValid(TimeDto timeDto, ConstraintValidatorContext context) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = timeDto.getStartTime();
        LocalDateTime endTime = timeDto.getEndTime();

        if(now.isAfter(startTime) || now.isAfter(endTime)) {
            return false;
        }

        if(startTime.isAfter(endTime)) {
            return false;
        }

        if (!(startTime.getMinute() == 0 || startTime.getMinute() == 30) ||!(endTime.getMinute() == 0 || endTime.getMinute() == 30)) {
            return false;
        }

        return true;
    }
}
