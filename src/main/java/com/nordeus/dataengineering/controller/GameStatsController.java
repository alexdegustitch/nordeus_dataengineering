package com.nordeus.dataengineering.controller;


import com.nordeus.dataengineering.dto.*;
import com.nordeus.dataengineering.service.GameInfoService;
import com.nordeus.dataengineering.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/game")
public class GameStatsController {


    @Autowired
    private GameInfoService gameInfoService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/gameStats")
    public ResponseEntity<GameStatsResponseDTO> getUserStats(@RequestBody GameStatsRequestDTO body) {
        return new ResponseEntity<>(gameInfoService.getGameStats(body.getDate()), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/gameStatsByCountry")
    public ResponseEntity<List<GameStatsByCountryResponseDTO>> getUserStatsByCountry(@RequestBody GameStatsRequestDTO body) {
        return new ResponseEntity<>(gameInfoService.getGameStatsByCountry(body.getDate()), HttpStatus.OK);
    }
}
