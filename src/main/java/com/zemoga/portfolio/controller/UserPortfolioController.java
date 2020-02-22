package com.zemoga.portfolio.controller;

import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.service.PortfolioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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


    @ApiOperation(value = "Finds a Portfolio by ID and the tweets of its user.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Portfolio not found"),
            @ApiResponse(code = 500, message = "Server error")})
    @GetMapping(value = "/user_info/{idPortfolio}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable("idPortfolio") final String idPortfolio) {
        var portfolio = portfolioService.findPortfolioById(idPortfolio);

        return ResponseEntity.ok(portfolio);
    }


    @ApiOperation(value = "Modify a Portfolio by ID with the provided data.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Portfolio not found"),
            @ApiResponse(code = 500, message = "Server error")})
    @PostMapping(value = "/modify_user_info/{idPortfolio}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Portfolio> modifyPortfolioById(@RequestBody(required = false) final Portfolio portfolio, @PathVariable("idPortfolio") final String idPortfolio) {
        var portfolioResponse = portfolioService.updatePortfolio(portfolio, idPortfolio);

        return ResponseEntity.ok(portfolioResponse);
    }


    @ApiOperation(value = "INVALID ENDPOINT should use /modify_user_info/{idPortfolio} instead.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 301, message = "Moved permanently")})
    @PostMapping(value = "/modify_user_info/")
    public ResponseEntity<?> invalidModifyEndpoint() {
        var responseMap = new LinkedHashMap<String, String>();

        responseMap.put("location", "/modify_user_info/{idPortfolio}");
        responseMap.put("message", "This endpoint is not valid, should use '/modify_user_info/{idPortfolio}' endpoint instead");

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).body(responseMap);
    }
}
