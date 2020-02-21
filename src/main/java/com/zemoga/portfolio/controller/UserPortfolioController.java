package com.zemoga.portfolio.controller;

import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;


@RestController
public class UserPortfolioController {

    private PortfolioService portfolioService;

    @Autowired
    public UserPortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }


    @GetMapping(value = "/user_info/{idPortfolio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable("idPortfolio") final Integer idPortfolio) {
        var portfolio = portfolioService.findPortfolioById(idPortfolio);

        return ResponseEntity.ok(portfolio);
    }

    @PostMapping(value = "/modify_user_info/{idPortfolio}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Portfolio> modifyPortfolioById(@RequestBody(required = false) final Portfolio portfolio, @PathVariable("idPortfolio") final Integer idPortfolio) {
        var portfolioResponse = portfolioService.updatePortfolio(portfolio, idPortfolio);

        return ResponseEntity.ok(portfolioResponse);
    }


    @PostMapping(value = "/modify_user_info/")
    public ResponseEntity<?> invalidModifyEndpoint() {
        var responseMap = new LinkedHashMap<String, String>();

        responseMap.put("location", "/modify_user_info/{idPortfolio}");
        responseMap.put("message", "This endpoint is not valid, should use '/modify_user_info/{idPortfolio}' endpoint instead");

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body(responseMap);
    }
}
