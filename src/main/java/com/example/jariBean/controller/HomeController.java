package com.example.jariBean.controller;

import com.example.jariBean.dto.home.HomeResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    @GetMapping
    public HomeResDto home() {
        return null;
    }
}
