package com.zemoga.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoga.portfolio.Exception.CustomPortfolioException;
import com.zemoga.portfolio.Exception.PortfolioNotFoundException;
import com.zemoga.portfolio.TestHelper;
import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.service.PortfolioServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserPortfolioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PortfolioServiceImpl portfolioService;


    @Test
    void getPortfolioByIdSuccessfulResponse() throws Exception {
        String id = "1";
        Portfolio dummyPortfolio = TestHelper.createDummyPortfolio(id);
        when(portfolioService.findPortfolioById(id)).thenReturn(dummyPortfolio);

        this.mockMvc.perform(get("/user_info/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idPortfolio\":\"1\"")))
                .andExpect(content().string(containsString("\"twitterUserName\":\"testUser\"")))
                .andExpect(content().string(containsString("\"imageUrl\":\"testImage\",\"description\"")))
                .andExpect(content().string(containsString("\"description\":\"testDescription\"")));
    }

    @Test
    void getPortfolioByIdNotFoundResponse() throws Exception {
        String id = "1";
        when(portfolioService.findPortfolioById(id)).thenThrow(new PortfolioNotFoundException("test-error-message"));

        this.mockMvc.perform(get("/user_info/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("\"Error\":\"Not Found\"")))
                .andExpect(content().string(containsString(",\"status\":404")))
                .andExpect(content().string(containsString("\"message\":\"test-error-message\"")));
    }

    @Test
    void getPortfolioByInvalidRequestShouldReturnBadRequest() throws Exception {
        String id = "invalid-id";
        when(portfolioService.findPortfolioById(id)).thenThrow(new CustomPortfolioException("invalid-error-message"));

        this.mockMvc.perform(get("/user_info/" + id))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("\"Error\":\"Bad Request\"")))
                .andExpect(content().string(containsString(",\"status\":400")))
                .andExpect(content().string(containsString("\"message\":\"invalid-error-message\"")));
    }

    @Test
    void modifyPortfolioByIdSucceedCase() throws Exception {
        String id = "1";
        Portfolio dummyPortfolio = TestHelper.createDummyPortfolio(id);
        String jsonContent = new ObjectMapper().writeValueAsString(dummyPortfolio);
        when(portfolioService.updatePortfolio(any(Portfolio.class), anyString())).thenReturn(dummyPortfolio);

        this.mockMvc.perform(post("/modify_user_info/1").contentType(MediaType.APPLICATION_JSON).content(jsonContent).characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"idPortfolio\":\"1\"")))
                .andExpect(content().string(containsString("\"twitterUserName\":\"testUser\"")))
                .andExpect(content().string(containsString("\"imageUrl\":\"testImage\",\"description\"")))
                .andExpect(content().string(containsString("\"description\":\"testDescription\"")));
    }

    @Test
    void invalidModifyEndpointShouldNotBeUsedMovedPermanently() throws Exception {
        Portfolio dummyPortfolio = TestHelper.createDummyPortfolio("1");
        String jsonContent = new ObjectMapper().writeValueAsString(dummyPortfolio);

        this.mockMvc.perform(post("/modify_user_info/").contentType(MediaType.APPLICATION_JSON).content(jsonContent).characterEncoding("utf-8"))
                .andExpect(status().isMovedPermanently())
                .andExpect(content().string(containsString("\"location\":\"/modify_user_info/{idPortfolio}")));
    }
}