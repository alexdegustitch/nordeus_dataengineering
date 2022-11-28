package com.nordeus.dataengineering.controller;


import com.nordeus.dataengineering.dto.UserStatsRequestDTO;
import com.nordeus.dataengineering.dto.UserStatsResponseDTO;
import com.nordeus.dataengineering.service.UserInfoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserStatsController {

    @Autowired
    private UserInfoService userInfoService;


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/userStats")
    public ResponseEntity<UserStatsResponseDTO> getUserStats(@RequestBody UserStatsRequestDTO body) {
        return new ResponseEntity<>(userInfoService.getUserStats(body.getUserId(), body.getDate()), HttpStatus.OK);
    }
}
