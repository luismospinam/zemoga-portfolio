package com.zemoga.portfolio.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TwitterIntegrationServiceImplTest {

    @InjectMocks
    private TwitterIntegrationServiceImpl twitterIntegrationService;

    @Mock
    private RestTemplate restTemplateMock;

    private String bearerToken = "token";
    private String quantityTweets = "1";
    private String apiUrl = "twitter-url/";

    @BeforeEach
    public void setUpClass() {
        ReflectionTestUtils.setField(twitterIntegrationService, "bearerToken", bearerToken);
        ReflectionTestUtils.setField(twitterIntegrationService, "quantityTweets", quantityTweets);
        ReflectionTestUtils.setField(twitterIntegrationService, "apiUrl", apiUrl);
    }

    @Test
    void getLastTweetsFromUserEmptyUserShouldReturnEmptyList() {
        String username = null;

        List<String> responseTweets = twitterIntegrationService.getLastTweetsFromUser(username);

        Assert.assertTrue(responseTweets.isEmpty());
    }

    @Test
    void getLastTweetsFromUserIncorrectStatusResponseShouldReturnEmptyList() {
        String username = "twitterUsername";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        List<String> responseTweets = twitterIntegrationService.getLastTweetsFromUser(username);

        Assert.assertTrue(responseTweets.isEmpty());
    }

    @Test
    void getLastTweetsFromUserExceptionWhenInvokingTwitterShouldReturnEmptyList() {
        String username = "twitterUsername";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenThrow(new RuntimeException("error"));

        List<String> responseTweets = twitterIntegrationService.getLastTweetsFromUser(username);

        Assert.assertTrue(responseTweets.isEmpty());
    }

    @Test
    void getLastTweetsFromUserSucceed() {
        String username = "twitterUsername";
        String twitterResponse = "[  {\"text\": \"TwitterMessage\" } ]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(twitterResponse, HttpStatus.OK);
        when(restTemplateMock.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)))
                .thenReturn(responseEntity);

        List<String> responseTweets = twitterIntegrationService.getLastTweetsFromUser(username);

        Assert.assertEquals(1, responseTweets.size());
        Assert.assertEquals("TwitterMessage", responseTweets.get(0));
    }

    @Test
    void isValidTwitterUsername() {
        String username = "luis_2020";
        boolean isValid = TwitterIntegrationServiceImpl.isValidTwitterUsername(username);
        Assert.assertTrue(isValid);
    }

    @Test
    void isValidTwitterUsernameNotValidBecauseWhitespace() {
        String username = "luis 2020";
        boolean isValid = TwitterIntegrationServiceImpl.isValidTwitterUsername(username);
        Assert.assertFalse(isValid);
    }
}