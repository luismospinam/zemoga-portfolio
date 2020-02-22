package com.zemoga.portfolio.service;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TwitterIntegrationServiceImpl implements TwitterIntegrationService {

    private String bearerToken;
    private String quantityTweets;
    private String apiUrl;
    private RestTemplate restTemplate;

    private Logger logger = LoggerFactory.getLogger(TwitterIntegrationServiceImpl.class);


    public TwitterIntegrationServiceImpl(@Value("${integration.twitter.bearer-token}") String bearerToken,
                                         @Value("${integration.twitter.quantity}") String quantityTweets,
                                         @Value("${integration.twitter.api-url}") String apiUrl,
                                         @Autowired RestTemplate restTemplate) {
        this.bearerToken = bearerToken;
        this.quantityTweets = quantityTweets;
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<String> getLastTweetsFromUser(String user) {
        List<String> tweets = new ArrayList<>();
        boolean isValidUser = !StringUtils.isEmpty(user) && isValidTwitterUsername(user); //Twitter will complain whitespaces in username
        if (isValidUser) {
            tweets = retrieveLastTweets(user);
        }

        return tweets;
    }

    private List<String> retrieveLastTweets(String user) {
        List<String> tweets = new ArrayList<>();
        String retrieveTweetsEndpoint = "statuses/user_timeline.json?screen_name=%s&count=%s";
        try {
            String twitterEndpoint = String.format(apiUrl + retrieveTweetsEndpoint, user, quantityTweets);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", bearerToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(twitterEndpoint, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                tweets = parseTwitterResponseToOnlyTweets(response.getBody());
            } else {
                logger.error("Incorrect status received from twitter integration %s, message: %s" + response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            logger.error("Integration with twitter went bad: " + e.getMessage(), e);
        }

        return tweets;
    }

    private List<String> parseTwitterResponseToOnlyTweets(String body) {
        Configuration config = Configuration.defaultConfiguration()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider())
                .setOptions(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS);

        DocumentContext documentContext = JsonPath.parse(body, config);
        TypeRef<List<String>> stringTypeRef = new TypeRef<>() {
        };
        return documentContext.read("$.[*].text", stringTypeRef);
    }

    public static boolean isValidTwitterUsername(String username) {
        String regexAlphanumericAndUnderscore = "^[a-zA-Z0-9_]+$";
        Pattern pattern = Pattern.compile(regexAlphanumericAndUnderscore);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
