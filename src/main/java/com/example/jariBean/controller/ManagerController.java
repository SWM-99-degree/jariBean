package com.example.jariBean.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manager")
public class ManagerController {

    @PutMapping("/matching/{status}")
    public void matching(@PathVariable("status") String status) {

    }

    @GetMapping("/reserve")
    public void reservePage() {

    }

    @GetMapping("/table")
    public void tablePage() {

    }

    @PutMapping("/table/{id}")
    public void updateTable(@PathVariable("id") String id) {

    }

    @PostMapping("/table")
    public void addTable() {

    }

    @PutMapping("/tableclass/{id}")
    public void updateTableClass(@PathVariable("id") String id)) {

    }

    @PostMapping("/tableclass")
    public void addTableClass() {

    }

}
