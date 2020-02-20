package com.zemoga.portfolio.controller;

import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserPortfolioController {

    private PortfolioService portfolioService;

    @Autowired
    public UserPortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }


    @GetMapping(value = "/user_info/{idPortfolio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable("idPortfolio") final Integer idPortfolio) {
        Portfolio portfolio = portfolioService.findPortfolioById(idPortfolio);

        return ResponseEntity.ok(portfolio);
    }

    @PostMapping(value = "/modify_user_info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Portfolio> modifyUserInfo(@RequestBody final Portfolio portfolio) {
        return null;
    }
}
